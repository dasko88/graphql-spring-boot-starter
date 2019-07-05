package it.ts.dotcom.demo.graphqlspringbootstarter.configuration;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import it.ts.dotcom.demo.graphqlspringbootstarter.service.schema.TypeDefinitionBuilder;
import it.ts.dotcom.demo.graphqlspringbootstarter.service.schema.parser.GraphQLSchemaParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

	@Bean
	public GraphQL getGraphQL(GraphQLSchemaStrategy graphQLSchemaStrategy) {
		GraphQLSchema schema = graphQLSchemaStrategy.create();
		return GraphQL.newGraphQL(schema).build();
	}

	@Bean
	public TypeDefinitionBuilder getTypeDefinitionBuilder() {
		return new TypeDefinitionBuilder();
	}

	@Bean
	public GraphQLSchemaParser getGraphQLSchemaParser() {
		return new GraphQLSchemaParser();
	}

	@Bean
	public GraphQLSchemaStrategy getGraphQLSchemaStrategy() {
		return new ReflectionSchemaStrategy();
	}

}