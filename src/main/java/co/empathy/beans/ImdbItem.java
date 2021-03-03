package co.empathy.beans;

import co.empathy.index.Indexable;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Indexable POJO class to manage IMDB entries
 */
public class ImdbItem implements Indexable {

	@JsonProperty("tconst")
	private String id;
	@JsonProperty("titleType")
	private String titleType;
	@JsonProperty("primaryTitle")
	private String primaryTitle;
	@JsonProperty("origintalTitle")
	private String originalTitle;
	@JsonProperty("isAdult")
	private String isAdult;
	@JsonProperty("startYear")
	private String startYear;
	@JsonProperty("endYear")
	private String endYear;
	@JsonProperty("runtimeMinutes")
	private String runtime;
	@JsonProperty("genres")
	private String genres;

	/**
	 *
	 * @param tconst			alphanumeric unique identifier of the title
	 * @param titleType			the type/format of the title (e.g. movie, short, tv series...)
	 * @param primaryTitle		the more popular title / title used on promotional materials
	 * @param originalTitle		original title, in the original language
	 * @param isAdult			O: non-adult title; 1: original title
	 * @param startYear			represents the release yer of a title (YYYY)
	 * @param endYear			TV Series end year, '\N' for all other title types
	 * @param runtimeMinutes	primary runtime of the title, in minutes
	 * @param genres			includes up to three genres associated with the title
	 */
	@JsonCreator
	public ImdbItem(
			@JsonProperty("tconst") String tconst,
			@JsonProperty("titleType") String titleType,
			@JsonProperty("primaryTitle") String primaryTitle,
			@JsonProperty("originalTitle") String originalTitle,
			@JsonProperty("isAdult") String isAdult,
			@JsonProperty("startYear") String startYear,
			@JsonProperty("endYear") String endYear,
			@JsonProperty("runtimeMinutes") String runtimeMinutes,
			@JsonProperty("genres") String genres) {
		this.id = tconst;
		this.titleType = titleType;
		this.primaryTitle = primaryTitle;
		this.originalTitle = originalTitle;
		this.isAdult = isAdult;
		this.startYear = startYear;
		this.endYear = endYear;
		this.runtime = runtimeMinutes;
		this.genres = genres;
	}

	public ImdbItem(String... params) {
		this(params[0], params[1], params[2],
				params[3], params[4], params[5],
				params[6], params[7], params[8]);
	}

	@Override
	public String getId() {
		return id;
	}



	@Override
	public Map<String, Object> getJsonMap() {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("titleType", titleType);
		jsonMap.put("primaryTitle", primaryTitle);
		jsonMap.put("originalTitle", originalTitle);
		jsonMap.put("isAdult", isAdult);
		jsonMap.put("startYear", startYear);
		jsonMap.put("endYear", endYear);
		jsonMap.put("runtimeMinutes", runtime);
		jsonMap.put("genres", genres);
		return jsonMap;
	}

}
