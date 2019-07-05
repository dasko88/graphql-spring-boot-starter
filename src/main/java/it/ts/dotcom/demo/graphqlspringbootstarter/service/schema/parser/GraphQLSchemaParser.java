package it.ts.dotcom.demo.graphqlspringbootstarter.service.schema.parser;

import it.ts.dotcom.demo.graphqlspringbootstarter.service.schema.GraphQLType;
import it.ts.dotcom.demo.graphqlspringbootstarter.service.schema.SchemaHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphQLSchemaParser {

	private Map<Class, String> classMap;

	public GraphQLSchemaParser() {
		this.classMap = new HashMap<>();
		classMap.put(Integer.class, "Int");
		classMap.put(String.class, "String");
	}

	public String parseGraphQLTypeList(List<GraphQLType> graphQLTypeList) {
		List<String> types = new ArrayList<>();
		graphQLTypeList.forEach(type -> {
			StringBuilder typeSB = new StringBuilder();
			typeSB.append("type");
			typeSB.append(" ");
			typeSB.append(type.getName());
			typeSB.append(" ");
			typeSB.append("{");
			typeSB.append("\n\t");
			List<String> fields = new ArrayList<>();
			type.getFieldList().forEach(field -> {
				StringBuilder fieldSB = new StringBuilder();
				fieldSB.append(field.getName());
				fieldSB.append(":");
				fieldSB.append(" ");
				fieldSB.append(classMap.get(field.getClazz()));
				fields.add(fieldSB.toString());
			});
			typeSB.append(fields.stream().collect(Collectors.joining("\n\t")));
			typeSB.append("\n");
			typeSB.append("}");
			types.add(typeSB.toString());

		});
		return types.stream().collect(Collectors.joining("\n\n"));
	}

	public String parseGraphQLQueryList(List<GraphQLType> graphQLTypeList) {
		List<String> queries = new ArrayList<>();
		queries.add("type Query {");
		graphQLTypeList.forEach(type -> {
			StringBuilder gueryGetSB = new StringBuilder();
			gueryGetSB.append("\t");
			gueryGetSB.append(SchemaHelper.applyTrasformation(type.getName(), SchemaHelper.getEntityTrasformation()));
			gueryGetSB.append("(");
			gueryGetSB.append(type.getIdField().getName());
			gueryGetSB.append(":");
			gueryGetSB.append(" ");
			gueryGetSB.append(classMap.get(type.getIdField().getClazz()));
			gueryGetSB.append("!");
			gueryGetSB.append(")");
			gueryGetSB.append(":");
			gueryGetSB.append(" ");
			gueryGetSB.append(type.getName());
			queries.add(gueryGetSB.toString());
			StringBuilder queryAllSB = new StringBuilder();
			queryAllSB.append("\t");
			queryAllSB.append(SchemaHelper.applyTrasformation(type.getName(), SchemaHelper.allEntityTrasformation()));
			queryAllSB.append(":");
			queryAllSB.append(" ");
			queryAllSB.append("[");
			queryAllSB.append(type.getName());
			queryAllSB.append("]");
			queries.add(queryAllSB.toString());
		});
		queries.add("}");
		return queries.stream().collect(Collectors.joining("\n"));
	}

	public String parseGraphQLMutationList(List<GraphQLType> graphQLTypeList) {
		List<String> mutations = new ArrayList<>();
		mutations.add("type Mutation {");
		graphQLTypeList.forEach(type -> {
			StringBuilder mutationNewSB = new StringBuilder();
			mutationNewSB.append("\t");
			mutationNewSB.append(SchemaHelper.applyTrasformation(type.getName(), SchemaHelper.newEntityTrasformation()));
			mutationNewSB.append("(");
			List<String> fields = new ArrayList<>();
			type.getFieldList().stream()
					.filter(field -> field.getName() != "id")
					.forEach(field -> {
						StringBuilder fieldSB = new StringBuilder();
						fieldSB.append(field.getName());
						fieldSB.append(":");
						fieldSB.append(" ");
						fieldSB.append(classMap.get(field.getClazz()));
						fields.add(fieldSB.toString());
					});
			mutationNewSB.append(fields.stream().collect(Collectors.joining(", ")));
			mutationNewSB.append(")");
			mutationNewSB.append(":");
			mutationNewSB.append(" ");
			mutationNewSB.append(type.getName());
			mutationNewSB.append("!");
			mutations.add(mutationNewSB.toString());
			StringBuilder mutationUpdateSB = new StringBuilder();
			mutationUpdateSB.append("\t");
			mutationUpdateSB.append(SchemaHelper.applyTrasformation(type.getName(), SchemaHelper.updateEntityTrasformation()));
			mutationUpdateSB.append("(");
			List<String> updateFields = new ArrayList<>();
			type.getFieldList().forEach(field -> {
				StringBuilder fieldSB = new StringBuilder();
				fieldSB.append(field.getName());
				fieldSB.append(":");
				fieldSB.append(" ");
				fieldSB.append(classMap.get(field.getClazz()));
				updateFields.add(fieldSB.toString());
			});
			mutationUpdateSB.append(updateFields.stream().collect(Collectors.joining(", ")));
			mutationUpdateSB.append(")");
			mutationUpdateSB.append(":");
			mutationUpdateSB.append(" ");
			mutationUpdateSB.append(type.getName());
			mutationUpdateSB.append("!");
			mutations.add(mutationUpdateSB.toString());
		});
		mutations.add("}");
		return mutations.stream().collect(Collectors.joining("\n"));
	}
}
