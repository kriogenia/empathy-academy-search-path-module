package co.empathy.search.request.filters;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class RangeFilterTest {

	/**
	 * Test the overall creation and retrieval of the class
	 */
	@Test
	public void singleRangeFilterTest() {
		// Valid filter
		var filter = new DateRangesFilter("field", "2010/2020");
		assertEquals("field", filter.getField());
		assertEquals(1, filter.getRanges().size());
		var range = filter.getRanges().get(0);
		assertEquals("2010", range.from);
		assertEquals("2020", range.to);
		assertEquals("year", filter.getFormat());
		// Incomplete range
		var exception = assertThrows(IllegalArgumentException.class,
				() -> new DateRangesFilter("field", "2010"));
		assertTrue(exception.getMessage().contains("Ranges of dates must have two edges"));
		// Range with excessive fields
		exception = assertThrows(IllegalArgumentException.class,
				() -> new DateRangesFilter("field", "2010/2020/2030"));
		assertTrue(exception.getMessage().contains("Ranges of dates must have two edges"));
	}

	// accept test is on the respective filter

}
