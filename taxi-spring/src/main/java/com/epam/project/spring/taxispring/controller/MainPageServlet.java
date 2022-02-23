package com.epam.project.spring.taxispring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/")
@Controller
public class MainPageServlet {

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    protected String doGet() {
        return "index";
    }

}