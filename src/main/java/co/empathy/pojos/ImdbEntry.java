package co.empathy.pojos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Common POJO class to manage IMDB entries
 */
public class ImdbEntry {

	private String tconst;
	private String titleType;
	private String primaryTitle;
	private String originalTitle;
	private int isAdult;
	private int startYear;
	private int endYear;
	private int runtimeMinutes;
	private String genres;


	@JsonCreator
	public ImdbEntry(
			@JsonProperty("tconst") String tconst,
			@JsonProperty("titleType") String titleType,
			@JsonProperty("primaryTitle") String primaryTitle,
			@JsonProperty("originalType") String originalTitle,
			@JsonProperty("isAdult") String isAdult,
			@JsonProperty("startYear") String startYear,
			@JsonProperty("endYear") String endYear,
			@JsonProperty("runtimeMinutes") String runtimeMinutes,
			@JsonProperty("genres") String genres) {
		this.tconst = tconst;
		this.titleType = titleType;
		this.primaryTitle = primaryTitle;
		this.originalTitle = originalTitle;
		this.isAdult = Integer.parseInt(isAdult);
		this.startYear = Integer.parseInt(startYear);
		this.endYear = Integer.parseInt(endYear);
		this.runtimeMinutes = Integer.parseInt(runtimeMinutes);
		this.genres = genres;
	}

}
