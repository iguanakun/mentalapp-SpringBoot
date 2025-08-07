package com.mentalapp.common.service;

//import com.demosecurity.common.dao.RoleDao;
import com.mentalapp.common.entity.Role;
import com.mentalapp.common.entity.User;
import com.mentalapp.common.mapper.UserMapper;
import com.mentalapp.common.user.WebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
public class UserService02Impl implements UserService02 {

	private final UserMapper userMapper;

//	private RoleDao roleDao;

	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public UserService02Impl(UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
		this.userMapper = userMapper;
//		this.roleDao = roleDao;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public User findByUserName(String userName) {
		// check the database if the user already exists
		return userMapper.selectByPrimaryKey(userName);
	}

	@Override
	public void save(WebUser webUser) {
		User user = new User();

		// assign user details to the user object
		user.setUserName(webUser.getUserName());
		user.setPassword(passwordEncoder.encode(webUser.getPassword()));
		user.setFirstName(webUser.getFirstName());
		user.setLastName(webUser.getLastName());
		user.setEmail(webUser.getEmail());
		user.setEnabled(true);

		// give user default role of "employee"
		// user.setRoles(Arrays.asList(roleDao.findRoleByName("ROLE_EMPLOYEE")));

		// save user in the database
		userMapper.insert(user);
	}

	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userMapper.selectByPrimaryKey(userName);

		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}

		// TODO ROLEの実装要否検討
//		Collection<SimpleGrantedAuthority> authorities = mapRolesToAuthorities(user.getRoles());

//		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
//				authorities);
		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
				new ArrayList<>());

	}

	private Collection<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (Role tempRole : roles) {
			SimpleGrantedAuthority tempAuthority = new SimpleGrantedAuthority(tempRole.getName());
			authorities.add(tempAuthority);
		}

		return authorities;
	}
}
