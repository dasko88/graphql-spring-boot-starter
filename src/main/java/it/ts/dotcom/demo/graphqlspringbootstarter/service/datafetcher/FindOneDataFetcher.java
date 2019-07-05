package it.ts.dotcom.demo.graphqlspringbootstarter.service.datafetcher;

import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;

public class FindOneDataFetcher<T> extends BaseDataFetcher<T> implements CrudDataFetcher<T> {

	public FindOneDataFetcher(JpaRepository<T, Integer> jpaRepository) {
		super(jpaRepository, null);
	}

	/**
	 * TODO read @Id
	 *
	 * @param environment
	 * @return
	 */
	@Override
	public T get(DataFetchingEnvironment environment) {
		Integer id = environment.getArgument("id");
		return jpaRepository.findById(id).get();
	}
}
