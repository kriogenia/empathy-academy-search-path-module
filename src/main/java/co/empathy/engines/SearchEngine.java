package co.empathy.engines;

import co.empathy.index.Indexable;
import co.empathy.beans.SearchResult;

import java.io.IOException;
import java.util.List;

/**
 * Common interface to adapt the search engines
 */
public interface SearchEngine extends AutoCloseable {

	/**
	 * Inserts an entry into the specified index
	 * @param index	Where to insert the entry
	 * @param entry	Document to insert
	 * @throws IOException	when an error occurred with the Search Engine
	 */
	void index(String index, Indexable entry) throws IOException;

	/**
	 * Inserts a collection of entries into the specified index
	 * @param index	Where to insert the collection
	 * @param entries	Collection of documents to insert
	 * @throws IOException	when an error occurred with the Search Engine
	 */
	void bulkIndex(String index, List<Indexable> entries) throws IOException;

	/**
	 * Retrieves the search of a matching title query
	 * @param title			Query to match
	 * @param indices		Indices to search
	 * @return				result of the search
	 * @throws IOException	if an error occurred with the Search Engine
	 */
	SearchResult searchByTitle(String title, String... indices) throws IOException;

	/**
	 * Retrieves the version of the cluster of the SearchEngine
	 * @return	Version number of the SearchEngine in use
	 * @throws IOException	to SearchEngine failures
	 */
	String getVersion() throws IOException;

}
