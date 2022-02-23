package com.epam.project.spring.taxispring.controller;

import com.epam.project.spring.taxispring.model.CarDAO;
import com.epam.project.spring.taxispring.model.OrderDAO;
import com.epam.project.spring.taxispring.model.dto.DoubleOrderDTO;
import com.epam.project.spring.taxispring.model.dto.OrderDTO;
import com.epam.project.spring.taxispring.model.entity.User;
import com.epam.project.spring.taxispring.utils.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

import static com.epam.project.spring.taxispring.filter.LoginFilter.USER_ATTRIBUTE;

@WebServlet(name = "OrderPossibilityServlet", value = "/orderSubmit")
public class OrderPossibilityServlet extends HttpServlet {

    private static String PATH = "/WEB-INF/pages/orderPossibility.jsp";

    private final static String CHOICE_ATTRIBUTE = "orderChoice";
    private static final String DOUBLE_ORDER_ATTRIBUTE = "doubleOrder";
    private static final String ABSENT_CHOICE_ATTRIBUTE = "absentUserChoice";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //if user is not logged in send them to login page
        if (RequestUtils.getSessionAttribute(req, USER_ATTRIBUTE, User.class) == null) {
            resp.sendRedirect("/logIn");
            return;
        }

        req.getRequestDispatcher(PATH).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //if user is not logged in send them to login page
        if (RequestUtils.getSessionAttribute(req, USER_ATTRIBUTE, User.class) == null) {
            resp.sendRedirect("/logIn");
            return;
        }

        OrderDAO orderDAO = OrderDAO.getInstance();
        CarDAO carDAO = CarDAO.getInstance();

        HttpSession session = req.getSession();

        //depending on button continue
        if (req.getParameter("submit") != null) {

            OrderDTO order = (OrderDTO) session.getAttribute(CHOICE_ATTRIBUTE);

            if(order !=null) {

                //add order depending on its type
                if(session.getAttribute(ABSENT_CHOICE_ATTRIBUTE) == null){
                    orderDAO.addOrder(order.getUserId(), order.getCarId(),
                            order.getLocationFrom(), order.getLocationTo(),
                            order.getOrderDate(), order.getPassengers(), order.getCost());
                } else{
                    orderDAO.addOrder(order.getUserId(), order.getCarId(),
                            order.getLocationFrom(), order.getLocationTo(),
                            order.getOrderDate(), order.getPassengers(), order.getCostWithDiscount());
                }

                carDAO.updateStatus(order.getCarId(), 2);

            } else {
                //add two orders if it`s a double order
                DoubleOrderDTO dOrder = (DoubleOrderDTO) session.getAttribute(DOUBLE_ORDER_ATTRIBUTE);

                OrderDTO order1 = dOrder.getOrder1();
                OrderDTO order2 = dOrder.getOrder2();

                BigDecimal costOneCar = dOrder.getCostWithDiscount().divide(BigDecimal.valueOf(2));

                orderDAO.addOrder(order1.getUserId(), order1.getCarId(),
                        order1.getLocationFrom(), order1.getLocationTo(),
                        order1.getOrderDate(), order1.getPassengers(), costOneCar);

                orderDAO.addOrder(order2.getUserId(), order2.getCarId(),
                        order2.getLocationFrom(), order2.getLocationTo(),
                        order2.getOrderDate(), order1.getPassengers(), costOneCar);

                carDAO.updateStatus(order1.getCarId(), 2);
                carDAO.updateStatus(order2.getCarId(), 2);
            }

            req.setAttribute("wait", "waitForCar");

            doGet(req, resp);

        } else if (req.getParameter("cancel") != null) {
            session.removeAttribute(CHOICE_ATTRIBUTE);

            resp.sendRedirect("/");
        } else if (req.getParameter("ok") != null){
            //send to main page if messages is shown
            resp.sendRedirect("/");
        }

    }

}