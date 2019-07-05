package it.ts.dotcom.demo.graphqlspringbootstarter.service.schema;

public class GraphQLField<T> {

	private String name;
	private Class<T> clazz;

	public GraphQLField(String name, Class<T> clazz) {
		this.name = name;
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}

	public Class<T> getClazz() {
		return clazz;
	}
}
