package com.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.money.api.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
