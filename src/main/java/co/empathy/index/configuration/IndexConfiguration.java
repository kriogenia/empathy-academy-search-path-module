package co.empathy.index.configuration;

import co.empathy.engines.EEngine;
import co.empathy.index.Indexable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;

/**
 * Classes holding all the needed configuration to create an index
 */
public interface IndexConfiguration {

	/**
	 * @return	key of the index
	 */
	@NotEmpty
	String getKey();

	/**
	 * @return	the path of the file to index
	 */
	@NotEmpty
	String getFilePath();

	/**
	 * @return  function that builds the pojo of the read file
	 */
	Function<String, Indexable> getBuilder();

	/**
 	 * @return	size for the bulk requests
	 */
	@Positive
	int getBulkSize();

	/**
	 * @return	number of bulks to index
	 */
	@Positive
	int getTotalBulks();

	/**
	 * @param requesterInfo enum of the engine requesting with the necessary info
	 * @return				index configuration as a string
	 * @throws IOException	if an error occurred reading the config file
	 */
	@NotEmpty
	String getSource(EEngine requesterInfo) throws IOException;


}
