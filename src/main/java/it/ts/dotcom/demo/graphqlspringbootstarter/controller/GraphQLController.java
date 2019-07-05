package it.ts.dotcom.demo.graphqlspringbootstarter.controller;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/graphql")
public class GraphQLController {

	@Autowired
	GraphQL graphQL;

	@ResponseStatus(HttpStatus.OK)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ExecutionResult query(@RequestBody Map<String, Object> request) {
		return graphQL.execute(ExecutionInput.newExecutionInput()
				.query((String) request.get("query"))
				.operationName((String) request.get("operationName"))
				.variables((Map) request.get("variables"))
				.build());
	}

	/**
	 * TODO: API GET graphql.schema
	 */
}
