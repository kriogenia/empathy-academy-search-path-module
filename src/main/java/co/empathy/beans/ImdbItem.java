package co.empathy.beans;

import co.empathy.index.Indexable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * Indexable POJO class to manage IMDB entries
 */
@JsonPropertyOrder({ImdbItem.ID, ImdbItem.TITLE, ImdbItem.GENRES,
		ImdbItem.TYPE, ImdbItem.START, ImdbItem.END})
@JsonIgnoreProperties({ImdbItem.ORIGINAL_TITLE, ImdbItem.IS_ADULT, ImdbItem.RUNTIME_MINUTES})
public class ImdbItem implements Indexable {

	public static final String ID = "id";
	public static final String TYPE = "type";
	public static final String TITLE = "title";
	public static final String ORIGINAL_TITLE = "original_title";
	public static final String IS_ADULT = "is_adult";
	public static final String START = "start_year";
	public static final String END = "end_year";
	public static final String RUNTIME_MINUTES = "runtime_minutes";
	public static final String GENRES = "genres";

	@JsonProperty(ID)
	private String id;

	@JsonProperty(TYPE)
	private String titleType;

	@JsonProperty(TITLE)
	private String primaryTitle;

	@JsonProperty(ORIGINAL_TITLE)
	private String originalTitle;

	@JsonProperty(IS_ADULT)
	private String isAdult;

	@JsonProperty(START)
	private String startYear;

	@JsonProperty(END)
	private String endYear;

	@JsonProperty(RUNTIME_MINUTES)
	private String runtime;

	@JsonProperty(GENRES)
	private String[] genres;

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
	 * @return 		the item modified
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
	 * @return 		the item modified
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
	 * @return 		the item modified
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
	 * @return 		the item modified
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
	 * @return 		the item modified
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
	 * @return 		the item modified
	 */
	public ImdbItem setStartYear(String startYear) {
		this.startYear = startYear;
		return this;
	}

	/**
	 * @return	if it exists, TV Series end year, '\N' for all other title types
	 */
	public String getEndYear() {
		return endYear;
	}

	/**
	 * @param endYear	TV Series end year, '\N' for all other title types
	 * @return 			the item modified
	 */
	public ImdbItem setEndYear(String endYear) {
		this.endYear = endYear.equals("\\N") ? null : endYear;
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
	 * @return 		the item modified
	 */
	public ImdbItem setRuntime(String runtime) {
		this.runtime = runtime;
		return this;
	}

	/**
	 * @return	up to three genres associated with the title
	 */
	public String[] getGenres() {
		return genres;
	}

	/**
	 * @param genres	up to three genres associated with the title
	 * @return 			the item modified
	 * @throws IllegalArgumentException	if there's less than one genre or more than 3
	 */
	public ImdbItem setGenres(String[] genres) {
		if (genres.length < 1 || genres.length > 3) {
			throw new IllegalArgumentException("Invalid number of genres");
		}
		this.genres = genres;
		return this;
	}

	@Override
	public Map<String, Object> toJsonMap() {
		String endYear = this.endYear == null ? "\\N" : this.endYear;
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put(TYPE, titleType);
		jsonMap.put(TITLE, primaryTitle);
		jsonMap.put(ORIGINAL_TITLE, originalTitle);
		jsonMap.put(IS_ADULT, isAdult);
		jsonMap.put(START, startYear);
		jsonMap.put(END, endYear);
		jsonMap.put(RUNTIME_MINUTES, runtime);
		jsonMap.put(GENRES, genres);
		return jsonMap;
	}

	/**
	 * Builds an ImdbItem from a single line string
	 * @param line	string with the properties separated by tabs
	 * @return		built ImdbItem
	 * @throws IllegalArgumentException	if the string doesn't have exactly nine properties
	 */
	public static ImdbItem buildFromString(String line) {
		String[] params =  line.split("\t");
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
				.parseGenres(params[8]);
	}

	/**
	 * @param line		string with up to three genres associated with the title
	 * @return 			the item modified
	 * @throws IllegalArgumentException	if there's less than one gender or more than three
	 */
	public ImdbItem parseGenres(String line) {
		if (line.length() == 0) {
			throw new IllegalArgumentException("Invalid number of genres");
		}
		String[] genres = line.split(",");
		return this.setGenres(genres);
	}

}
