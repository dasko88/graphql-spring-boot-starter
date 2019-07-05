package it.ts.dotcom.demo.graphqlspringbootstarter.configuration;

import graphql.schema.GraphQLSchema;

public interface GraphQLSchemaStrategy {

	GraphQLSchema create();
}
