package com.vnakhoa.crudtest.login;

import com.vnakhoa.crudtest.CrudtestApplication;
import com.vnakhoa.crudtest.user.User;
import com.vnakhoa.crudtest.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    UserRepository service;

    @Autowired
    ServletContext servletContext;

    public Model setPage(Model model, String title, String content){
        model.addAttribute("content",content);
        model.addAttribute("title",title);
        return model;
    }

    @GetMapping("/login")
    public String pageLogin(Model model) {
        model = setPage(model,"Login","login");
        return "index";
    }

    @PostMapping("/login")
    public String handleLogin(HttpSession session,@RequestParam("id") String id, @RequestParam("password") String password) {

        Optional<User> chekcID = service.findById(id);

        if (!chekcID.isPresent()) {
            session.setAttribute("status","ID don't exit");
            return "redirect:/login";
        }

        User user = chekcID.get();
        if (!user.getPassword().equals(password)) {
            session.setAttribute("status","Incorrect password");
            return "redirect:/login";
        }

        session.setAttribute("status","Login Success");
        session.setAttribute("user",user);
        return "redirect:/";
    }

    @GetMapping("/join")
    public String pageJoin(Model model) {
        model = setPage(model,"Join","join");
        return "index";
    }

    @PostMapping("/join")
    public String handleJoin(@ModelAttribute("user") User user, HttpSession ra,
                             @RequestParam("image") MultipartFile image) {
        try {
            String fileName = image.getOriginalFilename();
            String dir = "src/main/resources/static" + "/images/"+fileName;

            FileCopyUtils.copy(image.getBytes(), new File(dir));

            user.setEmoji(fileName);
            service.save(user);

            ra.setAttribute("status","Join Member Success");

        } catch (Exception e) {
            e.printStackTrace();
            ra.setAttribute("status","Join Member Fail");
        }

        return "redirect:/login";
    }

    @RequestMapping(value = "check-id/{id}", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkId(@PathVariable String id) {
        if (!service.existsById(id)) return true;
        return false;
    }


}
