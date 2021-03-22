package co.empathy.search.request.filters;

import co.empathy.engines.FilterVisitor;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class BaseRequestFilterTest {

	@Test
	public void testIllegalArguments() {
		// Valid creation
		var request = new BaseRequestFilter("field") {
			@Override
			public @NotNull Object accept(@NotNull FilterVisitor visitor) {
				return null;
			}
		};
		// Null field
		assertEquals("field", request.getField());
		var exception = assertThrows(IllegalArgumentException.class,
				() -> new BaseRequestFilter(null) {
					@Override
					public @NotNull Object accept(@NotNull FilterVisitor visitor) {
						return null;
					}
				});
		assertEquals("Filtering needs the field to filter", exception.getMessage());
		// Empty field
		exception = assertThrows(IllegalArgumentException.class,
				() -> new BaseRequestFilter("") {
					@Override
					public @NotNull Object accept(@NotNull FilterVisitor visitor) {
						return null;
					}
				});
		assertEquals("Filtering needs the field to filter", exception.getMessage());
	}

}
