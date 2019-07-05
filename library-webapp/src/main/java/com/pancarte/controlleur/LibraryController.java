package com.pancarte.controlleur;

import com.pancarte.Model.Book_List;
import com.pancarte.Model.Borrow;
import com.pancarte.Model.User;
import com.pancarte.proxy.MicroserviceLibraryProxy;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LibraryController {




    //todo:emprunt livre
    //
    // todo:+ ralonge
    //todo: loggedhomme

    public String getRole() {
        return role;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Autowired
    private MicroserviceLibraryProxy Library;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public String user = "";
    public String role = "";
   public String password = "";
   public String email = "";
    public String RoleSQL( ){
        if(!this.email.equals("")){
            return Library.queryRole(this.email);
        }
       return "";

    }
    public String UserSQL(){

        if(!this.email.equals("")){
            return Library.queryUser(this.email);
        }
        return "";
    }

    @RequestMapping(value = {"/content"}, method = RequestMethod.GET)
    public ModelAndView content(@RequestParam("idtopo") int idtopo) {
        ModelAndView model = new ModelAndView();
        User user = new User();
        List<Book_List> book = new ArrayList<>();
        List<Borrow> borrow = new ArrayList<>();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (!auth.getName().equals("anonymousUser")) {
            model.addObject("userName", user.getName() + " " + user.getLastName());
        } else {
            model.addObject("userName", "0");
        }
        model.addObject("borrow", borrow);
        model.addObject("user", user);
        model.addObject("book", book);
        model.setViewName("fragment/content");

        return model;
    }
    @RequestMapping(value = {"/header"}, method = RequestMethod.GET)
    public ModelAndView header() {
        ModelAndView model = new ModelAndView();
        model.addObject("userName", "0");


        model.setViewName("fragment/header");
        return model;
    }

    //@Scheduled(fixedRate = 5000)
    @Scheduled(cron = "0/20 * * * * ?")
    public void availableBook2() {
        System.out.println("Yes REUSSI" );
    }
    @RequestMapping(value = {"/index","/"}, method = RequestMethod.GET)
    public ModelAndView index() {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = Library.findUserByEmail(auth.getName());
        if (!auth.getName().equals("anonymousUser")) {
            model.addObject("userName", user.getName() + " " + user.getLastName());
        } else {
            model.addObject("userName", "0");
        }

        System.out.println(Library.test());
        List<Book_List> book;
        book = Library.getAllBooks();

        model.addObject("view", "home");
        model.addObject("book", book);


        model.setViewName("index");
        return model;
    }
    @RequestMapping(value = {"/search"}, method = RequestMethod.POST)
    public String search(String search) {
        System.out.println(search);

        return "redirect:/search/?search=" + search;
    }
    @RequestMapping(value = {"/search"}, method = RequestMethod.GET)
    public ModelAndView searchIt(@RequestParam("search") String search) {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = Library.findUserByEmail(auth.getName());
        if (!auth.getName().equals("anonymousUser")) {
            model.addObject("userName", user.getName() + " " + user.getLastName());
        } else {
            model.addObject("userName", "0");
        }

        String cap = search.substring(0, 1).toUpperCase() + search.substring(1);
        String caps = search.toUpperCase();

        //User user = userService.findUserByEmail(auth.getName());
        List<Book_List> book= Library.searchBooks(search);
        book.addAll(Library.searchBooks(cap));
        book.addAll(Library.searchBooks(caps));

/*
        topos.addAll(topoService.findByLieu(cap));
        spots.addAll(spotService.findByName(cap));
        for (Book_List books : book) {
            for (Book_List books2 : book) {
                if ((book.toArray().toString()).contains(Integer.toString(books2.getIdBook()))) {
                    topos.addAll(topoService.findById(spot.getIdtopo()));
                }
            }
        }

       // if (!auth.getName().equals("anonymousUser")) {
        //    model.addObject("userName", user.getName() + " " + user.getLastname());
       // } else {
       //     model.addObject("userName", "0");
       // }
        spots = spotService.findAllSpot();
*/
        model.addObject("book", book);

        model.addObject("search", search);

        model.addObject("view", "search");
        //model.setViewName("home/home");
        model.setViewName("index");

        return model;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = Library.findUserByEmail(auth.getName());
        if (!auth.getName().equals("anonymousUser")) {
            model.addObject("userName", user.getName() + " " + user.getLastName());
        } else {
            model.addObject("userName", "0");
        }
        User userv = new User();
        model.addObject("view", "login");
        System.out.println("xxxxxxxxxxxxxxx");
        model.addObject("userName", "0");
        model.addObject("user", userv);
        //model.setViewName("user/login");

        model.setViewName("index");
        return model;
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.POST)
    public String log(@RequestParam("email") String email,@RequestParam("password") String password) {


        System.out.println(" user__u="+Library.queryUser("u"));
        System.out.println(" role_____u"+Library.queryRole("u"));
        System.out.println(" user__email="+Library.queryUser(email));
        this.password=password;
        this.email=email;
        this.user = Library.queryUser(email);
        this.role = Library.queryRole(email);

        return "redirect:index";
    }
    @RequestMapping(value = {"/signup"}, method = RequestMethod.GET)
    public ModelAndView signup() {
        ModelAndView model = new ModelAndView();
        User user = new User();
        model.addObject("user", user);
        // model.setViewName("user/signup");
        model.addObject("view", "signup");
        model.setViewName("index");

        model.addObject("userName", "0");

        return model;
    }

    @RequestMapping(value = {"/signup"}, method = RequestMethod.POST)
    public ModelAndView createUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView model = new ModelAndView();
        User userExists = Library.findUserByEmail(user.getEmail());
        System.out.println(user.getLastName()+user.getName()+user.getEmail()+user.getPassword());
        if (userExists != null) {
            bindingResult.rejectValue("email", "error.user", "L'email existe déja");
        }
        if (bindingResult.hasErrors()) {
            // model.setViewName("user/signup");
            model.addObject("view", "signup");
            model.setViewName("index");
        } else {
            //bCryptPasswordEncoder.encode(user.getPassword());
            Library.createUser(user.getLastName(),user.getName(),user.getEmail(),bCryptPasswordEncoder.encode(user.getPassword()));
            model.addObject("msg", "L'utilisateur à été enregistré");
            model.addObject("user", new User());

            model.addObject("userName", "0");

            //model.setViewName("user/signup");
            model.addObject("view", "login");
            model.setViewName("index");

        }

        return model;
    }


    @RequestMapping(value = {"/loggedhome"}, method = RequestMethod.GET)
    public ModelAndView loggedHome() {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = Library.findUserByEmail(auth.getName());
        List<Book_List> book = Library.getAllBooks();
        List<Borrow> borrow = Library.getborrowedBook(user.getId());
        if (!auth.getName().equals("anonymousUser")) {
            model.addObject("userName", user.getName() + " " + user.getLastName());
        } else {
            model.addObject("userName", "0");
        }
        model.addObject("borrow", borrow);
        model.addObject("user", user);
        model.addObject("book", book);

        model.addObject("view", "loggedhome");
       //todo:afficher les emprunts


        model.setViewName("index");
    return model;
    }
    @RequestMapping(value = {"/borrow"}, method = RequestMethod.POST)
    public String  borrow(@RequestParam("idbook") int idBook) {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = Library.findUserByEmail(auth.getName());
        Library.borrow(idBook,user.getId());

        return "redirect:/loggedhome";

    }
 @RequestMapping(value = {"/borrowed"}, method = RequestMethod.POST)
    public String borrowed(@RequestParam("idborrow") int idBorrow) {
        ModelAndView model = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Library.extendBorrow(idBorrow);
     return "redirect:/loggedhome";

    }

}