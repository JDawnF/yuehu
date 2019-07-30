package com.baichen.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

@Component
public class WebFilter extends ZuulFilter {
    /**
     * 设置前置过滤器
     * @return 返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过
     * 滤器类型:
     * pre:可以在请求路由之前被调用
     * route:在路由请求是被调用
     * post:在route和error过滤器之后被调用
     * error：处理请求时发生错误被调用
     */
    @Override
    public String filterType() {
        return "pre";
    }


    /**
     * 何止过滤器执行顺序,通过int值来定义过滤器的执行顺序
     */
    @Override
    public int filterOrder() {
        return 0;  //优先级为0,数值越大，执行优先级越低
    }


    /**
     * 是否执行过滤器
     * true 执行过滤器,则要过滤
     * false 不执行过滤器
     * @return 返回一个boolean类型来判断该过滤器是否要执行，所以通过此函数可
     * 实现过滤器的开关。在上例中，我们直接返回true，所以该过滤器总是生效
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的逻辑
     * 如果return null 代表放行
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("执行了zuul的过滤器....");
        return null;
    }
}
