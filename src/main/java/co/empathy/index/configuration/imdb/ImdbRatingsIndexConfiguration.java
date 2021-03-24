package co.empathy.index.configuration.imdb;

import co.empathy.common.ImdbRating;
import co.empathy.engines.EEngine;
import co.empathy.index.Indexable;
import co.empathy.index.configuration.IndexConfiguration;
import io.micronaut.context.annotation.ConfigurationProperties;

import javax.validation.constraints.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

/**
 * Configuration to load the IMDB dataset
 */
@ConfigurationProperties("imdb.ratings")
public class ImdbRatingsIndexConfiguration implements IndexConfiguration {

	@NotEmpty
	private String key;

	@NotEmpty
	private String basePath;

	@NotEmpty
	private String file;

	@NotEmpty
	private String indexConfig;

	@Min(100)
	private int bulk;

	@Positive
	private int total;

	@Override
	@NotEmpty
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getIndexConfig() {
		return indexConfig;
	}

	public void setIndexConfig(String indexConfig) {
		this.indexConfig = indexConfig;
	}

	public int getBulk() {
		return bulk;
	}

	public void setBulk(int bulk) {
		this.bulk = bulk;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	@NotEmpty
	public String getFilePath() {
		return basePath + file;
	}

	@Override
	public Function<String, Indexable> getBuilder() {
		return ImdbRating::buildFromString;
	}

	@Override
	@Positive
	public int getBulkSize() {
		return bulk;
	}

	@Override
	@Positive
	public int getTotalBulks() {
		return total/bulk;
	}

	@Override
	@NotEmpty
	public String getSource(EEngine requesterInfo) throws IOException {
		String path = basePath + requesterInfo.getKey() + indexConfig;
		return new String(Files.readAllBytes(Paths.get(path)));
	}
}
