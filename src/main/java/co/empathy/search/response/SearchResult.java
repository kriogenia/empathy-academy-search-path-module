package co.empathy.search.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Common POJO class to manage search results
 */
public class SearchResult {

	public final static String TOTAL = "total";
	public final static String ITEMS = "items";
	public final static String AGGREGATIONS = "aggregations";

	private final long total;
	private final List<Map<String, Object>> items;
	private final Map<String,Map<String, Long>> aggregations;

	/**
	 * Constructor and JSON creator of the search result object
	 * @param total			Number of hits of the Search
	 * @param items			List of retrieved hits
	 */
	@JsonCreator
	public SearchResult(
			@JsonProperty(TOTAL) long total,
			@JsonProperty(ITEMS) List<Map<String, Object>> items,
			@JsonProperty(AGGREGATIONS)  Map<String,Map<String, Long>> aggregations) {
		this.total = total;
		this.items = items;
		this.aggregations = aggregations;
	}

	/**
	 * Builds a SearchResult pojo from an ElasticSearch response
	 * @param esResponse	SearchResponse from ElasticSearch
	 * @return				SearchResult with the search info
	 */
	public static SearchResult builder(SearchResponse esResponse) {
		// Get hits
		SearchHits hits = esResponse.getHits();
		long totalHits = hits.getTotalHits().value;
		var items = Arrays.stream(hits.getHits()).map(SearchResult::flatHit);
		var aggregations = new HashMap<String, Map<String, Long>>();
		for (var key : esResponse.getAggregations().getAsMap().keySet()) {
			aggregations.put(key, flatAggregation(esResponse.getAggregations().get(key)));
		}
		return new SearchResult(totalHits, items.collect(Collectors.toList()), aggregations);
	}

	/**
	 * @return	Total hits of the search
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @return	List of items retrieved from the search as maps
	 */
	public List<Map<String, Object>> getItems() {
		return items;
	}

	/**
	 * @return  Map with all the aggregations of the search
	 */
	public Map<String, Map<String, Long>> getAggregations() {
		return aggregations;
	}

	/**
	 * Converts a ES SearchHit into flat map
	 * @param hit	ElasticSearch search hit
	 * @return		Flat map with the retrieved attributes
	 */
	private static Map<String, Object> flatHit(SearchHit hit) {
		var map = hit.getSourceAsMap();
		map.put("id", hit.getId());
		return map;
	}

	private static Map<String, Long> flatAggregation(Terms terms) {
		var buckets = terms.getBuckets();
		return buckets.stream().collect(
				Collectors.toMap(Terms.Bucket::getKeyAsString, Terms.Bucket::getDocCount));
	}

}
