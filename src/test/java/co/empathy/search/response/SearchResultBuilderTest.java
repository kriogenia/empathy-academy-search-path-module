package co.empathy.search.response;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
public class SearchResultBuilderTest {

	@Inject
	SearchResultBuilder builder;

	/**
	 * Tests the SearchResult builder
	 */
	@Test
	public void buildTest() {
		// Total
		long value = 2;
		TotalHits total = new TotalHits(value, TotalHits.Relation.EQUAL_TO);
		// Items
		SearchHit hit = mock(SearchHit.class);
		when(hit.getId()).thenReturn("tt000000");
		when(hit.getSourceAsMap()).thenReturn(new HashMap<>());
		SearchHit[] hits = {hit};
		SearchHits searchHits = mock(SearchHits.class);
		when(searchHits.getTotalHits()).thenReturn(total);
		when(searchHits.getHits()).thenReturn(hits);
		// Aggregations
		Terms terms = mock(Terms.class);
		when(terms.getName()).thenReturn("test");
		when(terms.getBuckets()).thenReturn(new ArrayList<>());
		List<Aggregation> aggList = new ArrayList<>();
		aggList.add(terms);
		Aggregations aggs = mock(Aggregations.class);
		when(aggs.asList()).thenReturn(aggList);
		when(aggs.get("test")).thenReturn(terms);
		// Response
		SearchResponse response = mock(SearchResponse.class);
		when(response.getHits()).thenReturn(searchHits);
		when(response.getAggregations()).thenReturn(aggs);

		var built = builder.build(response);
		assertEquals(value, built.getTotal());
		var items = built.getItems();
		assertEquals("tt000000", items.get(0).get("id"));
		var aggregations = built.getAggregations();
		assertTrue(aggregations.containsKey("test"));
	}
}
