package co.empathy.search.response;

import io.micronaut.context.annotation.Factory;
import org.apache.http.protocol.RequestUserAgent;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.SingleBucketAggregation;
import org.elasticsearch.search.aggregations.bucket.range.Range;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.term.TermSuggestion;

import java.util.*;
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
		var suggestions = buildSuggestions(esResponse);
		return new SearchResult(totalHits, items.collect(Collectors.toList()))
				.setAggregations(aggregations).setSuggestions(suggestions);
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
			for (var agg : responseAggregations.asList()) {
				if (!agg.getName().contains("_filtered")) {
					aggregations.put(agg.getName(), flatAggregation(responseAggregations.get(agg.getName())));
				}
				else {
					var name = agg.getName().replace("_filtered", "");
					aggregations.put(name, flatNestedAggregation(name, responseAggregations.get(agg.getName())));
				}
			}
		}
		return aggregations;
	}

	/**
	 * Builds the list of suggested options
	 * @param esResponse    response with the suggestions
	 * @return              suggestions into string list
	 */
	private List<String> buildSuggestions(SearchResponse esResponse) {
		var suggestions = new ArrayList<String>();
		var responseSuggestions = esResponse.getSuggest();
		if (responseSuggestions != null) {
			for (var suggestion : responseSuggestions) {
				suggestions.addAll(flatSuggestion(suggestion));
			}
		}
		return suggestions;
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
	 * @param agg   ElasticSearch Terms Aggregation
	 * @return      flat ordered map with the retrieved aggregations
	 */
	private Map<String, Long> flatAggregation(MultiBucketsAggregation agg) {
		var buckets = agg.getBuckets();
		return buckets.stream().filter(x -> x.getDocCount() > 0).collect(Collectors.toMap(
				MultiBucketsAggregation.Bucket::getKeyAsString,
				MultiBucketsAggregation.Bucket::getDocCount,
				Math::addExact,
				LinkedHashMap::new));
	}

	/**
	 * Converts a nested Elastic Search Aggregation into a flat map
	 * @param name  name of the aggregation to return
	 * @param agg   ElasticSearch Terms Aggregation
	 * @return      Flat map with the retrieved aggregations
	 */
	private Map<String, Long> flatNestedAggregation(String name, SingleBucketAggregation agg) {
		return flatAggregation(agg.getAggregations().get(name));
	}

	/**
	 * Converts a ElasticSearch suggestion into a flat list
	 * @param suggestion    suggestion to flatten
	 * @return              flat list with the options of the suggestion
	 */
	private List<String> flatSuggestion(Suggest.Suggestion<?> suggestion) {
		var list = new ArrayList<String>();
		var entries = suggestion.getEntries();
		for (var entry: entries) {
			for (var option :entry.getOptions()) {
				list.add(((Suggest.Suggestion.Entry.Option) option).getText().string());
			}
		}
		return list;
	}



}