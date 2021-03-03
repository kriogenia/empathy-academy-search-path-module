package co.empathy.search;

import java.io.IOException;
import java.io.Serializable;

/**
 * Module managing the search engines
 */
public interface Searcher {

	/**
	 * Process a query and gets the search result matching the original title
	 * @param query	Query to match
	 * @return	Indexable object with the found info
	 * @throws IOException	when entered invalid queries or search engines failures
	 */
	Serializable searchByTitle(String query) throws IOException;

}
