package com.example.hw04s_att.controllers;

//import com.example.ls12s.models.Person;

import com.example.hw04s_att.models.Person;
import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AuthenticationController {
    
    @GetMapping("/authentication")
    public String login(@ModelAttribute("person") Person person) {
        return "authentication";
    }
    
    @PostMapping("/authentication")
    public String logout(@ModelAttribute("person") Person person) {
        return "redirect:/person_account";
    }
    
}
