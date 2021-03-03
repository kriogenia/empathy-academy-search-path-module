package co.empathy.beans;

import co.empathy.index.Indexable;
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
	@JsonProperty("originalTitle")
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
	 * Empty constructor of the JavaBean
	 */
	public ImdbItem() {}

	/**i
	 * @return	alphanumeric unique identifier of the title
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id	alphanumeric unique identifier of the title
	 */
	public ImdbItem setId(String id) {
		this.id = id;
		return this;
	}

	/**
	 * @return	the type/format of the title (e.g. movie, short, tv series...)
	 */
	public String getTitleType() {
		return titleType;
	}

	/**
	 * @param titleType	the type/format of the title (e.g. movie, short, tv series...)
	 */
	public ImdbItem setTitleType(String titleType) {
		this.titleType = titleType;
		return this;
	}

	/**
	 * @return	the more popular title / title used on promotional materials
	 */
	public String getPrimaryTitle() {
		return primaryTitle;
	}


	/**
	 * @param primaryTitle	the more popular title / title used on promotional materials
	 */
	public ImdbItem setPrimaryTitle(String primaryTitle) {
		this.primaryTitle = primaryTitle;
		return this;
	}

	/**
	 * @return	original title, in the original language
	 */
	public String getOriginalTitle() {
		return originalTitle;
	}

	/**
	 * @param originalTitle	original title, in the original language
	 */
	public ImdbItem setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
		return this;
	}

	/**
	 * @return	O: non-adult title; 1: original title
	 */
	public String getIsAdult() {
		return isAdult;
	}

	/**
	 * @param isAdult	O: non-adult title; 1: original title
	 */
	public ImdbItem setIsAdult(String isAdult) {
		this.isAdult = isAdult;
		return this;
	}

	/**
	 * @return	release year of a title (YYYY)
	 */
	public String getStartYear() {
		return startYear;
	}

	/**
	 * @param startYear    release year of a title (YYYY)
	 */
	public ImdbItem setStartYear(String startYear) {
		this.startYear = startYear;
		return this;
	}

	/**
	 * @return	TV Series end year, '\N' for all other title types
	 */
	public String getEndYear() {
		return endYear;
	}

	/**
	 * @param endYear	TV Series end year, '\N' for all other title types
	 */
	public ImdbItem setEndYear(String endYear) {
		this.endYear = endYear;
		return this;
	}

	/**
	 * @return	primary runtime of the title, in minutes
	 */
	public String getRuntime() {
		return runtime;
	}

	/**
	 * @param runtime	primary runtime of the title, in minutes
	 */
	public ImdbItem setRuntime(String runtime) {
		this.runtime = runtime;
		return this;
	}

	/**
	 * @return	includes up to three genres associated with the title
	 */
	public String getGenres() {
		return genres;
	}

	/**
	 * @param genres	includes up to three genres associated with the title
	 */
	public ImdbItem setGenres(String genres) {
		this.genres = genres;
		return this;
	}

	@Override
	public Map<String, Object> toJsonMap() {
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

	public static ImdbItem buildFromString(String... params) {
		if (params.length != 9) {
			throw new IllegalArgumentException("IMDB items must have nine fields");
		}
		return new ImdbItem()
				.setId(params[0])
				.setTitleType(params[1])
				.setPrimaryTitle(params[2])
				.setOriginalTitle(params[3])
				.setIsAdult(params[4])
				.setStartYear(params[5])
				.setEndYear(params[6])
				.setRuntime(params[7])
				.setGenres(params[8]);
	}

}
