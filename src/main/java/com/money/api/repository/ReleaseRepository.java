package com.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.money.api.model.Release;
import com.money.api.repository.release.ReleaseRepositoryQuery;

public interface ReleaseRepository extends JpaRepository<Release, Long>, ReleaseRepositoryQuery {

}
