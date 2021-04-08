package co.empathy.engines.elastic;

import co.empathy.search.request.filters.DateRangesFilter;
import co.empathy.search.request.filters.TermsFilter;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class ElasticFilterVisitorTest {

	@Inject
	ElasticFilterVisitor visitor;

	/**
	 * Test the correct transformation of the DateRangeFilter to a RangeQuery
	 */
	@Test
	public void testDateRangeFilter() {
		var filter = new DateRangesFilter("test", "2010/2020");
		var query = (RangeQueryBuilder) filter.accept(visitor);
		assertEquals("test", query.fieldName());
		assertEquals("2010", query.from());
		assertEquals("2020", query.to());
		assertEquals("year", query.format());
	}

	/**
	 * Test the correct transformation orf the TermsFilter to a TermsQuery
	 */
	@Test
	public void testTermsFilter() {
		var filter = new TermsFilter("test", "a,b");
		var query = (TermsQueryBuilder) filter.accept(visitor);
		assertEquals("test", query.fieldName());
		var values = query.values();
		assertEquals("a", values.get(0));
		assertEquals("b", values.get(1));
	}
}
