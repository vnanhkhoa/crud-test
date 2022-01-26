package com.vnakhoa.crudtest;

import com.vnakhoa.crudtest.user.User;
import com.vnakhoa.crudtest.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    UserRepository service;

    @GetMapping("")
    public String index(Model model, HttpSession session) {

        List<User> users = (List<User>) service.findAll();

        model.addAttribute("content","home");
        model.addAttribute("title","Home");
        model.addAttribute("users",users);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/login";
    }
}
