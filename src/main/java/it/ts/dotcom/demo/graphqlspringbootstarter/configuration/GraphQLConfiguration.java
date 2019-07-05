package it.ts.dotcom.demo.graphqlspringbootstarter.configuration;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLConfiguration {

	@Autowired
	@Qualifier("resourceSchemaStrategy")
	private GraphQLSchemaStrategy graphQLSchemaStrategy;

	@Bean
	public GraphQL initGraphQL() {
		GraphQLSchema schema = graphQLSchemaStrategy.create();
		return GraphQL.newGraphQL(schema).build();
	}

}
