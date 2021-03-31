package co.empathy.index;

import co.empathy.common.ImdbItem;
import co.empathy.engines.SearchEngine;
import co.empathy.index.configuration.IndexConfiguration;
import io.micronaut.runtime.http.scope.RequestScope;
import io.reactivex.annotations.NonNull;
import org.apache.lucene.index.IndexNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * Base implementation of the Indexer interface
 */
@RequestScope
public class BaseIndexer implements Indexer {

	private static final Logger LOG = LoggerFactory.getLogger(BaseIndexer.class);

	@NonNull
	private SearchEngine engine;

	@NonNull
	private IndexConfiguration config;

	private List<IndexConfiguration> extensions;

	/**
	 * Constructor of the Indexer.
	 * As injected automatically loads the ElasticSearchEngine with IMDB configuration by default.
	 * @param engine		Engine the Indexer will use.
	 * @param configuration	Configuration of the index to create
	 */
	public BaseIndexer(
			@NonNull @Named("elastic") SearchEngine engine,
			@NonNull @Named("imdbbasics") IndexConfiguration configuration) {
		this.engine = engine;
		this.config = configuration;
		this.extensions = new ArrayList<>();
	}

	@Override
	public Indexer setEngine(@NonNull SearchEngine engine) {
		this.engine = engine;
		return this;
	}

	@Override
	public Indexer setConfiguration(@NonNull IndexConfiguration configuration) {
		this.config = configuration;
		return this;
	}

	@Override
	public Indexer setExtensions(List<IndexConfiguration> extensions) {
		this.extensions = extensions;
		return this;
	}

	@Override
	public void index() throws IOException {
		LOG.info("Starting {} indexing...", config.getFilePath());
		// Create index in case it doesn't exist
		if (!existsIndex()) {
			createIndex();
		}
		// Start indexing the files
		LOG.info("Index ready, indexing the files...");
		Stream<String> stream = Files.lines(Paths.get(config.getFilePath())).skip(1);
		stream.map(ImdbItem::buildFromString).
				forEach(this::callIndex);
		LOG.info("Ending {} indexing...", config.getFilePath());
	}

	@Override
	public void bulkIndex() throws IOException {
		LOG.info("Starting {} bulk indexing...", config.getFilePath());
		// Create index in case it doesn't exists
		if (!existsIndex()) {
			createIndex();
		}
		// Start the bulk index
		LOG.info("Index ready, bulk indexing the files...");
		processBulk(config, this::indexAccept);
		LOG.info("Ending {} bulk indexing...", config.getFilePath());
	}

	@Override
	public void bulkExtend() throws IOException {
		// Check the index exists before starting the update
		if (!existsIndex()) {
			throw new IndexNotFoundException("The specified index does not exists, it can't be updated");
		}
		for (var extension: extensions) {
			LOG.info("Starting {} bulk updating...", extension.getFilePath());
			processBulk(extension, this::updateAccept);
			LOG.info("Ending {} bulk updating...", extension.getFilePath());
		}
		LOG.info("Finished the extension of the index {}", config.getKey());
	}

	@Override
	public boolean delete() throws IOException {
		LOG.info("Deleting {} index...", config.getKey());
		if (existsIndex()) {
			engine.deleteIndex(config.getKey());
			LOG.info("Index {} successfully deleted", config.getKey());
			return true;
		} else {
			LOG.info("Index {} doesn't exists and can't be deleted", config.getKey());
			return false;
		}
	}

	/**
	 * @return	true if the specified index exists, false in case it doesn't
	 */
	private boolean existsIndex() throws IOException {
		LOG.info("Checking if the index already exists...");
		return engine.hasIndex(config.getKey());
	}

	/**
	 * Builds an index with the
	 */
	private void createIndex() throws IOException {
		LOG.info("Creating new index.{}..", config.getKey());
		engine.createIndex(config);
		LOG.info("Index {} successfully built", config.getKey());
	}

	/**
	 * Calls the engine to index the entry
	 * @param entry	New entry to index
	 */
	private void callIndex(Indexable entry) {
		try {
			engine.index(config.getKey(), entry);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates bulks from the specified file and calls the bulking function
	 * @param config            configuration of the bulk to process
	 * @param bulkingFunction   function to invoke with the bulks
	 * @throws IOException      if an I/O error occurs
	 */
	private void processBulk(IndexConfiguration config, BiConsumer<String, List<Indexable>> bulkingFunction) throws IOException {
		List<Indexable> bulk = new ArrayList<>();		// Create the bulk list
		BufferedReader reader = new BufferedReader(new FileReader(config.getFilePath()));
		reader.readLine();								// Skip the tab header
		String line = reader.readLine();
		// For each line create a new entry
		int counter = 0;
		while (line != null) {
			// When the bulk is full send it to index
			if (bulk.size() >= config.getBulkSize()) {
				bulkingFunction.accept(config.getKey(), bulk);
				bulk = new ArrayList<>();
				// Log progress
				LOG.info("{}% completed - Filling new bulk", (++counter) * 100 / config.getTotalBulks());
			}
			// Add the entry to the bulk and advance one line
			bulk.add(config.getBuilder().apply(line));
			line = reader.readLine();
		}
		bulkingFunction.accept(config.getKey(), bulk);
	}

	// TODO extract throwing consumer interface

	/**
	 * Lambda call for bulk indexing
	 * @param key       Key of the index
	 * @param bulk      Bulk to index
	 */
	private void indexAccept(String key, List<Indexable> bulk) {
		try {
			engine.bulkIndex(key, bulk);
		} catch (IOException ioe) {
			LOG.error("IOException {}", ioe.getMessage());
			throw new RuntimeException(ioe.getMessage());
		}
	}

	/**
	 * Lambda call for bulk updating
	 * @param key       Key of the index
	 * @param bulk      Bulk to update
	 */
	private void updateAccept(String key, List<Indexable> bulk) {
		try {
			engine.bulkUpdate(key, bulk);
		} catch (IOException ioe) {
			LOG.error("IOException {}", ioe.getMessage());
			throw new RuntimeException(ioe.getMessage());
		}
	}
}
