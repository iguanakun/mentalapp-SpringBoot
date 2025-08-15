package com.mentalapp.work_care.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling static pages without using Thymeleaf templates.
 * This controller serves static HTML pages from the resource/work_care directory.
 */
@Controller
@RequestMapping("/work_care")
public class workCareIndexController {

    /**
     * Display the content1 page
     * @return the path to the static HTML page
     */
    @GetMapping("/content1")
    public String showContent1() {
        return "work_care/content1";
    }

    /**
     * Display the page1 page
     * @return the path to the static HTML page
     */
    @GetMapping("/page1")
    public String showPage1() {
        return "redirect:/work_care/page1.html";
    }

    /**
     * Display the page2 page
     * @return the path to the static HTML page
     */
    @GetMapping("/page2")
    public String showPage2() {
        return "redirect:/work_care/page2.html";
    }

    /**
     * Display the page3 page
     * @return the path to the static HTML page
     */
    @GetMapping("/page3")
    public String showPage3() {
        return "redirect:/work_care/page3.html";
    }

    /**
     * Display the page4 page
     * @return the path to the static HTML page
     */
    @GetMapping("/page4")
    public String showPage4() {
        return "redirect:/work_care/page4.html";
    }
}