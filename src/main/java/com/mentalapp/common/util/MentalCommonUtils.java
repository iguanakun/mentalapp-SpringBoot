package com.mentalapp.common.util;

import com.mentalapp.common.entity.User;
import com.mentalapp.common.dao.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MentalCommonUtils {

    public static final String REDIRECT_TOP_PAGE = "redirect:/";

    private final UserMapper userMapper;

    /**
     * ログイン中のユーザー情報を取得
     * @return ログイン中のユーザーエンティティ
     */
    public User getUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userMapper.selectByPrimaryKey(userName);
    }
    
    /**
     * アクセス権チェック
     * @param userId エンティティのユーザーID
     * @return アクセス権がある場合はtrue
     */
    public boolean isAuthorized(Long userId) {
        return userId.equals(getUser().getId());
    }
}
