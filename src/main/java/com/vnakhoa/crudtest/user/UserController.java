package com.vnakhoa.crudtest.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    UserRepository service;

    public Model setPage(Model model, String title, String content){
        model.addAttribute("content",content);
        model.addAttribute("title",title);
        return model;
    }

    @GetMapping("/user/{id}")
    public String showUser(@PathVariable("id") String id, Model model, HttpSession session) {

        Optional<User> check = service.findById(id);
        if (!check.isPresent()) {
            session.setAttribute("status","User don't exit");
            return "redirect:/";
        }
        User user = check.get();
        User current = (User) session.getAttribute("user");
        if (current != null && user.getId().equals(current.getId())) {
            model.addAttribute("isUser",true);
        }
        model.addAttribute("user",user);
        model = setPage(model,"User | "+user.getName(),"user");
        return "index";
    }

    @GetMapping("/edit-user/{id}")
    public String editUser(@PathVariable("id") String id, Model model, HttpSession session) {

        Optional<User> check = service.findById(id);
        if (!check.isPresent()) {
            session.setAttribute("status","User don't exit");
            return "redirect:/";
        }
        User user = check.get();

        model.addAttribute("user",user);
        model = setPage(model,"User | "+user.getName(),"edit-user");
        return "index";
    }

    @PostMapping("/edit-user")
    public String handleJoin(@ModelAttribute("user") User user, HttpSession ra,
                             @RequestParam("image") MultipartFile image) {
        try {
            System.out.print(user.toString());
            Optional<User> check = service.findById(user.getId());
            if (!check.isPresent()) {
                ra.setAttribute("status","User don't exit");
                return "redirect:/";
            }
            User userExit = check.get();
            User userNew = service.save(user);
            if (!image.isEmpty()) {
                String fileName = user.getEmoji();
                String dir = "src/main/resources/static/uploads";

                Path path = Paths.get(dir);

                InputStream inputStream = image.getInputStream();
                Path fileUpload = path.resolve(fileName);
                Files.copy(inputStream,fileUpload, StandardCopyOption.REPLACE_EXISTING);
            }

            ra.setAttribute("status","Edit User Success");

        } catch (Exception e) {
            e.printStackTrace();
            ra.setAttribute("status","Edit User Fail");
        }

        return "redirect:/user/"+user.getId();
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable("id") String id, Model model, HttpSession session) throws IOException {

        User user = (User) session.getAttribute("user");
        if (!user.getId().equals(id)) {
            session.setAttribute("status","Delete User Fail");
            return "redirect:/";
        }

        service.deleteById(id);

        Path path = Paths.get("src/main/resources/static/uploads/"+user.getEmoji());
        Files.delete(path);
        session.setAttribute("status", "Delete User Successfully. Please Create A New Account");
        session.removeAttribute("user");
        return "redirect:/";
    }

}
