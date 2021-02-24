package com.example.search;

import com.example.pojos.SearchResult;
import com.example.search.engines.SearchEngine;
import io.micronaut.context.annotation.Prototype;
import io.reactivex.annotations.NonNull;

import javax.inject.Named;
import java.io.IOException;

@Prototype
public class SearcherImpl implements Searcher {

	@NonNull
	private SearchEngine engine;

	public SearcherImpl(@NonNull @Named("elastic") SearchEngine engine) {
		this.engine = engine;
	}

	public void setEngine(@NonNull SearchEngine engine) {
		this.engine = engine;
	}

	@Override
	public SearchResult search(String query) throws IOException {
		// Query result
		return new SearchResult(query, engine.getVersion());
	}
}
