package co.empathy.search;

import co.empathy.engines.SearchEngine;
import co.empathy.pojos.SearchResult;
import io.micronaut.context.annotation.Prototype;
import io.reactivex.annotations.NonNull;

import javax.inject.Named;
import java.io.IOException;

/**
 * Default class implementing the Searcher interface
 */
@Prototype
public class SearcherImpl implements Searcher {

	@NonNull
	private SearchEngine engine;

	/**
	 * Constructor of the Searcher.
	 * As injected loads the ElasticSearchEngine by default.
	 * @param engine	Engine the Searcher will use.
	 */
	public SearcherImpl(@NonNull @Named("elastic") SearchEngine engine) {
		this.engine = engine;
	}

	/**
	 * Changes the Searcher engine
	 * @param engine	New SearchEngine to use
	 */
	public void setEngine(@NonNull SearchEngine engine) {
		this.engine = engine;
	}

	@Override
	public SearchResult search(String query) throws IOException {
		// Query result
		return new SearchResult(query, engine.getVersion());
	}
}
