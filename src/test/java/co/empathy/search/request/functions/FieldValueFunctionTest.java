package co.empathy.search.request.functions;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@MicronautTest
public class FieldValueFunctionTest {

	@Test
	public void testFieldValueFunction() {
		// Unmodified field value function
		var function = new FieldValueFunction("test");
		assertEquals("test", function.getField());
		assertNull(function.getFactor());
		assertNull(function.getMissing());
		assertNull(function.getModifier());
		// Full field value function
		function = new FieldValueFunction("test", 1f, FieldValueFunction.Modifier.LOG1P, 0f);
		assertEquals("test", function.getField());
		assertEquals(1f, function.getFactor());
		assertEquals(FieldValueFunction.Modifier.LOG1P, function.getModifier());
		assertEquals(0f, function.getMissing());
	}
}
