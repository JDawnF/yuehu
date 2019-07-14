package com.baichen.base.service;

import com.baichen.base.dao.LabelDao;
import com.baichen.base.pojo.Label;
import com.baichen.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Program: LabelService
 * @Author: baichen
 * @Description: 标签service
 */
@Service
public class LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 增加标签
     *
     * @param label
     */
    public void add(Label label) {
        label.setId(String.valueOf(idWorker.nextId()));
        labelDao.save(label);
    }

    /**
     * 查询全部标签
     *
     * @return
     */
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     * 根据ID查找标签,findById(id)是一个容器对象Optional,
     *
     * @param id
     * @return
     */
    public Label findById(String id) {
        return labelDao.findById(id).get();
    }

    /**
     * 修改标签
     *
     * @param label
     */
    public void update(Label label) {
        labelDao.save(label);
    }

    /**
     * 根据ID 删除标签
     *
     * @param id
     */
    public void delete(String id) {
        labelDao.deleteById(id);
    }

    /**
     * 条件查询
     *
     * @param searchMap
     * @return Specification可以封装查询条件
     */
    public List<Label> search(Map searchMap) {
        Specification<Label> specification = createSpecification(searchMap);
        return labelDao.findAll(specification);
    }

    /**
     * 条件查询+分页
     *
     * @return
     */
    public Page<Label> search(Map searchMap, int page, int size) {
        Specification<Label> specification = createSpecification(searchMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);   // PageRequest页码是从0开始，所以要page-1
        return labelDao.findAll(specification, pageRequest);
    }

    /**
     * 构造查询条件
     *
     * @param searchMap
     * @return
     */
    private Specification<Label> createSpecification(Map searchMap) {
        return new Specification<Label>() {
            /**
             * @param root 根对象，也就是要把条件封装到哪个对象中，where 类名=label.getid,like中是泛型中的属性名
             * @param criteriaQuery     封装的都是查询关键字，比如 group by,order by等
             * @param criteriaBuilder   用来封装条件对象的
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                String labelName = (String) searchMap.get("labelName");
                String state = (String) searchMap.get("state");
                String recommend = (String) searchMap.get("recommend");
                // new一个list集合，存放所有的条件
                ArrayList<Predicate> predicates = new ArrayList<>();
                if (labelName != null && !"".equals(labelName)) {   // 判断
                    // 获取Label中的属性名，as是作为什么类型
                    predicates.add(criteriaBuilder.like(root.get("labelName").as(String.class), "%" + labelName + "%"));
                }
                if (state != null && !"".equals(state)) {
                    predicates.add(criteriaBuilder.equal(root.get("state").as(String.class), state));   // where state= "state"
                }
                if (recommend != null && !"".equals(recommend)) {
                    predicates.add(criteriaBuilder.equal(root.get("recommend").as(String.class), recommend));
                }
                // new一个数组作为最终返回值的条件
                Predicate[] predicatesArray = new Predicate[predicates.size()];
                // and方法中要初始化长度，但是长度是动态的，predicates.toArray(predicatesArray)把list直接转成数组
                return criteriaBuilder.and(predicates.toArray(predicatesArray));    // where labelName like "%小明%" and state="1"
            }
        };
    }
}
