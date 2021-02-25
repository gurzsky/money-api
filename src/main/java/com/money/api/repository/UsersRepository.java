package com.money.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.money.api.model.Users;

public interface UsersRepository extends JpaRepository<Users, Long>{

	public Optional<Users> findByEmail(String email);
}
