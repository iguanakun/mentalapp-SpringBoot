package com.mentalapp.staticPages.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling static pages without using Thymeleaf templates.
 * This controller serves static HTML pages from the resource/view directory.
 */
@Controller
@RequestMapping("/static-pages")
public class StaticPagesController {

    /**
     * Display the content1 page
     * @return the path to the static HTML page
     */
    @GetMapping("/work-care/content1")
    public String showContent1() {
        return "forward:/view/work-care/content1.html";
    }

    /**
     * Display the page1 page
     * @return the path to the static HTML page
     */
    @GetMapping("/work-care/page1")
    public String showPage1() {
        return "forward:/view/work-care/page1.html";
    }

    /**
     * Display the page2 page
     * @return the path to the static HTML page
     */
    @GetMapping("/work-care/page2")
    public String showPage2() {
        return "forward:/view/work-care/page2.html";
    }

    /**
     * Display the page3 page
     * @return the path to the static HTML page
     */
    @GetMapping("/work-care/page3")
    public String showPage3() {
        return "forward:/view/work-care/page3.html";
    }

    /**
     * Display the page4 page
     * @return the path to the static HTML page
     */
    @GetMapping("/work-care/page4")
    public String showPage4() {
        return "forward:/view/work-care/page4.html";
    }
}