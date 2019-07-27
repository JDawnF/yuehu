package com.baichen.qa;

import com.baichen.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//@FeignClient注解用于指定从哪个服务中调用功能 ，注意 里面的名称与被调用的服务 名保持一致，并且不能包含下划线。
@FeignClient("yuehu-base")
public interface LabelClient {
    // 根据ID查询标签
    //@RequestMapping注解用于对被调用的微服务进行地址映射。注意 @PathVariable注 解一定要指定参数名称，否则出错
    @RequestMapping(value="/label/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id);
}
