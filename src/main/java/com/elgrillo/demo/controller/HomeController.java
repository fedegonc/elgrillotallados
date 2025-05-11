package com.elgrillo.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "El Grillo Tallados");
        model.addAttribute("message", "Bienvenido a nuestra tienda de tallados artesanales");
        return "index";
    }
}
