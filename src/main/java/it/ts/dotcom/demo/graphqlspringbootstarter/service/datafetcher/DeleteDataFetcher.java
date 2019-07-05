package it.ts.dotcom.demo.graphqlspringbootstarter.service.datafetcher;

import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;

public class DeleteDataFetcher<T> extends BaseDataFetcher<T> implements CrudDataFetcher<T> {

	public DeleteDataFetcher(JpaRepository<T, Integer> jpaRepository, Class<T> clazz) {
		super(jpaRepository, clazz);
	}

	/**
	 *
	 * @param environment
	 * @return
	 */
	@Override
	public T get(DataFetchingEnvironment environment) {
		throw new UnsupportedOperationException();
	}
}
