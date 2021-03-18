package co.empathy.common;

import co.empathy.index.Indexable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

	@NotNull(message = "ID cannot be null")
	@JsonProperty(ID)
	private String id;

	@NotNull(message = "Title type cannot be null")
	@JsonProperty(TYPE)
	private String titleType;

	@NotNull(message = "The item's primary title is required")
	@JsonProperty(TITLE)
	private String primaryTitle;

	@Nullable
	@JsonProperty(ORIGINAL_TITLE)
	private String originalTitle;

	@Nullable
	@JsonProperty(IS_ADULT)
	private String isAdult;

	@NotNull(message = "The item's start year is required")
	@JsonProperty(START)
	private String startYear;

	@Nullable
	@JsonProperty(END)
	private String endYear;

	@Nullable
	@JsonProperty(RUNTIME_MINUTES)
	private String runtime;

	@NotEmpty(message = "The item must have at least one genre")
	@JsonProperty(GENRES)
	private String[] genres;

	/**
	 * Empty constructor of the JavaBean
	 */
	public ImdbItem() {}

	/**i
	 * @return	alphanumeric unique identifier of the title
	 */
	@Nullable
	public String getId() {
		return id;
	}

	/**
	 * @param id	alphanumeric unique identifier of the title
	 * @return 		the item modified
	 */
	@NotNull
	public ImdbItem setId(@NotNull String id) {
		this.id = id;
		return this;
	}

	/**
	 * @return	the type/format of the title (e.g. movie, short, tv series...)
	 */
	@Nullable
	public String getTitleType() {
		return titleType;
	}

	/**
	 * @param titleType	the type/format of the title (e.g. movie, short, tv series...)
	 * @return 		the item modified
	 */
	@NotNull
	public ImdbItem setTitleType(@NotNull String titleType) {
		this.titleType = titleType;
		return this;
	}

	/**
	 * @return	the more popular title / title used on promotional materials
	 */
	@Nullable
	public String getPrimaryTitle() {
		return primaryTitle;
	}


	/**
	 * @param primaryTitle	the more popular title / title used on promotional materials
	 * @return 		the item modified
	 */
	@NotNull
	public ImdbItem setPrimaryTitle(@NotNull String primaryTitle) {
		this.primaryTitle = primaryTitle;
		return this;
	}

	/**
	 * @return	original title, in the original language
	 */
	@Nullable
	public String getOriginalTitle() {
		return originalTitle;
	}

	/**
	 * @param originalTitle	original title, in the original language
	 * @return 		the item modified
	 */
	@NotNull
	public ImdbItem setOriginalTitle(@NotNull String originalTitle) {
		this.originalTitle = originalTitle;
		return this;
	}

	/**
	 * @return	O: non-adult title; 1: original title
	 */
	@Nullable
	public String getIsAdult() {
		return isAdult;
	}

	/**
	 * @param isAdult	O: non-adult title; 1: original title
	 * @return 			the item modified
	 */
	@NotNull
	public ImdbItem setIsAdult(@NotNull String isAdult) {
		this.isAdult = isAdult;
		return this;
	}

	/**
	 * @return	release year of a title (YYYY)
	 */
	@Nullable
	public String getStartYear() {
		return startYear;
	}

	/**
	 * @param startYear    release year of a title (YYYY)
	 * @return 		the item modified
	 */
	@NotNull
	public ImdbItem setStartYear(@NotNull String startYear) {
		this.startYear = startYear;
		return this;
	}

	/**
	 * @return	if it exists, TV Series end year, '\N' for all other title types
	 */
	@Nullable
	public String getEndYear() {
		return endYear;
	}

	/**
	 * @param endYear	TV Series end year, '\N' for all other title types
	 * @return 			the item modified
	 */
	@NotNull
	public ImdbItem setEndYear(@Nullable String endYear) {
		this.endYear = endYear == null || endYear.equals("\\N") ? null : endYear;
		return this;
	}

	/**
	 * @return	primary runtime of the title, in minutes
	 */
	@Nullable
	public String getRuntime() {
		return runtime;
	}

	/**
	 * @param runtime	primary runtime of the title, in minutes
	 * @return 		the item modified
	 */
	@NotNull
	public ImdbItem setRuntime(@NotNull String runtime) {
		this.runtime = runtime;
		return this;
	}

	/**
	 * @return	up to three genres associated with the title
	 */
	@Nullable
	public String[] getGenres() {
		return genres;
	}

	/**
	 * @param genres	up to three genres associated with the title
	 * @return 			the item modified
	 * @throws IllegalArgumentException	if there's less than one genre or more than 3
	 */
	@NotNull
	public ImdbItem setGenres(@NotNull String[] genres) {
		if (genres.length < 1 || genres.length > 3) {
			throw new IllegalArgumentException("Invalid number of genres");
		}
		this.genres = genres;
		return this;
	}

	@Override
	@NotEmpty
	public Map<String, Object> toJsonMap() {
		//String endYear = this.endYear == null ? "\\N" : this.endYear;
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
	@NotNull
	public static ImdbItem buildFromString(@NotNull String line) {
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
	@NotNull
	public ImdbItem parseGenres(@NotNull String line) {
		if (line.length() == 0) {
			throw new IllegalArgumentException("Invalid number of genres");
		}
		String[] genres = line.split(",");
		return this.setGenres(genres);
	}

}