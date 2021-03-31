package co.empathy.search.request.functions;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class TermWeightingFunctionTest {

	@Test
	public void testTermWeightingFunction() {
		var function = new TermWeightingFunction("field", "text", 1f);
		assertEquals("field", function.getField());
		assertEquals("text", function.getText());
		assertEquals(1f, function.getWeight());
	}
}
