package com.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.money.api.model.Release;

public interface ReleaseRepository extends JpaRepository<Release, Long>{

}
