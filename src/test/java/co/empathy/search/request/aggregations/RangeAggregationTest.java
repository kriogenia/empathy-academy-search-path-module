package co.empathy.search.request.aggregations;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class RangeAggregationTest {

	@Test
	public void rangeAggregationTest() {
		// Valid whole range
		var aggs = new RangeAggregation("name", "field", 0, 10);
		assertEquals("name", aggs.getName());
		assertEquals("field", aggs.getField());
		assertEquals(0, aggs.getFrom());
		assertEquals(10, aggs.getTo());
		// Valid start range
		aggs = new RangeAggregation("name", "field", 0, null);
		assertEquals("name", aggs.getName());
		assertEquals("field", aggs.getField());
		assertEquals(0, aggs.getFrom());
		assertNull(aggs.getTo());
		// Valid end range
		aggs = new RangeAggregation("name", "field", null, 0);
		assertEquals("name", aggs.getName());
		assertEquals("field", aggs.getField());
		assertNull(aggs.getFrom());
		assertEquals(0, aggs.getTo());
		// No range - INVALID
		var exception = assertThrows(IllegalArgumentException.class,
				() -> new RangeAggregation("name", "field", null, null));
		assertEquals("Ranges must have two edges", exception.getMessage());
	}
}
