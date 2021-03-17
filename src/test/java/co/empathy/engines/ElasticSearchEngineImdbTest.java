package co.empathy.engines;

import co.empathy.common.ImdbItem;
import co.empathy.search.response.SearchResult;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static co.empathy.util.ElasticSearchTestHelper.*;

@MicronautTest
public class ElasticSearchEngineImdbTest {

	@Inject
	ElasticSearchEngine engine;

	/**
	 * Test the ascii_folding filters applied to the titles
	 * @throws IOException	if the engine fails
	 * /
	@Test
	public void testAsciiFoldingFilter() throws IOException {
		// Ñ
		var items = performSingleMatch(engine, "Ocana", 1, 1);
		assertTrue(allContains("title","Ocaña", items));
	}

	/**
	 * Tests the lowercase filters applied to the titles
	 * @throws IOException	if the engine fails
	 * /
	@Test
	public void testLowercaseFilter() throws IOException {
		List<SearchResult> results = new ArrayList<>();
		// All lowercase
		results.add(engine.searchSingleMatch("carmencita", ImdbItem.ORIGINAL_TITLE, INDEX));
		// All uppercase
		results.add(engine.searchSingleMatch("CARMENCITA", ImdbItem.ORIGINAL_TITLE, INDEX));
		// Combined
		results.add(engine.searchSingleMatch("CarMeNCiTA", ImdbItem.ORIGINAL_TITLE, INDEX));

		for (var result: results) {
			assertEquals(1, result.getTotal());
			allContains("title", "Carmencita", result.getItems());
		}
	}

	/**
	 * Tests the number extensive search filter applied to the titles
	 * @throws IOException	if the engine fails
	 * /
	@Test
	public void testNumberExtensionFilter() throws IOException {
		// Text and roman to arabic
		var result1 = engine.searchSingleMatch("two", ImdbItem.ORIGINAL_TITLE, INDEX);
		var result2 = engine.searchSingleMatch("ii", ImdbItem.ORIGINAL_TITLE, INDEX);

		assertEquals(1, result1.getTotal());
		assertEquals(1, result2.getTotal());
		assertTrue(allContains("title","The Amazing Spider-Man 2", result1.getItems()));
		assertTrue(allContains("title","The Amazing Spider-Man 2", result2.getItems()));

		// Roman and arabic to text
		result1 = engine.searchSingleMatch("iv", ImdbItem.ORIGINAL_TITLE, INDEX);
		result2 = engine.searchSingleMatch("4", ImdbItem.ORIGINAL_TITLE, INDEX);

		assertEquals(1, result1.getTotal());
		assertEquals(1, result2.getTotal());
		assertTrue(allContains("title","The Fantastic Four", result1.getItems()));
		assertTrue(allContains("title","The Fantastic Four", result2.getItems()));

		// Arabic and text to roman
		result1 = engine.searchSingleMatch("5", ImdbItem.ORIGINAL_TITLE, INDEX);
		result2 = engine.searchSingleMatch("five", ImdbItem.ORIGINAL_TITLE, INDEX);

		assertEquals(1, result1.getTotal());
		assertEquals(1, result2.getTotal());
		assertTrue(allContains("title","Rocky V", result1.getItems()));
		assertTrue(allContains("title","Rocky V", result2.getItems()));
	}

	/**
	 * Tests the word delimited filter with the -
	 * @throws IOException	if the engine fails
	 * /
	@Test
	public void testDelimiterWithHyphen() throws IOException {
		List<SearchResult> results = new ArrayList<>();
		// Concatenated
		results.add(engine.searchSingleMatch("spiderman", ImdbItem.ORIGINAL_TITLE, INDEX));
		// Split
		results.add(engine.searchSingleMatch("spider man", ImdbItem.ORIGINAL_TITLE, INDEX));
		// With hyphen
		results.add(engine.searchSingleMatch("spider-man", ImdbItem.ORIGINAL_TITLE, INDEX));

		for (var result: results) {
			assertEquals(1, result.getTotal());
			allContains("title","The Amazing Spider-Man 2", result.getItems());
		}
	}

	/**
	 * Test the word delimiter filter with the '
	 * @throws IOException	if the engine fails
	 * /
	@Test
	public void testDelimiterWithApostrophes() throws IOException {
		List<SearchResult> results = new ArrayList<>();
		// Concatenated
		results.add(engine.searchSingleMatch("youre", ImdbItem.ORIGINAL_TITLE, INDEX));
		// Split
		results.add(engine.searchSingleMatch("you re", ImdbItem.ORIGINAL_TITLE, INDEX));
		// With hyphen
		results.add(engine.searchSingleMatch("you're", ImdbItem.ORIGINAL_TITLE, INDEX));

		for (var result: results) {
			assertEquals(1, result.getTotal());
			allContains("title","You're Fired!", result.getItems());
		}
	}

	/**
	 * Test the word delimiter with . and ?
	 * @throws IOException	if the engine fails
	 * /
	@Test
	public void testDelimiterWithOtherSymbols() throws IOException {
		//dr who to Dr. Who and Dr. Who?
		var items = performSingleMatch(engine, "dr who", 2, 2);
		assertTrue(allContains("title", "Dr. Who", items));
	}

	/**
	 * Tests the matches are related to only the original_title
	 * @throws IOException	if the engine fails
	 * /
	@Test
	public void testDifferentTitles() throws IOException {
		// Original title
		var items = performSingleMatch(engine, "Gisaengchung", 1,1);
		assertTrue(allContains("title","Parasite", items));
		// Primary title
		items = performSingleMatch(engine, "Parasite", 0, 0);
		assertEquals(0, items.size());
	}

	/**
	 * Test that the search can find stop word titles like It
	 * @throws IOException	if the engine fails
	 * /
	@Test
	public void testStopWords() throws IOException {
		var items = performSingleMatch(engine,"It", 1, 1);
		assertTrue(allContains("title", "It", items));
	}

	/**
	 * Tests the synonyms filter applied to the types
	 * @throws IOException	if the engine fails
	 * /
	@Test
	public void testTypeSynonyms() throws IOException {
		// film => movie
		var items = performMultiMatch(engine, "it film", 10, 10);
		assertTrue(allContains("type", "movie", items));
		// picture => movie
		items = performMultiMatch(engine, "it picture", 10, 10);
		assertTrue(allContains("type", "movie", items));
		// series => tvSeries
		items = performMultiMatch(engine,"mink series", 2, 2);
		assertTrue(anyContains("type", "tvSeries", items) ||
				anyContains("title", "Mink", items)
		);
		// ep => tvepisode
		items = performMultiMatch(engine, "who ep", 3, 3);
		assertTrue(allContains("type", "tvEpisode", items));
		// episode => tvepisode
		items = performMultiMatch(engine, "who episode", 3, 3);
		assertTrue(allContains("type", "tvEpisode", items));
		// special => tvspecial
		items = performMultiMatch(engine,"mink special", 2, 2);
		assertTrue(anyContains("type", "tvSpecial", items) ||
				anyContains("title", "Mink", items)
		);
		// miniseries => tvminiseries
		items = performMultiMatch(engine,"mink miniseries", 2, 2);
		assertTrue(anyContains("type", "tvMiniSeries", items) ||
				anyContains("title", "Mink", items)
		);
		// game => videogame
		items = performMultiMatch(engine,"mink game", 2, 2);
		assertTrue(anyContains("type", "videoGame", items) ||
				anyContains("title", "Mink", items)
		);
	}

	// TODO test search with decimal digit

}
*/