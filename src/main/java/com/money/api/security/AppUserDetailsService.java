package com.money.api.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.money.api.model.Users;
import com.money.api.repository.UsersRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsersRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Optional<Users> userOptional = userRepository.findByEmail(email);
		Users user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User and/or password incorrect"));
		return new UserLoggin(user, getPermissions(user));
	}

	private Collection<? extends GrantedAuthority> getPermissions(Users user) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getPermissoes()
			.forEach(
					p -> authorities.add(new SimpleGrantedAuthority(p.getDescricao().toUpperCase())));
		return authorities;
	}
}
