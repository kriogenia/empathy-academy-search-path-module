package co.empathy.search.response;

import io.micronaut.context.annotation.Factory;
import org.apache.http.protocol.RequestUserAgent;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator;
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
		var aggregations = buildAggregations(esResponse);
		return new SearchResult(totalHits, items.collect(Collectors.toList()), aggregations);
	}

	/**
	 * Builds the common map for aggregations
	 * @param esResponse    response with the aggregations
	 * @return              aggregations into maps
	 */
	private Map<String, Map<String, Long>> buildAggregations(SearchResponse esResponse) {
		var aggregations = new HashMap<String, Map<String, Long>>();
		var responseAggregations = esResponse.getAggregations();
		if (responseAggregations != null ) {
			for (var key : responseAggregations.getAsMap().keySet()) {
				if (!key.contains("_filtered")) {
					aggregations.put(key, flatAggregation(responseAggregations.get(key)));
				}
				else {
					var name = key.replace("_filtered", "");
					aggregations.put(name, flatNestedAggregation(name, responseAggregations.get(key)));
				}
			}
		}
		return aggregations;
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
	 * Converts a Elastic Search Aggregation into a flat map
	 * @param agg ElasticSearch Terms Aggregation
	 * @return      Flat map with the retrieved aggregations
	 */
	private Map<String, Long> flatAggregation(MultiBucketsAggregation agg) {
		var buckets = agg.getBuckets();
		return buckets.stream().filter(x -> x.getDocCount() > 0).collect(Collectors.toMap(
				MultiBucketsAggregation.Bucket::getKeyAsString,
				MultiBucketsAggregation.Bucket::getDocCount));
	}

	/**
	 * Converts a nested Elastic Search Aggregation into a flat map
	 * @param name  name of the aggregation to return
	 * @param agg   ElasticSearch Terms Aggregation
	 * @return      Flat map with the retrieved aggregations
	 */
	private Map<String, Long> flatNestedAggregation(String name, MultiBucketsAggregation agg) {
		var buckets = agg.getBuckets();
		return flatAggregation(buckets.get(0).getAggregations().get(name));
	}


}