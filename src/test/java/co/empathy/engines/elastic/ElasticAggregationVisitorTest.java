package co.empathy.engines.elastic;

import co.empathy.search.request.aggregations.DividedRangeAggregation;
import co.empathy.search.request.aggregations.RangeAggregation;
import co.empathy.search.request.aggregations.TermsAggregation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@MicronautTest
public class ElasticAggregationVisitorTest {

	@Inject
	ElasticAggregationVisitor visitor;

	@Inject
	ObjectMapper mapper;

	/**
	 * Test the correct transformation of the aggregation to the ES equivalent
	 * @throws JsonProcessingException  if the mapping fails
	 */
	@Test
	public void testDividedRangeAggregation() throws JsonProcessingException {
		var range = new DividedRangeAggregation("name", "field", 0, 10, 5);
		var query = (DateRangeAggregationBuilder) range.accept(visitor);
		assertEquals("name", query.getName());
		assertEquals("field", query.field());
		var map = mapper.readValue(query.toString(),
				new TypeReference<Map<String, Map<String, Object>>>() {});
		// Check we got a date_range aggregation
		assertNotNull(map.get("name").get("date_range"));
		// And it has the correct ranges
		var ranges = (ArrayList<?>) ((Map<?,?>) map.get("name").get("date_range")).get("ranges");
		assertEquals(2, ranges.size());
		var first = (Map<?,?>) ranges.get(0);
		assertEquals(0.0, first.get("from"));
		assertEquals(5.0, first.get("to"));
		var second = (Map<?,?>) ranges.get(1);
		assertEquals(5.0, second.get("from"));
		assertNull(second.get("to"));
	}

	/**
	 * Test the correct transformation of the aggregation to the ES equivalent
	 * @throws JsonProcessingException  if the mapping fails
	 */
	@Test
	public void testDateRangeAggregation() throws JsonProcessingException {
		var rangeAggregation = new RangeAggregation("name", "field", 0, 10);
		var query = (DateRangeAggregationBuilder) rangeAggregation.accept(visitor);
		assertEquals("name", query.getName());
		assertEquals("field", query.field());
		var map = mapper.readValue(query.toString(),
				new TypeReference<Map<String, Map<String, Object>>>() {});
		// Check we got a date_range aggregation
		assertNotNull(map.get("name").get("date_range"));
		// And it has the correct ranges
		var ranges = (ArrayList<?>) ((Map<?,?>) map.get("name").get("date_range")).get("ranges");
		assertEquals(1, ranges.size());
		var range = (Map<?,?>) ranges.get(0);
		assertEquals(0.0, range.get("from"));
		assertEquals(10.0, range.get("to"));
	}

	/**
	 * Test the correct transformation of the aggregation to the ES equivalent
	 */
	@Test
	public void testTermsAggregation() {
		var terms = new TermsAggregation("name", "field");
		var query = (TermsAggregationBuilder) terms.accept(visitor);
		assertEquals("name", query.getName());
		assertEquals("field", query.field());
	}
}
