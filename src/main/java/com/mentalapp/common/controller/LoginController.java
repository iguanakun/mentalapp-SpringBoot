package com.mentalapp.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ログイン関連の処理を行うコントローラークラス
 */
@Controller
public class LoginController {

    /**
     * ログインページを表示
     * @return ログインページのビュー名
     */
    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage() {
        return "fancy-login";
    }
    
    /**
     * Rubyスタイルのログインページを表示
     * @return Rubyスタイルのログインページのビュー名
     */
    @GetMapping("/showRubyStyleLoginPage")
    public String showRubyStyleLoginPage() {
        return "ruby-style-login";
    }

    /**
     * アクセス拒否ページを表示
     * @return アクセス拒否ページのビュー名
     */
    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }
}










