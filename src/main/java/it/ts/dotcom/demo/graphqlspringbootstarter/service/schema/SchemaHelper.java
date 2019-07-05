package it.ts.dotcom.demo.graphqlspringbootstarter.service.schema;

import it.ts.dotcom.demo.graphqlspringbootstarter.service.StringTrasformation;

public class SchemaHelper {

	public static String applyTrasformation(String s, StringTrasformation stringTrasformation) {
		return stringTrasformation.transform(s);
	}

	public static StringTrasformation allEntityTrasformation() {
		return s -> "all" + s + "s";
	}

	public static StringTrasformation newEntityTrasformation() {
		return s -> "new" + s;
	}

	public static StringTrasformation updateEntityTrasformation() {
		return s -> "update" + s;
	}

	public static StringTrasformation getEntityTrasformation() {
		return s -> "get" + s;
	}
}
