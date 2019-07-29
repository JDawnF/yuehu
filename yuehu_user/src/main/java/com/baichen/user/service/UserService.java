package com.baichen.user.service;

import com.baichen.entity.Contants;
import com.baichen.user.dao.UserDao;
import com.baichen.user.pojo.User;
import com.baichen.util.IdWorker;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 *
 * @author baichen
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //短信平台uid，非必填
    @Value("${uid}")
    private String uid;
    // 短信平台模板id
    @Value("${templateId}")
    private String templateId;

    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private HttpServletRequest request;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }


    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }


    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 增加用户
     *
     * @param code
     * @param user
     */
    public void add(String code, User user) {
        // 判断验证码是否在正确,key是smsCode_手机号码
        String code_redis = (String) redisTemplate.opsForValue().get("smsCode_" + user.getMobile());
        if (!StringUtils.isEmpty(code_redis)) {
            if (code_redis.equals(code)) {
                user.setId(idWorker.nextId() + "");
                user.setFanscount(0);       // 粉丝数
                user.setFollowcount(0);     // 关注数
                user.setOnline(0l);     // 在线时长
                user.setRegdate(new Date());
                user.setUpdatedate(new Date());
                user.setLastdate(new Date());
                //密码加密
                String newpassword = encoder.encode(user.getPassword());//加密后的
                user.setPassword(newpassword);
                userDao.save(user);
            } else {
                throw new RuntimeException(Contants.WRONG_SMSCODE);
            }
        } else {
            throw new RuntimeException(Contants.EMPTY_SMSCODE);
        }
    }

    /**
     * 修改
     *
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     * 删除,必须由admin角色才能删除,将判断请求头和claims抽取出来做成一个拦截器JwtFilter
     *
     * @param id
     */
    public void deleteById(String id) {
        //判断是否是管理员admin
        /*//从请求获取头信息 Authorization
        String authorization = request.getHeader("Authorization");
        if (authorization == null){
            return new Result(false,StatusCode.ACCESS_ERROR,"权限不足 authorization==null");
        }

        //如果authorization的值不以Bearer开头
        if(!authorization.startsWith("Bearer")){
            return new Result(false,StatusCode.ACCESS_ERROR,"权限不足 不以Bearer开头");
        }

        //截取token字符串
        String token = authorization.substring(7);

        //解析token
        Claims claims = jwtUtil.parseJWT(token);

        if(claims==null){
            return new Result(false,StatusCode.ACCESS_ERROR,"权限不足 claims==null");
        }

        if (!"admin".equals(claims.get("roles"))){
            return new Result(false,StatusCode.ACCESS_ERROR,"权限不足 roles");
        }*/
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 手机号码
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                // 昵称
                if (searchMap.get("loginname") != null && !"".equals(searchMap.get("loginname"))) {
                    predicateList.add(cb.like(root.get("loginname").as(String.class), "%" + (String) searchMap.get("loginname") + "%"));
                }
                // 性别
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
                }
                // 头像
                if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
                }
                // E-Mail
                if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
                }
                // 兴趣
                if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
                }
                // 个性
                if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    /**
     * 发送手机验证码
     *
     * @param mobile
     */
    public void sendsms(String mobile) {
        //通过lang这个包生成6位随机数
        String code = RandomStringUtils.randomNumeric(6);
        System.out.println("验证码：" + code);

        //保存到redis中，10分钟过期
        redisTemplate.opsForValue().set("smsCode_" + mobile, code, 10, TimeUnit.MINUTES);

        Map<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("templateId", templateId);  // 短信模板id
        map.put("uid", uid);//非必填
        map.put("code", code + ",1");       // 表示一分钟有效

        rabbitTemplate.convertAndSend("sms", map);
    }

    /**
     * 用户登录
     *
     * @param mobile
     * @param password
     * @return
     */
    public User login(String mobile, String password) {
        User user = userDao.findByMobile(mobile);
        // 注意顺序不能写反
        if (user != null && encoder.matches(password, user.getPassword()))
            return user;
        return null;
    }

    /**
     * 根据手机号和密码查询用户 * @param mobile
     *
     * @param password
     * @return
     */
    public User findByMobileAndPassword(String mobile, String password) {
        User user = userDao.findByMobile(mobile);
        if (user != null && encoder.matches(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    // 更新粉丝数和关注数
    public void updateFansCountAndFollowCount(int x, String userid, String friendid) {
        userDao.updateFanswcount(x, friendid);
        userDao.updateFollowcount(x, userid);
    }
}
