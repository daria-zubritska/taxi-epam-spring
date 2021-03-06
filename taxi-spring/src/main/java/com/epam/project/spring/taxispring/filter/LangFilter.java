package com.epam.project.spring.taxispring.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class LangFilter implements Filter {

    public static final String EN_LANG = "en";
    public static final String UA_LANG = "ua";
    public static final String LANG_COOKIE_NAME = "lang";
    public static final String ENCODING = "UTF-8";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        request.setCharacterEncoding(ENCODING);
        response.setCharacterEncoding(ENCODING);

        HttpServletRequest req = (HttpServletRequest) request;
        String lang = getCookieValue(req);

        if(lang != null && (lang.equals(EN_LANG) || lang.equals(UA_LANG)))
            request.setAttribute(LANG_COOKIE_NAME, lang);
        else
            request.setAttribute(LANG_COOKIE_NAME, UA_LANG);

        chain.doFilter(request, response);
    }

    private String getCookieValue(HttpServletRequest request) {
        if(request != null) {
            Cookie[] cookies = request.getCookies();
            if(cookies != null)
                for (Cookie cookie : cookies) {
                    if (LANG_COOKIE_NAME.equalsIgnoreCase(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
        }
        return null;
    }

}
