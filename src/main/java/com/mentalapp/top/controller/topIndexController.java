package com.mentalapp.top.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class topIndexController {

    @GetMapping("/")
    public String showIndex() {
        return "monitorings/index";
    }
}