package com.money.api.repository.release;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.money.api.model.Release;
import com.money.api.repository.filter.ReleaseFilter;
import com.money.api.repository.projection.ReleaseResume;

public interface ReleaseRepositoryQuery {
	
	public Page<Release> filter(ReleaseFilter releaseFilter, Pageable pageable);
	
	public Page<ReleaseResume> resume(ReleaseFilter releaseFilter, Pageable pageable);

}
