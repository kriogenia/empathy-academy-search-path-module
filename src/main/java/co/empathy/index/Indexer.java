package co.empathy.index;

import co.empathy.engines.SearchEngine;
import co.empathy.index.configuration.IndexConfiguration;
import io.reactivex.annotations.NonNull;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

/**
 * Objects that can create an index in the Search Engines
 */
public interface Indexer {

	/**
	 * Changes the Indexer engine
	 * @param engine	new SearchEngine to use
	 * @return 			modified indexer
	 */
	@NotNull
	Indexer setEngine(@NonNull SearchEngine engine);

	/**
	 * Changes the Indexer configuration
	 * @param configuration	new IndexConfiguration to use
	 * @return 				modified indexer
	 */
	@NotNull
	Indexer setConfiguration(@NonNull IndexConfiguration configuration);

	/**
	 * Changes the Indexer extensions
	 * @param extensions    list of IndexConfigurations to extend
	 * @return              modified indexer
	 */
	@NotNull
	Indexer setExtensions(List<IndexConfiguration> extensions);

	/**
	 * Reads and indexes the content of the configuration file
	 * @throws IOException	if an I/O error occurs opening the file
	 * 						or with the search engine
	 */
	void index() throws IOException;

	/**
	 * Reads and indexes the content of the configuration file
	 * through Bulk Requests
	 * @throws IOException	if an I/O error occurs opening the file
	 * 						or with the search engine
	 */
	void bulkIndex() throws IOException;

	/**
	 * Reads and updates the index with the content of the configuration
	 * extension files through Bulk Requests
	 * @throws IOException  if an I/O error occurs opening the file
	 * 	 * 					or with the search engine
	 */
	void bulkExtend() throws IOException;

	/**
	 * Deletes the index specified by the current IndexConfiguration
	 * @return true if the index exists and was successfully deleted
	 * @throws IOException	if an I/O error occurs opening the file
	 * 						or with the search engine
	 */
	boolean delete() throws IOException;

}
