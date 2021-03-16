package co.empathy.engines;

import co.empathy.index.Indexable;
import co.empathy.search.response.SearchResult;
import co.empathy.index.configuration.IndexConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
	 * Retrieves the search using match against pairs of field and queries
	 * @param queries		pairs of field and queries to search matching all
	 * @param indices		indices to search
	 * @return				result of the search
	 * @throws IOException	if an error occurred with the Search Engine
	 */
	SearchResult searchMultiMatch(Map<String, String> queries, String... indices) throws IOException;

	/**
	 * Retrieves the search using match against multiple fields
	 * @param query			query to match
	 * @param fields		fields to match
	 * @param indices		indices to search
	 * @return				result of the search
	 * @throws IOException	if an error occurred with the Search Engine
	 */
	SearchResult crossSearch(String query, String[] fields, String... indices) throws IOException;

	/**
	 * Retrieves the version of the cluster of the SearchEngine
	 * @return	Version number of the SearchEngine in use
	 * @throws IOException	if an error occurred with the Search Engine
	 */
	String getVersion() throws IOException;

	/**
	 * @param key	key of the index to check
	 * @return		true if the index already exists in the engine, false if it doesn't
	 * @throws IOException	if an error occurred with the Search Engine
	 */
	boolean hasIndex(String key) throws IOException;

	/**
	 * Creates and index with the specified configuration
	 * @param configuration	index requested configuration
	 * @throws IOException	if an error occurred with the search engine
	 */
	void createIndex(IndexConfiguration configuration) throws IOException;

	/**
	 * Deletes the requested index
	 * @param key	key of the index to delete
	 * @throws IOException	if an error occurred with the search engine
	 */
	void deleteIndex(String key) throws IOException;

}
