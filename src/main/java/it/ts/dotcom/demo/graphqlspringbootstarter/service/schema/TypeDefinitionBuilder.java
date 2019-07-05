package it.ts.dotcom.demo.graphqlspringbootstarter.service.schema;

import it.ts.dotcom.demo.graphqlspringbootstarter.service.schema.parser.GraphQLSchemaParser;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.metamodel.EntityType;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class TypeDefinitionBuilder {

	@Autowired
	private GraphQLSchemaParser graphQLSchemaParser;

	public String createSchema(Set<EntityType<?>> entities) {
		List<String> schemaFragments = new ArrayList<>();
		List<GraphQLType> graphQLTypeList = new ArrayList<>();
		Iterator<EntityType<?>> iterator = entities.iterator();
		while (iterator.hasNext()) {
			EntityType<?> entityType = iterator.next();
			Field[] declaredFields = entityType.getJavaType().getDeclaredFields();
			List<GraphQLField> graphQLFieldList = new ArrayList<>();
			Arrays.asList(declaredFields).stream().forEach(field -> {
				graphQLFieldList.add(new GraphQLField(field.getName(), field.getType()));
			});
			graphQLTypeList.add(new GraphQLType(entityType.getName(), graphQLFieldList));
		}
		schemaFragments.add(graphQLSchemaParser.parseGraphQLQueryList(graphQLTypeList));
		schemaFragments.add(graphQLSchemaParser.parseGraphQLMutationList(graphQLTypeList));
		schemaFragments.add(graphQLSchemaParser.parseGraphQLTypeList(graphQLTypeList));
		return schemaFragments.stream().collect(Collectors.joining("\n\n"));
	}
}
