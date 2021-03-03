package co.empathy.index;

import co.empathy.engines.SearchEngine;
import co.empathy.beans.ImdbItem;
import io.micronaut.context.annotation.Prototype;

import javax.inject.Inject;
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
 * Indexer of the IMDB database, by default uses ElasticSearch
 */
@Prototype
public class ImdbIndexer implements Indexer {

	public static final String INDEX_KEY = "imdb";
	public static final int BULK_SIZE = 10000;
	public static final int TOTAL_BULKS = 766;

	private int counter = 0;

	//TODO abstract the engine from the indexer to pick one
	@Inject
	SearchEngine engine;

	@Override
	public void indexFile(String filePath) throws IOException {
		System.out.format("Starting %s indexing...\n", filePath);
		Stream<String> stream = Files.lines(Paths.get(filePath)).skip(1);
		stream.map(x -> new ImdbItem(x.split("\t"))).
				forEach(this::callIndex);
	}

	@Override
	public void bulkIndexFile(String filePath) throws IOException {
		System.out.format("Starting %s bulk indexing...\n", filePath);
		List<Indexable> bulk = new ArrayList<>();		// Create the bulk list
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		reader.readLine();								// Skip the tab header
		String line = reader.readLine();
		// For each line create a new entry
		while (line != null) {
			// When the bulk is full send it to index
			if (bulk.size() >= BULK_SIZE) {
				engine.bulkIndex(INDEX_KEY, bulk);
				bulk = new ArrayList<>();
				System.out.println((++counter)*100/TOTAL_BULKS + "% - Filling new bulk - " +
						LocalDateTime.now());
			}
			// Add the entry to the bulk and advance one line
			bulk.add(new ImdbItem(line.split("\t")));
			line = reader.readLine();
		}
		engine.bulkIndex(INDEX_KEY, bulk);
		System.out.format("Ending %s bulk indexing...\n", filePath);
	}

	/**
	 * Calls the engine to index the entry
	 * @param entry	New entry to index
	 */
	private void callIndex(Indexable entry) {
		try {
			engine.index(INDEX_KEY, entry);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
