package com.mentalapp.common.util;

import com.mentalapp.common.entity.User;
import com.mentalapp.common.mapper.UserMapper;
import com.mentalapp.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MentalCommonObject {
    @Autowired
    private UserMapper userMapper;

    // ログイン中のユーザ情報を取得
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userMapper.selectByPrimaryKey(userName);
    }
}
