package com.epam.project.spring.taxispring.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class RequestUtils {

    public static <T> T getSessionAttribute(HttpServletRequest request, String attributeName, Class<T> clazz) {
        HttpSession session = request.getSession(false);
        if (session != null && attributeName != null) {
            Object attribute = session.getAttribute(attributeName);
            if (clazz.isInstance(attribute)) {
                return clazz.cast(attribute);
            }
        }
        return null;
    }


}