package it.ts.dotcom.demo.graphqlspringbootstarter.service.schema;

import org.junit.Assert;
import org.junit.Test;

public class SchemaHelperTest {

	@Test
	public void stringTrasformation() {
		String value = "User";

		String trasf = SchemaHelper.applyTrasformation(value, SchemaHelper.allEntityTrasformation());

		Assert.assertEquals("allUsers", trasf);
	}
}