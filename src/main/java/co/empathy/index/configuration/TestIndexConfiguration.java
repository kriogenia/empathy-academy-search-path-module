package co.empathy.index.configuration;

import co.empathy.engines.EEngine;
import io.micronaut.context.annotation.ConfigurationProperties;

import javax.inject.Singleton;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Index configuration to load a portion of the dataset with
 * a test configuration
 */
@ConfigurationProperties("reducedImdb")
public class TestIndexConfiguration implements IndexConfiguration {

	@NotEmpty
	private String key;

	@NotEmpty
	private String basePath;

	@NotEmpty
	private String fileName;

	@NotEmpty
	private String indexName;

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
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
		return basePath + fileName;
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
		String path = basePath + indexName;
		return new String(Files.readAllBytes(Paths.get(path)));
	}

}
