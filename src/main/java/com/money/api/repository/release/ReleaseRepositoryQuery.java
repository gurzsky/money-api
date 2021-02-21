package com.money.api.repository.release;

import java.util.List;

import com.money.api.model.Release;
import com.money.api.repository.filter.ReleaseFilter;

public interface ReleaseRepositoryQuery {
	
	public List<Release> filter(ReleaseFilter releaseFilter);

}
