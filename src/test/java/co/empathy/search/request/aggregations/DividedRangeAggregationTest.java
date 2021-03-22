package co.empathy.search.request.aggregations;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class DividedRangeAggregationTest {

	@Test
	public void dividedRangeAggregation() {
		// Valid object
		var agg = new DividedRangeAggregation("name", "field", 0, 10, 5);
		assertEquals("name", agg.getName());
		assertEquals("field", agg.getField());
		assertEquals(0, agg.getFrom());
		assertEquals(10, agg.getTo());
		assertEquals(5, agg.getGap());
		// Invalid range
		var exception = assertThrows(IllegalArgumentException.class,
				() -> new DividedRangeAggregation("name", "field", 10, 0 , 2));
		assertEquals("Range start can't be after the end", exception.getMessage());
		// Invalid gap
		exception = assertThrows(IllegalArgumentException.class,
				() -> new DividedRangeAggregation("name", "field", 0, 10, -2));
		assertEquals("The gap between ranges can't be negative", exception.getMessage());
	}
}
