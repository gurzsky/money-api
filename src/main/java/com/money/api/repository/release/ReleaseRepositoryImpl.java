package com.money.api.repository.release;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.money.api.model.Release;
import com.money.api.model.Release_;
import com.money.api.repository.filter.ReleaseFilter;

public class ReleaseRepositoryImpl implements ReleaseRepositoryQuery {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Release> filter(ReleaseFilter releaseFilter) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Release> criteria = builder.createQuery(Release.class);
		
		Root<Release> root = criteria.from(Release.class);
		
		// criar as restrições
		Predicate[] predicates = createRestrictions(releaseFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Release> query = manager.createQuery(criteria);		
		return query.getResultList();
	}

	private Predicate[] createRestrictions(ReleaseFilter releaseFilter, CriteriaBuilder builder, Root<Release> root) {
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if (!StringUtils.isEmpty(releaseFilter.getDescricao())) {
			predicates.add(
					builder.like(
							builder.lower(root.get(Release_.descricao)), 
							"%" + releaseFilter.getDescricao().toLowerCase() + "%")
					);
		}
		
		if (releaseFilter.getDataVencimentoDe() != null) {
			predicates.add(
					builder.greaterThanOrEqualTo(
							root.get(Release_.dataVencimento),
							releaseFilter.getDataVencimentoDe())
					);
		}
		
		if (releaseFilter.getDataVencimentoAte() != null) {
			predicates.add(
					builder.lessThanOrEqualTo(
							root.get(Release_.dataVencimento),
							releaseFilter.getDataVencimentoAte())
					);
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
}
