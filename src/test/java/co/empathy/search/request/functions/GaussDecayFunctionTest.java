package co.empathy.search.request.functions;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@MicronautTest
public class GaussDecayFunctionTest {

	@Test
	public void testGaussDecayFunction() {
		// Without offset
		var function = new GaussDecayFunction("test", "now", "1d", 0.5f);
		assertEquals("test", function.getField());
		assertEquals("now", function.getOrigin());
		assertEquals("1d", function.getScale());
		assertEquals(0.5f, function.getDecay());
		assertNull(function.getOffset());
		// With offset
		function = new GaussDecayFunction("test", "now", "1d", 0.5f, "2d");
		assertEquals("test", function.getField());
		assertEquals("now", function.getOrigin());
		assertEquals("1d", function.getScale());
		assertEquals(0.5f, function.getDecay());
		assertEquals("2d", function.getOffset());
	}
}
