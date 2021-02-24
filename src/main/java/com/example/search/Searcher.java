package com.example.search;

import com.example.pojos.SearchResult;

import java.io.IOException;

public interface Searcher {

	SearchResult search(String query) throws IOException;

}
