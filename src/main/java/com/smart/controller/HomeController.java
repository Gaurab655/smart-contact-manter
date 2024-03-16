package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.Message;
import com.smart.entities.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    //handler for registering user
    @PostMapping("/do_register")
    public String registerUser(@ModelAttribute("user") User user, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model, HttpSession session) {
        try {
            if (!agreement) {
                System.out.println("Please agree to terms and conditions");
                throw new Exception("Please agree to terms and conditions");
            }
            user.setRole("ROLE_USER");
            user.setEnable(true);
            user.setImageUrl("default.png");
            System.out.println("Agreement: " + agreement);
            System.out.println("User: " + user);
            User result = this.userRepository.save(user);
            model.addAttribute("user", new User());
            model.addAttribute("message", new Message("Successfully registered", "alert-success"));
            return "signup";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            model.addAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
//            session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
            return "signup";
        }

    }


}
