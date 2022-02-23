package com.epam.project.spring.taxispring.controller;

import com.epam.project.spring.taxispring.model.UserDAO;
import com.epam.project.spring.taxispring.model.entity.User;
import com.epam.project.spring.taxispring.utils.RequestUtils;
import com.epam.project.spring.taxispring.utils.Security;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

    private static final String USER_ATTRIBUTE = "user";
    private static final String ERROR_ATTRIBUTE = "error";
    private static String PATH = "/WEB-INF/pages/register.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //if already logged in send to main page
        if (RequestUtils.getSessionAttribute(req, USER_ATTRIBUTE, User.class) != null) {
            resp.sendRedirect("/makeOrder");
        } else {
            req.getRequestDispatcher(PATH).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //if already logged in send to main page
        if (RequestUtils.getSessionAttribute(req, USER_ATTRIBUTE, User.class) != null) {

            resp.sendRedirect("/");
            return;
        }

        UserDAO userDAO = UserDAO.getInstance();

        //get parameters for validation
        String email = req.getParameter("email").toLowerCase().trim();
        String password = req.getParameter("password");
        String name = req.getParameter("username");

        Map<String, String> viewAttributes = new HashMap<>();
        viewAttributes.put("email", email);
        viewAttributes.put("username", name);

        //validating parameters
        if(!Security.isPasswordValid(password)) {
            viewAttributes.put(ERROR_ATTRIBUTE, "passwordNotValid");
            passErrorToView(req, resp, viewAttributes);
            return;
        }
        if(!Security.isEmailValid(email)) {
            viewAttributes.put(ERROR_ATTRIBUTE, "emailNotValid");
            passErrorToView(req, resp, viewAttributes);
            return;
        }
        if(name == null || name.isBlank()) {
            viewAttributes.put(ERROR_ATTRIBUTE, "usernameNotValid");
            passErrorToView(req, resp, viewAttributes);
            return;
        }

        //does this user already exist
        if(userDAO.findUserByEmail(email) != null) {
            viewAttributes.put(ERROR_ATTRIBUTE, "emailExists");
            passErrorToView(req, resp, viewAttributes);
            return;
        }

        boolean userAdded = false;

        //trying to add user
        try {
            userAdded = userDAO.addUser(email, Security.hashPassword(password), name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //setting session or telling user about mistake
        if(!userAdded) {
            viewAttributes.put(ERROR_ATTRIBUTE, "somethingWrong");
            passErrorToView(req, resp, viewAttributes);
        } else {
            User user = userDAO.findUserByEmail(email);

            HttpSession session = req.getSession(true);
            session.setAttribute(USER_ATTRIBUTE, user);
            session.setMaxInactiveInterval(86400); // 1 day
            resp.sendRedirect("/");
        }
    }

    private void passErrorToView(HttpServletRequest request, HttpServletResponse response, Map<String, String> viewAttributes) throws ServletException, IOException {
        for (Map.Entry<String, String> entry : viewAttributes.entrySet())
            request.setAttribute(entry.getKey(), entry.getValue());
        request.getRequestDispatcher(PATH).forward(request, response);
    }
}
