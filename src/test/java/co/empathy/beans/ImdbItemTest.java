package co.empathy.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class ImdbItemTest {

	@Inject
	ObjectMapper mapper;

	private ImdbItem item;

	@BeforeEach
	public void resetItem() {
		item = new ImdbItem().setId("tt0000001").setTitleType("short")
				.setPrimaryTitle("Carmencita").setOriginalTitle("Carmencita")
				.setIsAdult("0").setStartYear("1894").setEndYear("\\N")
				.setRuntime("0").setGenres(new String[]{"Documentary", "Short"});
	}

	/**
	 * Tests the automatic conversion of the item to JSON
	 * @throws JsonProcessingException	if the parsing fails
	 */
	@Test
	public void toJsonTest() throws JsonProcessingException {
		var json = mapper.writeValueAsString(item);
		var jsonMap = mapper.readValue(json, Map.class);
		// ID
		assertEquals("tt0000001", jsonMap.get(ImdbItem.ID));
		// Title
		assertEquals("Carmencita", jsonMap.get(ImdbItem.TITLE));
		// Genres
		assertTrue(jsonMap.get(ImdbItem.GENRES) instanceof List<?>);
		var genres = jsonMap.get(ImdbItem.GENRES);
		var genresList = new ArrayList<String>();
		for (Object o: (List<?>) genres) {
			assertTrue(o instanceof String);
			genresList.add((String) o);
		}
		assertArrayEquals(item.getGenres(), genresList.toArray());
		// Type
		assertEquals("short", jsonMap.get(ImdbItem.TYPE));
		// Start year
		assertEquals("1894", jsonMap.get(ImdbItem.START));
		// End year empty
		assertNull(jsonMap.get(ImdbItem.END));
		// End year fulfilled
		item.setEndYear("1895");
		json = mapper.writeValueAsString(item);
		jsonMap = mapper.readValue(json, Map.class);
		assertEquals("1895", jsonMap.get(ImdbItem.END));
	}

	/**
	 * Tests the automatic conversion from JSON to the object
	 * @throws JsonProcessingException	if the parsing fails
	 */
	@Test
	public void fromJsonTest() throws JsonProcessingException {
		String json = "{" + "\"" + ImdbItem.ID + "\": \"tt0000001\"," +
				"\"" + ImdbItem.TYPE + "\": \"short\"," +
				"\"" + ImdbItem.TITLE + "\": \"Carmencita\"," +
				"\"" + ImdbItem.ORIGINAL_TITLE + "\": \"Carmencita\"," +
				"\"" + ImdbItem.IS_ADULT + "\": \"0\"," +
				"\"" + ImdbItem.START + "\": \"1894\"," +
				"\"" + ImdbItem.RUNTIME_MINUTES + "\": \"0\"," +
				"\"" + ImdbItem.GENRES + "\": [\"Documentary\", \"Short\"]}";
		var fromJson = mapper.readValue(json, ImdbItem.class);
		assertEquals(item.getId(), fromJson.getId());
		assertEquals(item.getPrimaryTitle(), fromJson.getPrimaryTitle());
		assertEquals(item.getTitleType(), fromJson.getTitleType());
		assertEquals(item.getStartYear(), fromJson.getStartYear());
		assertEquals(item.getEndYear(), fromJson.getEndYear());
		assertArrayEquals(item.getGenres(), fromJson.getGenres());
		assertNull(fromJson.getOriginalTitle());
		assertNull(fromJson.getIsAdult());
		assertNull(fromJson.getRuntime());
	}

	/**
	 * Tests the setEndYear
	 */
	@Test
	public void setEndYearTest() {
		assertNull(item.getEndYear());
		// Valid end year
		item.setEndYear("1900");
		assertEquals("1900", item.getEndYear());
		// No end year
		item.setEndYear("\\N");
		assertNull(item.getEndYear());
		// Null end year
		item.setEndYear(null);
		assertNull(item.getEndYear());
	}

	/**
	 * Tests the setGenres of ImdbItem and its exceptions
	 */
	@Test
	public void setGenresTest() {
		// Less than one genre - INVALID
		String[] no_genres = new String[0];
		var exception = assertThrows(IllegalArgumentException.class,
				() -> item.setGenres(no_genres));
		assertEquals("Invalid number of genres", exception.getMessage());
		// More than three genres - INVALID
		String[] more_than_3 = new String[4];
		exception = assertThrows(IllegalArgumentException.class, () -> item.setGenres(more_than_3));
		assertEquals("Invalid number of genres", exception.getMessage());
		// Between one and three genres
		String[] valid = new String[]{"Documentary", "Short"};
		item.setGenres(valid);
		assertArrayEquals(valid, item.getGenres());
	}

	/**
	 * Tests the toJsonMap of Indexable
	 */
	@Test
	public void toJsonMapTest() {
		var map = item.toJsonMap();
		Map<String, Object> jsonMap = new HashMap<>();
		// No ID
		assertNull(map.get(ImdbItem.ID));
		// Type
		assertEquals("short", map.get(ImdbItem.TYPE));
		// Primary title
		assertEquals("Carmencita", map.get(ImdbItem.TITLE));
		// Original title
		assertEquals("Carmencita", map.get(ImdbItem.ORIGINAL_TITLE));
		// Is adult
		assertEquals("0", map.get(ImdbItem.IS_ADULT));
		// Start year
		assertEquals("1894", map.get(ImdbItem.START));
		// Runtime minutes
		assertEquals("0", map.get(ImdbItem.RUNTIME_MINUTES));
		// Genres
		assertTrue(map.get(ImdbItem.GENRES) instanceof String[]);
		var genres = (String[]) map.get(ImdbItem.GENRES);
		assertArrayEquals(new String[]{"Documentary", "Short"}, genres);
		// No end year
		assertNull(map.get(ImdbItem.END));
		item.setEndYear("1900");
		map = item.toJsonMap();
		assertEquals("1900", map.get(ImdbItem.END));
	}

	/**
	 * Test the parseGenres of ImdbItem and its exceptions
	 */
	@Test
	public void parseGenresTest() {
		// Less than one - INVALID
		String no_genres = "";
		var exception = assertThrows(IllegalArgumentException.class,
				() -> item.parseGenres(no_genres));
		assertEquals("Invalid number of genres", exception.getMessage());
		// More than three - INVALID
		String more_than_3_genres = "a,b,c,d";
		exception = assertThrows(IllegalArgumentException.class, () -> item.parseGenres(more_than_3_genres));
		assertEquals("Invalid number of genres", exception.getMessage());
		// Between 1 and three genres
		String valid = "Short";
		item.parseGenres(valid);
		assertArrayEquals(new String[]{valid}, item.getGenres());
	}

	/**
	 * Tests the buildFromString from ImdbItem and its exceptions
	 */
	@Test
	public void buildFromStringTest() {
		// Less than 9 arguments
		String less_than_9 = "a\tb\tc\td\te\tf\tg\th";
		var exception = assertThrows(IllegalArgumentException.class,
				() -> ImdbItem.buildFromString(less_than_9));
		assertEquals("IMDB items must have nine fields", exception.getMessage());
		// More than 9 arguments
		String more_than_9 = "a\tb\tc\td\te\tf\tg\th\ti\tj";
		exception = assertThrows(IllegalArgumentException.class,
				() -> ImdbItem.buildFromString(more_than_9));
		assertEquals("IMDB items must have nine fields", exception.getMessage());
		// Valid fields
		String valid = "tt0000001\tshort\tCarmencita\tCarmencita\t0\t1894\t\\N\t0\tDocumentary,Short";
		var newItem = ImdbItem.buildFromString(valid);
		assertEquals(item.getId(), newItem.getId());
		assertEquals(item.getPrimaryTitle(), newItem.getPrimaryTitle());
		assertEquals(item.getOriginalTitle(), newItem.getOriginalTitle());
		assertEquals(item.getTitleType(), newItem.getTitleType());
		assertEquals(item.getIsAdult(), newItem.getIsAdult());
		assertEquals(item.getStartYear(), newItem.getStartYear());
		assertEquals(item.getEndYear(), newItem.getEndYear());
		assertEquals(item.getRuntime(), newItem.getRuntime());
		assertArrayEquals(item.getGenres(), newItem.getGenres());

	}

}
