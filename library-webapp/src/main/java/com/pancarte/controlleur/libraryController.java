package com.pancarte.controlleur;



import com.pancarte.Model.Book_List;
import com.pancarte.proxy.MicroserviceLibraryProxy;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class libraryController {




    @Autowired
    private MicroserviceLibraryProxy Library;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public ModelAndView in() {
        ModelAndView model = new ModelAndView();

        model.setViewName("index");
        return model;}

    @RequestMapping(value = {"/index"}, method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView model = new ModelAndView();
        System.out.println("GGG");


        System.out.println( Library.test());
        List<Book_List>  book;
   book= Library.searchAvailableBooks("des");



        //System.out.println(book);
        System.out.println("ddd");
        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView model = new ModelAndView();
        model.addObject("view", "login");
        model.addObject("userName", "0");
        //model.setViewName("user/login");
        model.setViewName("index");
        return model;
    }
}
