package co.empathy.search.request.functions;

import co.empathy.engines.FilterVisitor;
import co.empathy.engines.FunctionVisitor;
import co.empathy.search.request.filters.BaseRequestFilter;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class BaseRequestFunctionTest {

	@Test
	public void testIllegalArguments() {
		// Valid creation
		var request = new BaseRequestFunction("field") {
			@Override
			public @NotNull Object accept(@NotNull FunctionVisitor visitor) {
				return null;
			}
		};
		assertEquals("field", request.getField());
		// Null field
		var exception = assertThrows(IllegalArgumentException.class,
				() -> new BaseRequestFunction(null) {
					@Override
					public @NotNull Object accept(@NotNull FunctionVisitor visitor) {
						return null;
					}
				});
		assertEquals("Functions needs a field to work with", exception.getMessage());
		// Empty field
		exception = assertThrows(IllegalArgumentException.class,
				() -> new BaseRequestFunction("") {
					@Override
					public @NotNull Object accept(@NotNull FunctionVisitor visitor) {
						return null;
					}
				});
		assertEquals("Functions needs a field to work with", exception.getMessage());

	}
}
