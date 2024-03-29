package com.baichen.spit.service;

import com.baichen.spit.dao.SpitDao;
import com.baichen.spit.pojo.Spit;
import com.baichen.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SpitService {
    @Autowired      // 默认按类型注入，Resource默认按名称注入，找不到按类型注入
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    //引入MongoDb模板
    @Autowired
    private MongoTemplate mongoTemplate;

    //发布吐槽
    public void add(Spit spit) {
        // 赋初始值，防止空指针
        spit.setId(idWorker.nextId() + "");
        spit.setPublishtime(new Date());// 发布日期
        spit.setVisits(0);// 浏览量
        spit.setShare(0);// 分享数
        spit.setThumbup(0);// 点赞数
        spit.setComment(0);// 回复数
        spit.setState("1");// 状态

        spitDao.save(spit);
        String parentid = spit.getParentid();
        //判断哪些是吐槽的评论，如果当前添加的吐槽有父节点，那么父节点的吐槽回复数要加一
        if (parentid != null && !"".equals(parentid)) {
            //更新该评论对应的吐槽的回复数+1
            //1.创建查询对象
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(parentid));
            //2.创建修改对象
            Update update = new Update();
            update.inc("comment", 1);
            mongoTemplate.updateFirst(query, update, "spit");
        }
    }

    //查询全部
    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    //根据ID查找
    public Spit findById(String spitId) {
        return spitDao.findById(spitId).get();
    }

    //修改
    public void update(Spit spit) {
        spitDao.save(spit);
    }

    //删除
    public void delete(String spitId) {
        spitDao.deleteById(spitId);
    }

    //根据上级ID查询吐槽列表(分页)
    public Page<Spit> findByParentid(String parentid, int page, int size) {
        return spitDao.findByParentidOrderByPublishtime(parentid, PageRequest.of(page - 1, size));
    }

    //点赞
    public void thumbup(String id) {
       /* //实现方法1
        Spit spit = findById(id);
        // 先判断
        spit.setThumbup(spit.getThumbup()==null?0:spit.getThumbup()+1);
        update(spit);*/

        //实现方法2，用原生的MongoDB命令来实现自增，db.spit.update({"_id":"1"},{$inc:{thumbup:NumberInt(1)}})
        //1.创建条件
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        //2.创建修改对象
        Update update = new Update();
        //inc :增加字段值
        update.inc("thumbup", 1);

        //3.使用模板进行修改
        mongoTemplate.updateFirst(query, update, "spit");
    }

    //增加访问量
    public void visit(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("visits", 1);
        mongoTemplate.updateFirst(query, update, "spit");
    }

    //增加分享
    public void share(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("share", 1);
        mongoTemplate.updateFirst(query, update, "spit");
    }
}
