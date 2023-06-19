package com.atcpl.crowd.filter;

import com.atcpl.crowd.constant.AccessPermitThroughResources;
import com.atcpl.crowd.constant.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author cpl
 * @date 2023/1/5
 * @apiNote
 */
@Component
public class CrowdAccessFilter extends ZuulFilter {
    @Override
    public String filterType() {
        // 返回 pre 意思是在目标我服务前去执行过滤
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        // 获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        // 通过requestContext对象获取当前请求对象（框架底层借助ThreadLocal从当前线程上获取实现绑定的Request对象）
        HttpServletRequest request = requestContext.getRequest();
        // 获取ServletPath的值
        String servletPath = request.getServletPath();
        // 判断是否属于放行的请求
        boolean b = AccessPermitThroughResources.PERMIT_THROUGH_REQUEST_RESOURCES_SET.contains(servletPath);
        if (b) {
            // 如果当前请求是可以直接放行的请求，则返回false放行
            return false;
        }
        // 判断当前请求是否为静态资源 true：当前请求是静态资源请求，false表示放行不做登录检查
        return !AccessPermitThroughResources.judgeCurrentServletPathWhetherStaticResource(servletPath);
    }

    @Override
    public Object run() throws ZuulException {
        // 获取当前请求对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        // 获取session
        HttpSession session = request.getSession();
        // 从session中获取已登录的用户
        Object member = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        // 判断member是否为空
        if (member == null) {
            // 从requestContext对象中获取response对象
            HttpServletResponse response = requestContext.getResponse();
            // 将提示消息存入Session域中
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_ACCESS_FORBIDDEN);
            // 重定向到auth模块的登录页面
            try {
                response.sendRedirect("/fg/auth/to/login/page.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
