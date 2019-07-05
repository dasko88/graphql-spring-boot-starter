package it.ts.dotcom.demo.graphqlspringbootstarter.service.schema;

import java.util.List;

public class GraphQLType {

	private String name;
	private List<GraphQLField> fieldList;

	private GraphQLField idField;

	public GraphQLType(String name, List<GraphQLField> fieldList) {
		this.name = name;
		this.fieldList = fieldList;
	}

	public String getName() {
		return name;
	}

	public List<GraphQLField> getFieldList() {
		return fieldList;
	}

	// TODO: search for @Id
	public GraphQLField getIdField() {
		return new GraphQLField("id", Integer.class);
	}
}
