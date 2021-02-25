package co.empathy.search;

import co.empathy.pojos.SearchResult;

import java.io.IOException;

/**
 * Module managing the search engines
 */
public interface Searcher {

	/**
	 * Process a query and gets the search result
	 * @param query	Query to search
	 * @return	Result of the search
	 * @throws IOException	when entered invalid queries or search engines failures
	 */
	SearchResult search(String query) throws IOException;

}
