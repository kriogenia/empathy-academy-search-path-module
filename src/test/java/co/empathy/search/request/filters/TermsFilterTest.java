package co.empathy.search.request.filters;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class TermsFilterTest {

	/**
	 * Test the overall creation and retrieval of the class
	 */
	@Test
	public void termsFilterTest() {
		// One term
		var filter = new TermsFilter("field", "term");
		assertEquals("field", filter.getField());
		assertArrayEquals(new String[]{"term"}, filter.getTerms());
		// More terms
		filter = new TermsFilter("field", "term1,term2,term3");
		assertEquals("field", filter.getField());
		assertArrayEquals(new String[]{"term1", "term2", "term3"}, filter.getTerms());
	}

	// accept test is on the respective filter

}
