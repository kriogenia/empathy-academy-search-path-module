package co.empathy.engines.elastic;

import co.empathy.search.request.aggregations.DividedRangeAggregation;
import co.empathy.search.request.aggregations.RangeAggregation;
import co.empathy.search.request.aggregations.TermsAggregation;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class ElasticAggregationVisitorTest {

	@Inject
	ElasticAggregationVisitor visitor;

	@Test
	public void testDividedRangeTransform() {
		var range = new DividedRangeAggregation("name", "field", 0, 10, 5);
		var query = (DateRangeAggregationBuilder) range.accept(visitor);
		assertEquals("name", query.getName());
		assertEquals("field", query.field());
		assertEquals("YYYY", query.format());
	}

	@Test
	public void testDateRangeTransform() {
		var range = new RangeAggregation("name", "field", 0, 10);
		var query = (DateRangeAggregationBuilder) range.accept(visitor);
		assertEquals("name", query.getName());
		assertEquals("field", query.field());
		assertEquals("YYYY", query.format());
	}

	@Test
	public void testTermsTransform() {
		var terms = new TermsAggregation("name", "field");
		var query = (TermsAggregationBuilder) terms.accept(visitor);
		assertEquals("name", query.getName());
		assertEquals("field", query.field());

	}
}
