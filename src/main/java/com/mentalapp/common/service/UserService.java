package com.mentalapp.common.service;

import com.mentalapp.common.entity.User;
import com.mentalapp.common.user.WebUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

	public User findByUserName(String userName);

	void save(WebUser webUser);

}
