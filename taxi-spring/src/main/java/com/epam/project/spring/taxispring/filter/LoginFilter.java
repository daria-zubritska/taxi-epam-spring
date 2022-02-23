package com.epam.project.spring.taxispring.filter;

import com.epam.project.spring.taxispring.model.entity.User;
import com.epam.project.spring.taxispring.utils.RequestUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class LoginFilter implements Filter {

    public static final List<String> ADMIN_PATHS = Arrays.asList("/statistics");
    public static final String USER_ATTRIBUTE = "user";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;

            if (ADMIN_PATHS.contains(req.getServletPath())) {

                User user = RequestUtils.getSessionAttribute(req, USER_ATTRIBUTE, User.class);
                if (user == null || user.getRole() != User.Role.ADMIN) {
                    request.getRequestDispatcher("/error").forward(request, response);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

}
