package it.ts.dotcom.demo.graphqlspringbootstarter.service.datafetcher;

import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class CreateDataFetcher<T> extends BaseDataFetcher<T> implements CrudDataFetcher<T> {

	public CreateDataFetcher(JpaRepository<T, Integer> jpaRepository, Class<T> clazz) {
		super(jpaRepository, clazz);
	}

	@Override
	public T get(DataFetchingEnvironment environment) {
		try {
			Constructor<T> constructor = clazz.getConstructor();
			T object = constructor.newInstance();
			Arrays.asList(clazz.getDeclaredFields()).forEach(field -> {
				field.setAccessible(true);
				String fieldName = field.getName();
				Object value = environment.getArgument(fieldName);
				try {
					field.set(object, value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			});
			return jpaRepository.save(object);
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException("Error on deserialize entity", e);
		}
	}
}
