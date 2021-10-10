package com.yetiwu.crm.web.filter;

import com.yetiwu.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        // 1、获取当前请求对象的HttpSession对象
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String uri = request.getRequestURI();
        if(uri.toLowerCase().contains("login") || "/crm/login.jsp".equals(uri)){
            chain.doFilter(req,resp);
        }else{
            if(user != null){
                chain.doFilter(req,resp);
            }else{
                response.sendRedirect(request.getContextPath() + "/login.jsp");
            }
        }

    }
}
