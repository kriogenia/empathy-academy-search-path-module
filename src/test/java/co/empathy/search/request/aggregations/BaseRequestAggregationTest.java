package co.empathy.search.request.aggregations;

import co.empathy.engines.AggregationVisitor;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotNull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class BaseRequestAggregationTest {

	@Test
	public void testIllegalArguments() {
		// Required name
		var exception = assertThrows(IllegalArgumentException.class,
				() -> new BaseRequestAggregation(null, "field") {
					@Override
					public @NotNull Object accept(@NotNull AggregationVisitor visitor) {
						return null;
					}
				});
		assertEquals("The aggregation name is required", exception.getMessage());
		// Required field
		exception = assertThrows(IllegalArgumentException.class,
				() -> new BaseRequestAggregation("name", null) {
					@Override
					public @NotNull Object accept(@NotNull AggregationVisitor visitor) {
						return null;
					}
				});
		assertEquals("The aggregation field is required", exception.getMessage());

	}
}
