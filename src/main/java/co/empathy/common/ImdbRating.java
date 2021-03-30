package co.empathy.common;

import co.empathy.index.Indexable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.reactivex.Flowable;

import javax.annotation.Nullable;
import javax.validation.constraints.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Indexable POJO class to manage IMDB ratings
 */
public class ImdbRating implements Indexable {

	public static final String ID = "id";
	public static final String AVERAGE = "average_rating";
	public static final String VOTES = "num_votes";

	@NotNull(message = "ID cannot be null")
	private String id;

	@Min(value = 0, message = "The minimum rating is 0")
	@Max(value = 10, message = "The maximum rating is 10")
	private float averageRating;

	@Positive(message = "The number of votes can't be negative")
	private int numVotes;

	/**
	 * Empty constructor of the JavaBean
	 */
	public ImdbRating() {}

	@Override
	@NotEmpty
	public String getId() {
		return id;
	}

	/**
	 * @param id	alphanumeric unique identifier of the title
	 * @return 		the item modified
	 */
	@NotNull
	public ImdbRating setId(@NotNull String id) {
		if (id == null) {
			throw new IllegalArgumentException("A rating needs an ID to be associated with");
		}
		this.id = id;
		return this;
	}

	/**
	 * @return  weighted average on all the individual user ratings
	 */
	public float getAverageRating() {
		return averageRating;
	}

	/**
	 * @param averageRating weighted average on all the individual user ratings
	 * @return              the item modified
	 */
	public ImdbRating setAverageRating(float averageRating) {
		if (averageRating < 0 || averageRating > 10) {
			throw new IllegalArgumentException("The average rating must be between 0 and 10");
		}
		this.averageRating = averageRating;
		return this;
	}

	/**
	 * @return  number of votes the tiles has received
	 */
	public int getNumVotes() {
		return numVotes;
	}

	/**
	 * @param numVotes  number of votes the tiles has received
	 * @return          the item modified
	 */
	public ImdbRating setNumVotes(int numVotes) {
		if (numVotes < 1) {
			throw new IllegalArgumentException("Ratings must have at least one vote to exist");
		}
		this.numVotes = numVotes;
		return this;
	}

	@Override
	@NotEmpty
	public Map<String, Object> toJsonMap() {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put(AVERAGE, averageRating);
		jsonMap.put(VOTES, numVotes);
		return jsonMap;
	}

	/**
	 * Builds an ImdbRating from a single line string
	 * @param line	string with the properties separated by tabs
	 * @return		built ImdbItem
	 * @throws IllegalArgumentException	if the string doesn't have exactly nine properties
	 */
	@NotNull
	public static ImdbRating buildFromString(@NotNull String line) {
		String[] params =  line.split("\t");
		if (params.length != 3) {
			throw new IllegalArgumentException("IMDB ratings must have three fields");
		}
		return new ImdbRating()
				.setId(params[0])
				.setAverageRating(Float.parseFloat(params[1]))
				.setNumVotes(Integer.parseInt(params[2]));
	}

}
