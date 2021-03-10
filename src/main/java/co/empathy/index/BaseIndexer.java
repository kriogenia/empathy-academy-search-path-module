package co.empathy.index;

import co.empathy.beans.ImdbItem;
import co.empathy.engines.SearchEngine;
import co.empathy.index.configuration.IndexConfiguration;
import io.micronaut.context.annotation.Prototype;
import io.reactivex.annotations.NonNull;

import javax.inject.Named;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Base implementation of the Indexer interface
 */
@Prototype
public class BaseIndexer implements Indexer {

	@NonNull
	private SearchEngine engine;

	@NonNull
	private IndexConfiguration config;

	/**
	 * Constructor of the Indexer.
	 * As injected automatically loads the ElasticSearchEngine with IMDB configuration by default.
	 * @param engine		Engine the Indexer will use.
	 * @param configuration	Configuration of the index to create
	 */
	public BaseIndexer(
			@NonNull @Named("elastic") SearchEngine engine,
			@NonNull @Named("imdb") IndexConfiguration configuration) {
		this.engine = engine;
		this.config = configuration;
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
	public void indexFile() throws IOException {
		System.out.format("Starting %s indexing...\n", config.getFilePath());
		// Create index in case it doesn't exists
		if (!existsIndex()) {
			createIndex();
		}
		// Start indexing the files
		System.out.println("Index ready, indexing the files...");
		Stream<String> stream = Files.lines(Paths.get(config.getFilePath())).skip(1);
		stream.map(ImdbItem::buildFromString).
				forEach(this::callIndex);
		System.out.format("Ending %s indexing...\n", config.getFilePath());
	}

	@Override
	public void bulkIndexFile() throws IOException {
		System.out.format("Starting %s bulk indexing...\n", config.getFilePath());
		// Create index in case it doesn't exists
		if (!existsIndex()) {
			createIndex();
		}
		// Start the bulk index
		System.out.println("Index ready, bulk indexing the files...");
		List<Indexable> bulk = new ArrayList<>();		// Create the bulk list
		BufferedReader reader = new BufferedReader(new FileReader(config.getFilePath()));
		reader.readLine();								// Skip the tab header
		String line = reader.readLine();
		// For each line create a new entry
		int counter = 0;
		while (line != null) {
			// When the bulk is full send it to index
			if (bulk.size() >= config.getBulkSize()) {
				engine.bulkIndex(config.getKey(), bulk);
				bulk = new ArrayList<>();
				// Log progress
				System.out.println((++counter) * 100 / config.getTotalBulks() + "% - Filling new bulk - " +
						LocalDateTime.now());
			}
			// Add the entry to the bulk and advance one line
			bulk.add(ImdbItem.buildFromString(line));
			line = reader.readLine();
		}
		engine.bulkIndex(config.getKey(), bulk);
		System.out.format("Ending %s bulk indexing...\n", config.getFilePath());
	}

	/**
	 * @return	true if the specified index exists, false in case it doesn't
	 */
	private boolean existsIndex() throws IOException {
		System.out.println("Checking if the index already exists...");
		return engine.hasIndex(config.getKey());
	}

	/**
	 * Builds an index with the
	 */
	private void createIndex() throws IOException {
		System.out.println("Creating new index...");
		engine.createIndex(config);
		System.out.println("Index successfully built");
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

}
