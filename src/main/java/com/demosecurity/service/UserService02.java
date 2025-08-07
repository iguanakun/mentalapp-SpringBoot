package com.demosecurity.service;

import com.demosecurity.entity.User;
import com.demosecurity.user.WebUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService02 extends UserDetailsService {

	public User findByUserName(String userName);

	void save(WebUser webUser);

}
