package co.empathy.search.request.aggregations;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class TermsAggregationTest {

	@Test
	public void termsAggregationTest() {
		var aggs = new TermsAggregation("name", "field");
		assertEquals("name", aggs.getName());
		assertEquals("field", aggs.getField());
	}

}
