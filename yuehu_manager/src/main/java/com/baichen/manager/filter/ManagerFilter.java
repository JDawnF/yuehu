package com.baichen.manager.filter;

import com.baichen.util.JwtUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 设置前置过滤器
     *
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
     *
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
        System.out.println("执行了Zuul的过滤器....");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String url = request.getRequestURL().toString();
        if (url.indexOf("/admin/login") > 0) {
            System.out.println("登陆页面" + url);
            return null;
        }
        String authHeader = (String) request.getHeader("Authorization");// 获取头信息
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = jwtUtil.parseJWT(token);
            if (claims != null) {
                if ("admin".equals(claims.get("roles"))) {
                    requestContext.addZuulRequestHeader("Authorization", authHeader);
                    System.out.println("token 验证通过，添加了头信息" + authHeader);
                    return null;
                }
            }
        }
// 这里通过 ctx.setSendZuulResponse(false) 令zuul过滤该请求，
// 不对其 进行路由，然后通过 ctx.setResponseStatusCode(401) 设置了其返回的错误码
        requestContext.setSendZuulResponse(false);  //终止运行
        requestContext.setResponseStatusCode(401);//http状态码
        requestContext.setResponseBody("无权访问");
        requestContext.getResponse().setContentType("text/html;charset=UTF‐8");
        return null;
    }
}

