package it.ts.dotcom.demo.graphqlspringbootstarter.service.schema.parser;

import it.ts.dotcom.demo.graphqlspringbootstarter.service.schema.GraphQLField;
import it.ts.dotcom.demo.graphqlspringbootstarter.service.schema.GraphQLType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphQLSchemaParserTest {

	private GraphQLSchemaParser graphQLSchemaParser;
	private List<GraphQLType> graphQLTypeList;

	@Before
	public void setUp() throws Exception {
		graphQLSchemaParser = new GraphQLSchemaParser();
		graphQLTypeList = new ArrayList<>();
	}

	@Test
	public void parseGraphQLTypeList() {
		List<GraphQLField> userFields = Arrays.asList(
				new GraphQLField("id", Integer.class),
				new GraphQLField("name", String.class),
				new GraphQLField("surname", String.class),
				new GraphQLField("height", Integer.class)
		);
		List<GraphQLField> taskFields = Arrays.asList(
				new GraphQLField("id", Integer.class),
				new GraphQLField("name", String.class),
				new GraphQLField("code", String.class),
				new GraphQLField("prioriy", Integer.class)
		);
		graphQLTypeList = Arrays.asList(
				new GraphQLType("User", userFields),
				new GraphQLType("Task", taskFields)
		);

		String typeSchema = graphQLSchemaParser.parseGraphQLTypeList(graphQLTypeList);

		Assert.assertEquals("type User {\n" +
				"\tid: Int\n" +
				"\tname: String\n" +
				"\tsurname: String\n" +
				"\theight: Int\n" +
				"}\n" +
				"\n" +
				"type Task {\n" +
				"\tid: Int\n" +
				"\tname: String\n" +
				"\tcode: String\n" +
				"\tprioriy: Int\n" +
				"}", typeSchema);
	}

	@Test
	public void parseGraphQLQueryList() {
		List<GraphQLField> userFields = Arrays.asList(
				new GraphQLField("id", Integer.class),
				new GraphQLField("name", String.class),
				new GraphQLField("surname", String.class),
				new GraphQLField("height", Integer.class)
		);
		List<GraphQLField> taskFields = Arrays.asList(
				new GraphQLField("id", Integer.class),
				new GraphQLField("name", String.class),
				new GraphQLField("code", String.class),
				new GraphQLField("prioriy", Integer.class)
		);
		graphQLTypeList = Arrays.asList(
				new GraphQLType("User", userFields),
				new GraphQLType("Task", taskFields)
		);

		String typeSchema = graphQLSchemaParser.parseGraphQLQueryList(graphQLTypeList);

		Assert.assertEquals("type Query {\n" +
				"\tgetUser(id: Int!): User\n" +
				"\tallUsers: [User]\n" +
				"\tgetTask(id: Int!): Task\n" +
				"\tallTasks: [Task]\n" +
				"}", typeSchema);
	}

	@Test
	public void parseGraphQLMutationList() {
		List<GraphQLField> userFields = Arrays.asList(
				new GraphQLField("id", Integer.class),
				new GraphQLField("name", String.class),
				new GraphQLField("surname", String.class),
				new GraphQLField("height", Integer.class)
		);
		List<GraphQLField> taskFields = Arrays.asList(
				new GraphQLField("id", Integer.class),
				new GraphQLField("name", String.class),
				new GraphQLField("code", String.class),
				new GraphQLField("prioriy", Integer.class)
		);
		graphQLTypeList = Arrays.asList(
				new GraphQLType("User", userFields),
				new GraphQLType("Task", taskFields)
		);

		String typeSchema = graphQLSchemaParser.parseGraphQLMutationList(graphQLTypeList);

		Assert.assertEquals("type Mutation {\n" +
				"\tnewUser(name: String, surname: String, height: Int): User!\n" +
				"\tupdateUser(id: Int, name: String, surname: String, height: Int): User!\n" +
				"\tnewTask(name: String, code: String, prioriy: Int): Task!\n" +
				"\tupdateTask(id: Int, name: String, code: String, prioriy: Int): Task!\n" +
				"}", typeSchema);
	}

}