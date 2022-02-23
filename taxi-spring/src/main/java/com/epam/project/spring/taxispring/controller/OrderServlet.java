package com.epam.project.spring.taxispring.controller;

import com.epam.project.spring.taxispring.model.CarDAO;
import com.epam.project.spring.taxispring.model.OrderDAO;
import com.epam.project.spring.taxispring.model.dto.DoubleOrderDTO;
import com.epam.project.spring.taxispring.model.dto.OrderDTO;
import com.epam.project.spring.taxispring.model.entity.Car;
import com.epam.project.spring.taxispring.model.entity.User;
import com.epam.project.spring.taxispring.model.service.OrderService;
import com.epam.project.spring.taxispring.utils.RequestUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "OrderServlet", value = "/makeOrder")
public class OrderServlet extends HttpServlet {

    private static String PATH = "/WEB-INF/pages/order.jsp";
    private final static String LOCATION_ATTRIBUTE = "locationList";
    private final static String ERROR_ATTRIBUTE = "error";
    private final static String CHOICE_ATTRIBUTE = "orderChoice";
    private static final String USER_ATTRIBUTE = "user";
    private static final String ABSENT_CHOICE_ATTRIBUTE = "absentUserChoice";
    private static final String DOUBLE_ORDER_ATTRIBUTE = "doubleOrder";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //redirect if not logged in
        if (RequestUtils.getSessionAttribute(req, USER_ATTRIBUTE, User.class) == null) {
            resp.sendRedirect("/logIn");
            return;
        }

        OrderDAO orderDAO = OrderDAO.getInstance();

        //prepare locations lists
        req.setAttribute(LOCATION_ATTRIBUTE, orderDAO.getAllLocations());

        req.getRequestDispatcher(PATH).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //redirect if not logged in
        if (RequestUtils.getSessionAttribute(req, USER_ATTRIBUTE, User.class) == null) {
            resp.sendRedirect("/logIn");
            return;
        }

        OrderDAO orderDAO = OrderDAO.getInstance();
        OrderService orderService = new OrderService(orderDAO);

        CarDAO carDAO = CarDAO.getInstance();

        HttpSession session = req.getSession(true);

        //get parameters for search
        String loc_from = req.getParameter("loc_from");
        String loc_to = req.getParameter("loc_to");
        String passengers = req.getParameter("passengers");
        String carClass = req.getParameter("class");

        Map<String, String> viewAttributes = new HashMap<>();
        viewAttributes.put("loc_from", loc_from);
        viewAttributes.put("loc_to", loc_to);
        viewAttributes.put("passengers", passengers);
        viewAttributes.put("class", carClass);


        //validate parameters
        if(loc_from == null || loc_to == null){
            viewAttributes.put(ERROR_ATTRIBUTE, "locationNotValid");
            passErrorToView(req, resp, viewAttributes);
            return;
        }
        if(passengers == null){
            viewAttributes.put(ERROR_ATTRIBUTE, "passengersNotValid");
            passErrorToView(req, resp, viewAttributes);
            return;
        }

        Car car = carDAO.findAppropriateCar(carClass, Integer.parseInt(passengers));

        //finding appropriate car or cars
        if(car == null){

            List<Car> carsByType = carDAO.findTwoCarsByType(carClass, Integer.parseInt(passengers));

            //finding two cars of the needed type
            if(carsByType.size() == 2){

                BigDecimal idealCost = carDAO.findAppropriateCarCost(carClass, Integer.parseInt(passengers));

                User user = (User) session.getAttribute(USER_ATTRIBUTE);
                Date date = new Date();

                BigDecimal cost = orderService.costForTwoCars(carsByType, loc_from, loc_to);
                BigDecimal costWithDiscount = orderService.costWithDiscountForTwoCars(idealCost, carsByType, loc_from, loc_to);

                session.setAttribute(ABSENT_CHOICE_ATTRIBUTE, "noNeeded");

                DoubleOrderDTO doubleOrder = new DoubleOrderDTO();

                Car car1 = carsByType.get(0);
                Car car2 = carsByType.get(1);

                doubleOrder.setOrder1(new OrderDTO(0,
                        car1.getName(),
                        user.getId(),
                        car1.getId(),
                        date.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate(),
                        loc_to,
                        loc_from,
                        Integer.parseInt(passengers),
                        cost,
                        car1.getCategory().toString()));

                doubleOrder.setOrder2(new OrderDTO(0,
                        car2.getName(),
                        user.getId(),
                        car2.getId(),
                        date.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate(),
                        loc_to,
                        loc_from,
                        Integer.parseInt(passengers),
                        cost,
                        car2.getCategory().toString()));


                doubleOrder.setFullCost(cost);
                doubleOrder.setCostWithDiscount(costWithDiscount);

                session.setAttribute(DOUBLE_ORDER_ATTRIBUTE, doubleOrder);

            } else{

                Car carByPass = carDAO.findCarByPassengers(Integer.parseInt(passengers));

                //finding car by passengers amount
                if(carByPass != null){

                    BigDecimal idealCost = carDAO.findAppropriateCarCost(carClass, Integer.parseInt(passengers));

                    User user = (User) session.getAttribute(USER_ATTRIBUTE);
                    Date date = new Date();

                    BigDecimal cost = orderService.cost(carByPass.getCost(), loc_from, loc_to);
                    BigDecimal costWithDiscount = orderService.costWithDiscount(idealCost, carByPass.getCost(), loc_from, loc_to);

                    session.setAttribute(ABSENT_CHOICE_ATTRIBUTE, "noNeeded");

                    OrderDTO order = new OrderDTO(0,
                            carByPass.getName(),
                            user.getId(),
                            carByPass.getId(),
                            date.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate(),
                            loc_to,
                            loc_from,
                            Integer.parseInt(passengers),
                            cost,
                            carByPass.getCategory().toString());

                    order.setCostWithDiscount(costWithDiscount);

                    session.setAttribute(CHOICE_ATTRIBUTE, order);
                }
            }

            resp.sendRedirect("/orderSubmit");
            return;

        }else{

            //if we have appropriate car
            User user = (User) session.getAttribute(USER_ATTRIBUTE);
            Date date = new Date();

            BigDecimal cost = orderService.cost(car.getCost(), loc_from, loc_to);

            session.setAttribute(CHOICE_ATTRIBUTE, new OrderDTO(0,
                    car.getName(),
                    user.getId(),
                    car.getId(),
                    date.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate(),
                    loc_to,
                    loc_from,
                    Integer.parseInt(passengers),
                    cost,
                    car.getCategory().toString()));

            resp.sendRedirect("/orderSubmit");

        }

    }

    private void passErrorToView(HttpServletRequest req, HttpServletResponse resp, Map<String, String> viewAttributes) throws ServletException, IOException {
        for(Map.Entry<String, String> entry : viewAttributes.entrySet())
            req.setAttribute(entry.getKey(), entry.getValue());

        doGet(req, resp);
    }
}