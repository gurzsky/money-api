package com.money.api.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.money.api.model.Users;

public class UserLoggin extends User {
	
	private static final long serialVersionUID = 1L;
	
	private Users users;

	public UserLoggin(Users users, Collection<? extends GrantedAuthority> permissions) {
		super(users.getEmail(), users.getSenha(), permissions);
		this.users = users;
	}

	public Users getUsers() {
		return users;
	}
}
