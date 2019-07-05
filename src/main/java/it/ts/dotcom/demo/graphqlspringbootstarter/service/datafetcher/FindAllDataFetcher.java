package it.ts.dotcom.demo.graphqlspringbootstarter.service.datafetcher;

import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class FindAllDataFetcher<T> extends BaseDataFetcher<T> implements CrudDataFetcher<List<T>> {

	public FindAllDataFetcher(JpaRepository<T, Integer> jpaRepository) {
		super(jpaRepository, null);
	}

	@Override
	public List<T> get(DataFetchingEnvironment environment) {
		return jpaRepository.findAll();
	}
}
