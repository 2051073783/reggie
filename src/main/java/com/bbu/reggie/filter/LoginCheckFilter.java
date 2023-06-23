package com.bbu.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.bbu.reggie.common.BaseContext;
import com.bbu.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();

        String[] urls ={
                "/employee/logout",
                "/employee/login",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        boolean check = check(urls, requestURI);
        if (check){
            filterChain.doFilter(request,response);
            return;
        }
        if (request.getSession().getAttribute("employee") != null){

            Long empId = (Long) request.getSession().getAttribute("employee");

            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        if (request.getSession().getAttribute("user") != null){

            Long userId = (Long) request.getSession().getAttribute("user");

            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }
//        return R.error("NOTLOGIN");
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match){
                return true;
            }
        }
        return false;
    }
}
