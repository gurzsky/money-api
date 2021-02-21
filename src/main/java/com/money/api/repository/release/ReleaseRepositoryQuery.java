package com.money.api.repository.release;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.money.api.model.Release;
import com.money.api.repository.filter.ReleaseFilter;

public interface ReleaseRepositoryQuery {
	
	public Page<Release> filter(ReleaseFilter releaseFilter, Pageable pageable);

}
