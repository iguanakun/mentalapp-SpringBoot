package com.mentalapp.top.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * トップページ表示のコントローラークラス
 */
@Controller
public class topIndexController {

    /**
     * トップページを表示
     * @return トップページのビュー名
     */
    @GetMapping("/")
    public String showIndex() {
        return "monitorings/index";
    }
}