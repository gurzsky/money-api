package com.money.api.repository.release;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.money.api.model.Category_;
import com.money.api.model.Person_;
import com.money.api.model.Release;
import com.money.api.model.ReleaseType;
import com.money.api.model.Release_;
import com.money.api.repository.filter.ReleaseFilter;
import com.money.api.repository.projection.ReleaseResume;

public class ReleaseRepositoryImpl implements ReleaseRepositoryQuery {
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Release> filter(ReleaseFilter releaseFilter, Pageable pageable) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Release> criteria = builder.createQuery(Release.class);
		
		Root<Release> root = criteria.from(Release.class);
		
		// criar as restrições
		Predicate[] predicates = createRestrictions(releaseFilter, builder, root);
		criteria.where(predicates);
		
		TypedQuery<Release> query = manager.createQuery(criteria);
		
		addRestrictionsPage(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(releaseFilter));
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
	
	private void addRestrictionsPage(TypedQuery<?> query, Pageable pageable) {
		int actualPage = pageable.getPageNumber();
		int totalRecordsPerPage = pageable.getPageSize();
		int firstRecordPerPage = actualPage * totalRecordsPerPage;
		
		query.setFirstResult(firstRecordPerPage);
		query.setMaxResults(totalRecordsPerPage);
	}
	
	private Long total(ReleaseFilter releaseFilter) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Release> root = criteria.from(Release.class);
		
		Predicate[] predicates = createRestrictions(releaseFilter, builder, root);
		criteria.where(predicates);
		criteria.select(builder.count(root));
		
		return manager.createQuery(criteria).getSingleResult();
	}

	@Override
	public Page<ReleaseResume> resume(ReleaseFilter releaseFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ReleaseResume> criteria = builder.createQuery(ReleaseResume.class);
		Root<Release> root = criteria.from(Release.class);
		
		criteria.select(builder.construct(ReleaseResume.class
				, root.get(Release_.codigo)
				, root.get(Release_.descricao)
				, root.get(Release_.dataVencimento)
				, root.get(Release_.dataPagamento)
				, root.get(Release_.valor)
				, root.get(Release_.tipo)
				, root.get(Release_.categoria).get(Category_.nome)
				, root.get(Release_.pessoa).get(Person_.nome)));
				
		// criar as restrições
		Predicate[] predicates = createRestrictions(releaseFilter, builder, root);
		criteria.where(predicates);
				
		TypedQuery<ReleaseResume> query = manager.createQuery(criteria);
				
		addRestrictionsPage(query, pageable);
				
		return new PageImpl<>(query.getResultList(), pageable, total(releaseFilter));
	}
}
