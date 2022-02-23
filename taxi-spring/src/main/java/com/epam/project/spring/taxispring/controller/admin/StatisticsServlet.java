package com.epam.project.spring.taxispring.controller.admin;

import com.epam.project.spring.taxispring.model.OrderDAO;
import com.epam.project.spring.taxispring.model.entity.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "StatisticsServlet", value = "/statistics")
public class StatisticsServlet extends HttpServlet {


    private static String PATH = "/WEB-INF/pages/statistics.jsp";
    private final static String STATISTICS_ATTRIBUTE = "statsList";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        OrderDAO orderDAO = OrderDAO.getInstance();

        int currentPage;
        int recordsPerPage;

        String userName = req.getParameter("userName");
        String date = req.getParameter("date");

        String order = req.getParameter("orderBy");

        //set fields and parameters if exist
        if (order != null) {
            if (order.equals("noOrder")) {
                req.setAttribute("currOrder", order);
            } else if (order.equals("byDate")) {
                req.setAttribute("currOrder", order);
            } else if (order.equals("byCost")) {
                req.setAttribute("currOrder", order);
            }
        }

        if (userName != null && date != null) {

            if (!userName.equals("") && date.equals("")) {
                req.setAttribute("currFilter", "user");
                req.setAttribute("userField", userName);
            } else if (userName.equals("") && !date.equals("")) {
                req.setAttribute("currFilter", "date");
                req.setAttribute("dateField", date);
            } else if (!userName.equals("") && !date.equals("")) {
                req.setAttribute("currFilter", "all");
                req.setAttribute("userField", userName);
                req.setAttribute("dateField", date);
            }
        }

        //check and set current page
        if (req.getParameter("currentPage") == null || req.getParameter("recordsPerPage") == null) {
            currentPage = 1;
            recordsPerPage = 5;
        } else {
            currentPage = Integer.parseInt(req.getParameter("currentPage"));
            recordsPerPage = Integer.parseInt(req.getParameter("recordsPerPage"));
        }

        List<Order> orders;
        int rows;

        //get list according to filters and sorting
        if (req.getAttribute("currFilter") == null) {

            if(order != null) {
                switch (order) {
                    case ("byCost"): {
                        orders = orderDAO.getOrdersNoFilterOrderedCost(currentPage * recordsPerPage - recordsPerPage,
                                recordsPerPage);
                        break;
                    }
                    case ("byDate"): {
                        orders = orderDAO.getOrdersNoFilterOrderedDate(currentPage * recordsPerPage - recordsPerPage,
                                recordsPerPage);
                        break;
                    }
                    default: {
                        orders = orderDAO.getOrdersNoFilter(currentPage * recordsPerPage - recordsPerPage,
                                recordsPerPage);
                        break;
                    }
                }
            }else{
                orders = orderDAO.getOrdersNoFilter(currentPage * recordsPerPage - recordsPerPage,
                        recordsPerPage);
            }

            rows = orderDAO.getNumberOfRows();
        } else if (req.getAttribute("currFilter") == "user") {

            switch (order) {
                case ("byCost"): {
                    orders = orderDAO.getOrdersUserFilterOrderedCost(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("userField").toString());
                    break;
                }
                case ("byDate"): {
                    orders = orderDAO.getOrdersUserFilterOrderedDate(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("userField").toString());
                    break;
                }
                default: {
                    orders = orderDAO.getOrdersUserFilter(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("userField").toString());
                    break;
                }
            }

            rows = orderDAO.getNumberOfRowsFilterUser(req.getAttribute("userField").toString());
        } else if (req.getAttribute("currFilter") == "date") {

            switch (order) {
                case ("byCost"): {
                    orders = orderDAO.getOrdersDateFilterOrderedCost(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("dateField").toString());
                    break;
                }
                case ("byDate"): {
                    orders = orderDAO.getOrdersDateFilterOrderedDate(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("dateField").toString());
                    break;
                }
                default: {
                    orders = orderDAO.getOrdersDateFilter(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("dateField").toString());
                    break;
                }
            }

            rows = orderDAO.getNumberOfRowsFilterDate(req.getAttribute("dateField").toString());
        } else {

            switch (order) {
                case ("byCost"): {
                    orders = orderDAO.getOrdersUserAndDateFilterOrderedCost(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("userField").toString(), req.getAttribute("dateField").toString());
                    break;
                }
                case ("byDate"): {
                    orders = orderDAO.getOrdersUserAndDateFilterOrderedDate(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("userField").toString(), req.getAttribute("dateField").toString());
                    break;
                }
                default: {
                    orders = orderDAO.getOrdersUserAndDateFilter(currentPage * recordsPerPage - recordsPerPage,
                            recordsPerPage, req.getAttribute("userField").toString(), req.getAttribute("dateField").toString());
                    break;
                }
            }

            rows = orderDAO.getNumberOfRowsFilterDateUser(req.getAttribute("dateField").toString(), req.getAttribute("userField").toString());
        }

        req.setAttribute(STATISTICS_ATTRIBUTE, orders);

        //number of pages
        int nOfPages = rows / recordsPerPage;

        if (nOfPages % recordsPerPage > 0) {

            nOfPages++;
        }


        req.setAttribute("noOfPages", nOfPages);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("recordsPerPage", recordsPerPage);

        req.getRequestDispatcher(PATH).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
