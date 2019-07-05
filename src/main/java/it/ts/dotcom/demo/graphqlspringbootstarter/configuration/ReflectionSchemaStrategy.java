package it.ts.dotcom.demo.graphqlspringbootstarter.configuration;

import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import it.ts.dotcom.demo.graphqlspringbootstarter.service.ConsumesRepository;
import it.ts.dotcom.demo.graphqlspringbootstarter.service.datafetcher.CreateDataFetcher;
import it.ts.dotcom.demo.graphqlspringbootstarter.service.datafetcher.FindAllDataFetcher;
import it.ts.dotcom.demo.graphqlspringbootstarter.service.datafetcher.FindOneDataFetcher;
import it.ts.dotcom.demo.graphqlspringbootstarter.service.datafetcher.UpdateDataFetcher;
import it.ts.dotcom.demo.graphqlspringbootstarter.service.schema.SchemaHelper;
import it.ts.dotcom.demo.graphqlspringbootstarter.service.schema.TypeDefinitionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

public class ReflectionSchemaStrategy implements GraphQLSchemaStrategy {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private TypeDefinitionBuilder typeDefinitionBuilder;

	@Override
	public GraphQLSchema create() {
		SchemaParser schemaParser = new SchemaParser();
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
		Iterator<EntityType<?>> iterator = entities.iterator();
		String schemaInput = typeDefinitionBuilder.createSchema(entities);
		TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaInput);
		Map<String, DataFetcher> dataFetcherQueryMap = new HashMap<>();
		Map<String, DataFetcher> dataFetcherMutationMap = new HashMap<>();
		while (iterator.hasNext()) {
			EntityType<?> entityType = iterator.next();
			Class repositoryClass = entityType.getJavaType().getAnnotation(ConsumesRepository.class).value();
			JpaRepository jpaRepository = (JpaRepository) applicationContext.getBean(repositoryClass);
			String entityName = entityType.getName();
			String allEntityName = SchemaHelper.applyTrasformation(entityName, SchemaHelper.allEntityTrasformation());
			dataFetcherQueryMap.put(allEntityName, new FindAllDataFetcher(jpaRepository));
			String getEntityName = SchemaHelper.applyTrasformation(entityName, SchemaHelper.getEntityTrasformation());
			dataFetcherQueryMap.put(getEntityName, new FindOneDataFetcher(jpaRepository));
			String newEntityName = SchemaHelper.applyTrasformation(entityName, SchemaHelper.newEntityTrasformation());
			dataFetcherMutationMap.put(newEntityName, new CreateDataFetcher(jpaRepository, entityType.getJavaType()));
			String updateEntityName = SchemaHelper.applyTrasformation(entityName, SchemaHelper.updateEntityTrasformation());
			dataFetcherMutationMap.put(updateEntityName, new UpdateDataFetcher(jpaRepository, entityType.getJavaType()));
		}
		RuntimeWiring runtimeWiring = newRuntimeWiring()
				.type("Query", builder -> builder.dataFetchers(dataFetcherQueryMap))
				.type("Mutation", builder -> builder.dataFetchers(dataFetcherMutationMap))
				.build();
		return new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
	}
}
