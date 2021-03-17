package co.empathy.search.response;

import io.micronaut.context.annotation.Factory;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Factory
public class SearchResultBuilder {

	/**
	 * Builds a SearchResult from an ElasticSearch response
	 * @param esResponse	SearchResponse from ElasticSearch
	 * @return				SearchResult with the search info
	 */
	public SearchResult build(SearchResponse esResponse) {
		// Get hits
		SearchHits hits = esResponse.getHits();
		long totalHits = hits.getTotalHits().value;
		var items = Arrays.stream(hits.getHits()).map(this::flatHit);
		var aggregations = new HashMap<String, Map<String, Long>>();
		if (esResponse.getAggregations() != null ) {
			for (var key : esResponse.getAggregations().getAsMap().keySet()) {
				aggregations.put(key, flatAggregation(esResponse.getAggregations().get(key)));
			}
		}
		return new SearchResult(totalHits, items.collect(Collectors.toList()), aggregations);
	}

	/**
	 * Converts a ES SearchHit into flat map
	 * @param hit	ElasticSearch search hit
	 * @return		Flat map with the retrieved attributes
	 */
	private Map<String, Object> flatHit(SearchHit hit) {
		var map = hit.getSourceAsMap();
		map.put("id", hit.getId());
		return map;
	}

	/**
	 * Converts a Elastic Search Terms into a flat map
	 * @param terms ElasticSearch Terms Aggregation
	 * @return      Flat map with the retrieved aggregations
	 */
	private Map<String, Long> flatAggregation(Terms terms) {
		var buckets = terms.getBuckets();
		return buckets.stream().collect(
				Collectors.toMap(Terms.Bucket::getKeyAsString, Terms.Bucket::getDocCount));
	}

}