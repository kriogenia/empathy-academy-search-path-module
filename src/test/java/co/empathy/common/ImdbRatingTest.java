package co.empathy.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ImdbRatingTest {

	private ImdbRating item;

	@BeforeEach
	public void resetItem() {
		item = new ImdbRating();
	}

	/**
	 * Tests the setId
	 */
	@Test
	public void setIdTest() {
		// Valid id
		item.setId("test");
		assertEquals("test", item.getId());
		// Invalid id
		var exception = assertThrows(IllegalArgumentException.class,
				() -> item.setId(null));
		assertEquals("A rating needs an ID to be associated with", exception.getMessage());
	}

	/**
	 * Test the setAverageRating
	 */
	@Test
	public void setAverageRatingTest() {
		// Valid rating
		item.setAverageRating(5);
		assertEquals(5.0, item.getAverageRating());
		// Low rating
		var exception = assertThrows(IllegalArgumentException.class,
				() -> item.setAverageRating(-1));
		assertEquals("The average rating must be between 0 and 10", exception.getMessage());
		// High rating
		exception = assertThrows(IllegalArgumentException.class, () -> item.setAverageRating(11));
		assertEquals("The average rating must be between 0 and 10", exception.getMessage());
	}

	/**
	 * Test the setVotes
	 */
	@Test
	public void setVotesTest() {
		// Valid rating
		item.setNumVotes(5);
		assertEquals(5, item.getNumVotes());
		// Zero votes
		var exception = assertThrows(IllegalArgumentException.class,
				() -> item.setNumVotes(0));
		assertEquals("Ratings must have at least one vote to exist", exception.getMessage());
		// Negative votes
		exception = assertThrows(IllegalArgumentException.class, () -> item.setNumVotes(-1));
		assertEquals("Ratings must have at least one vote to exist", exception.getMessage());
	}

	@Test
	public void toJsonMapTest() {
		item.setId("votes").setAverageRating(5.0f).setNumVotes(10);
		var jsonMap = item.toJsonMap();
		assertNull(jsonMap.get("id"));
		assertEquals(5.0f, jsonMap.get(ImdbRating.AVERAGE));
		assertEquals(10, jsonMap.get(ImdbRating.VOTES));
	}

	/**
	 * Test the buildFromString builds the object correctly
	 */
	@Test
	public void buildFromString() {
		item.setId("test").setAverageRating(5.0f).setNumVotes(10);
		var rating = ImdbRating.buildFromString("test\t5.0\t10");
		assertEquals(item.getId(), rating.getId());
		assertEquals(item.getAverageRating(), rating.getAverageRating());
		assertEquals(item.getNumVotes(), rating.getNumVotes());
	}

}
