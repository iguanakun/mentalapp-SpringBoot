package com.mentalapp.common.security;

// このクラスはアプリケーションのセキュリティ設定を管理します
// Spring Securityを使用して認証と認可の機能を提供します

import com.mentalapp.common.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    // パスワードエンコーダーのBean定義
    // BCryptアルゴリズムを使用してパスワードをハッシュ化します
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 認証プロバイダーのBean定義
    // ユーザー情報の取得方法とパスワード検証方法を設定します
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService); // カスタムユーザー詳細サービスを設定
        auth.setPasswordEncoder(passwordEncoder()); // パスワードエンコーダー（BCrypt）を設定
        return auth;
    }

    // セキュリティフィルターチェーンの設定
    // URLごとのアクセス権限、ログインページ、ログアウト処理などを定義します
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationSuccessHandler customAuthenticationSuccessHandler) throws Exception {

        http.authorizeHttpRequests(configurer ->
                        configurer
                                //TODO ROLEの実装
//                                .requestMatchers("/").hasRole("EMPLOYEE")
//                                .requestMatchers("/leaders/**").hasRole("MANAGER")
//                                .requestMatchers("/systems/**").hasRole("ADMIN")
                                .requestMatchers("/register/**").permitAll()
                                .requestMatchers("/").permitAll() // トップページは認証不要
                                .requestMatchers("/images/**", "/css/**", "/js/**").permitAll() // 静的リソースへのアクセスを許可
                                .anyRequest().authenticated() //  他のURLはログイン後アクセス可能
                )
                .formLogin(form ->
                        form
                                .loginPage("/showMyLoginPage")
                                .loginProcessingUrl("/authenticateTheUser")
                                .successHandler(customAuthenticationSuccessHandler)
                                .permitAll()
                )
                .logout(logout -> logout.permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                );

        return http.build();
    }

}