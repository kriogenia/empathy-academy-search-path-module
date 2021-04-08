package co.empathy.index.configuration.test;

import co.empathy.common.ImdbItem;
import co.empathy.engines.EEngine;
import co.empathy.index.Indexable;
import co.empathy.index.configuration.IndexConfiguration;
import io.micronaut.context.annotation.ConfigurationProperties;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

/**
 * Index configuration to load a portion of the dataset with
 * a test configuration
 */
@ConfigurationProperties("reducedImdb.basics")
public class TestBasicsIndexConfiguration implements IndexConfiguration {

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
		return ImdbItem::buildFromString;
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
		String path = basePath + indexConfig;
		return new String(Files.readAllBytes(Paths.get(path)));
	}

}
