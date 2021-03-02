package co.empathy.index;

import co.empathy.engines.SearchEngine;
import co.empathy.pojos.ImdbEntry;
import io.micronaut.context.annotation.Prototype;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Indexer of the IMDB database, by default uses ElasticSearch
 */
@Prototype
public class ImdbIndexer implements Indexer {

	//TODO abstract the engine from the indexer to pick one
	@Inject
	SearchEngine engine;

	@Override
	public void indexFile(String filePath) throws IOException {
		System.out.format("Starting %s indexing...\n", filePath);
		Stream<String> stream = Files.lines(Paths.get(filePath)).skip(1);
		stream.map(x -> new ImdbEntry(x.split("\t"))).
				forEach(this::callIndex);

		// read file lazily
		// trim lines
		// convert to entries
		// bulk request
	}

	/**
	 * Calls the engine to index the entry
	 * @param entry	New entry to index
	 */
	private void callIndex(Indexable entry) {
		try {
			engine.index("imdb", entry);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
