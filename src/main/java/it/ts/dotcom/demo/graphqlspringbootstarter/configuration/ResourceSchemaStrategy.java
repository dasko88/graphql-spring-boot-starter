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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

@Component("resourceSchemaStrategy")
public class ResourceSchemaStrategy implements GraphQLSchemaStrategy {

	@Autowired
	private EntityManager entityManager;
	@Autowired
	private ApplicationContext applicationContext;
	private String schemaInput;

	/**
	 * TODO: create schema from Entities
	 */
	@PostConstruct
	public void init() {
		schemaInput = "type Query {\n" +
				"    getUser(id: Int!): User\n" +
				"    allUsers: [User]\n" +
				"    getTask(id: Int!): Task\n" +
				"    allTasks: [Task]\n" +
				"}\n" +
				"\n" +
				"type Mutation {\n" +
				"    newUser(name: String!, surname: String, height:Int!): User!\n" +
				"    newTask(name: String!, code: String!, priority:Int): Task!\n" +
				"    updateUser(id: Int!, name: String!, surname: String, height:Int!): User!\n" +
				"}\n" +
				"\n" +
				"type User {\n" +
				"    id: Int!\n" +
				"    name: String!\n" +
				"    surname: String\n" +
				"    height: Int!\n" +
				"}\n" +
				"\n" +
				"type Task {\n" +
				"    id: Int!\n" +
				"    name: String!\n" +
				"    code: String!\n" +
				"    priority: Int\n" +
				"}";
	}

	@Override
	public GraphQLSchema create() {
		SchemaParser schemaParser = new SchemaParser();
		// TODO: concat #schemaInput with additional queries and mutations
		TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schemaInput);
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
		Iterator<EntityType<?>> iterator = entities.iterator();
		Map<String, DataFetcher> dataFetcherQueryMap = new HashMap<>();
		Map<String, DataFetcher> dataFetcherMutationMap = new HashMap<>();
		while (iterator.hasNext()) {
			EntityType<?> entityType = iterator.next();
			Class repositoryClass = entityType.getJavaType().getAnnotation(ConsumesRepository.class).value();
			JpaRepository jpaRepository = (JpaRepository) applicationContext.getBean(repositoryClass);
			String entityName = entityType.getName();
			String allEntityName = "all" + entityName + "s";
			dataFetcherQueryMap.put(allEntityName, new FindAllDataFetcher(jpaRepository));
			String getEntityName = "get" + entityName;
			dataFetcherQueryMap.put(getEntityName, new FindOneDataFetcher(jpaRepository));
			String newEntityName = "new" + entityName;
			dataFetcherMutationMap.put(newEntityName, new CreateDataFetcher(jpaRepository, entityType.getJavaType()));
			String updateEntityName = "update" + entityName;
			dataFetcherMutationMap.put(updateEntityName, new UpdateDataFetcher(jpaRepository, entityType.getJavaType()));
		}
		// TODO: allow additional queries and mutations
		RuntimeWiring runtimeWiring = newRuntimeWiring()
				.type("Query", builder -> builder.dataFetchers(dataFetcherQueryMap))
				.type("Mutation", builder -> builder.dataFetchers(dataFetcherMutationMap))
				.build();
		return new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
	}
}
