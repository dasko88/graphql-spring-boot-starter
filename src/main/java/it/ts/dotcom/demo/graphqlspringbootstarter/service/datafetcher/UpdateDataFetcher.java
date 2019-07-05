package it.ts.dotcom.demo.graphqlspringbootstarter.service.datafetcher;

import graphql.schema.DataFetchingEnvironment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public class UpdateDataFetcher<T> extends BaseDataFetcher<T> implements CrudDataFetcher<T> {

	public UpdateDataFetcher(JpaRepository<T, Integer> jpaRepository, Class<T> clazz) {
		super(jpaRepository, clazz);
	}

	@Override
	public T get(DataFetchingEnvironment environment) {
		Field entityIdField = getAccessibleEntityIdField(clazz);
		Integer id = environment.getArgument(entityIdField.getName());
		T object = jpaRepository.findById(id).get();
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
	}

	private Field getAccessibleEntityIdField(Class<T> clazz) {
		Optional<Field> optionalField = Arrays.asList(clazz.getDeclaredFields()).stream()
				.filter(field -> field.getAnnotation(Id.class) != null)
				.findFirst();
		if (optionalField.isPresent()) {
			Field field = optionalField.get();
			field.setAccessible(true);
			return field;
		} else {
			throw new RuntimeException("Unable to find @Id field for class " + clazz.getName());
		}
	}
}
