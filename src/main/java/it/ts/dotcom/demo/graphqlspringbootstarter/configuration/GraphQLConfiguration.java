package it.ts.dotcom.demo.graphqlspringbootstarter.configuration;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import it.ts.dotcom.demo.graphqlspringbootstarter.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLConfiguration {

	@Autowired
	@Qualifier("resourceSchemaStrategy")
	private GraphQLSchemaStrategy graphQLSchemaStrategy;

	@Autowired
	HelloService helloService;

	@Bean
	public GraphQL initGraphQL() {
		helloService.sayHello();
		GraphQLSchema schema = graphQLSchemaStrategy.create();
		return GraphQL.newGraphQL(schema).build();
	}

}
