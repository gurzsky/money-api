package com.money.api.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Release.class)
public abstract class Release_ {

	public static volatile SingularAttribute<Release, Long> codigo;
	public static volatile SingularAttribute<Release, String> observacao;
	public static volatile SingularAttribute<Release, ReleaseType> tipo;
	public static volatile SingularAttribute<Release, LocalDate> dataPagamento;
	public static volatile SingularAttribute<Release, Person> pessoa;
	public static volatile SingularAttribute<Release, LocalDate> dataVencimento;
	public static volatile SingularAttribute<Release, Category> categoria;
	public static volatile SingularAttribute<Release, BigDecimal> valor;
	public static volatile SingularAttribute<Release, String> descricao;

}

