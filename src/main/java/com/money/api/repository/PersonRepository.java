package com.money.api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.money.api.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

	public Page<Person> findByNomeContaining(String nome, Pageable pageable);
	
}
