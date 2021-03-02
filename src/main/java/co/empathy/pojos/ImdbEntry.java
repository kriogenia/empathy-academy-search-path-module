package co.empathy.pojos;

import co.empathy.index.Indexable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Indexable POJO class to manage IMDB entries
 */
public class ImdbEntry implements Indexable {

	private final String tconst;
	private final String titleType;
	private final String primaryTitle;
	private final String originalTitle;
	private final String isAdult;
	private final String startYear;
	private final String endYear;
	private final String runtimeMinutes;
	private final String genres;

	/**
	 *
	 * @param tconst	ID
	 * @param titleType	Type
	 * @param primaryTitle	International title
	 * @param originalTitle	Original language title
	 * @param isAdult	If it's only for adults
	 * @param startYear	Starting year of screening
	 * @param endYear	Ending year of screening ("/N" if it doesn't have or
	 *                  is the same as the starting year)
	 * @param runtimeMinutes	Total length in minutes
	 * @param genres	Genres
	 */
	@JsonCreator
	public ImdbEntry(
			@JsonProperty("tconst") String tconst,
			@JsonProperty("titleType") String titleType,
			@JsonProperty("primaryTitle") String primaryTitle,
			@JsonProperty("originalTitle") String originalTitle,
			@JsonProperty("isAdult") String isAdult,
			@JsonProperty("startYear") String startYear,
			@JsonProperty("endYear") String endYear,
			@JsonProperty("runtimeMinutes") String runtimeMinutes,
			@JsonProperty("genres") String genres) {
		this.tconst = tconst;
		this.titleType = titleType;
		this.primaryTitle = primaryTitle;
		this.originalTitle = originalTitle;
		this.isAdult = isAdult;
		this.startYear = startYear;
		this.endYear = endYear;
		this.runtimeMinutes = runtimeMinutes;
		this.genres = genres;
	}

	public ImdbEntry(String... params) {
		this(params[0], params[1], params[2],
				params[3], params[4], params[5],
				params[6], params[7], params[8]);
	}

	@Override
	public String getId() {
		return tconst;
	}

	@Override
	public Map<String, Object> getJson() {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("titleType", titleType);
		jsonMap.put("primaryTitle", primaryTitle);
		jsonMap.put("originalTitle", originalTitle);
		jsonMap.put("isAdult", isAdult);
		jsonMap.put("startYear", startYear);
		jsonMap.put("endYear", endYear);
		jsonMap.put("runtimeMinutes", runtimeMinutes);
		jsonMap.put("genres", genres);
		return jsonMap;
	}

}
