package com.money.api.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Users.class)
public abstract class Users_ {

	public static volatile SingularAttribute<Users, String> senha;
	public static volatile ListAttribute<Users, Permission> permissoes;
	public static volatile SingularAttribute<Users, Long> codigo;
	public static volatile SingularAttribute<Users, String> nome;
	public static volatile SingularAttribute<Users, String> email;

}

