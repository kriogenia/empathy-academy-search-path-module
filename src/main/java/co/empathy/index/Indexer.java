package co.empathy.index;

import co.empathy.engines.SearchEngine;
import co.empathy.index.configuration.IndexConfiguration;
import io.reactivex.annotations.NonNull;

import java.io.IOException;

/**
 * Objects that can create an index in the Search Engines
 */
public interface Indexer {

	/**
	 * Changes the Indexer engine
	 * @param engine	new SearchEngine to use
	 * @return 			modified indexer
	 */
	Indexer setEngine(@NonNull SearchEngine engine);

	/**
	 * Changes the Indexer configuration
	 * @param configuration	new IndexConfiguration to use
	 * @return 				modified indexer
	 */
	Indexer setConfiguration(@NonNull IndexConfiguration configuration);

	/**
	 * Reads and inserts the content of the file into an index
	 * @throws IOException	if an I/O error occurs opening the file
	 * 						or with the search engine
	 */
	void indexFile() throws IOException;

	/**
	 * Reads and inserts the content of the file into an index
	 * through Bulk Requests
	 * @throws IOException	if an I/O error occurs opening the file
	 * 						or with the search engine
	 */
	void bulkIndexFile() throws IOException;

	/**
	 * Deletes the index specified by the current IndexConfiguration
	 * @throws IOException	if an I/O error occurs opening the file
	 * 						or with the search engine
	 */
	boolean deleteIndex() throws IOException;

}
