package it.ts.dotcom.demo.graphqlspringbootstarter.service.datafetcher;

import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseDataFetcher<T> {

	protected JpaRepository<T, Integer> jpaRepository;
	protected Class<T> clazz;

	public BaseDataFetcher(JpaRepository<T, Integer> jpaRepository, Class<T> clazz) {
		this.jpaRepository = jpaRepository;
		this.clazz = clazz;
	}
}
