package co.empathy.search.request.filters;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class RangeFilterTest {

	/**
	 * Test the overall creation and retrieval of the class
	 */
	@Test
	public void rangeFilterTest() {
		// Valid filter
		var filter = new RangeFilter("field", "2010/2020");
		assertEquals("field", filter.getField());
		assertEquals("2010", filter.getFrom());
		assertEquals("2020", filter.getTo());
		assertEquals("YYYY", filter.getFormat());
		// Incomplete range
		var exception = assertThrows(IllegalArgumentException.class,
				() -> new RangeFilter("field", "2010"));
		assertEquals("Ranges of dates must have two edges", exception.getMessage());
		// Range with excessive fields
		exception = assertThrows(IllegalArgumentException.class,
				() -> new RangeFilter("field", "2010/2020/2030"));
		assertEquals("Ranges of dates must have two edges", exception.getMessage());
	}

}
