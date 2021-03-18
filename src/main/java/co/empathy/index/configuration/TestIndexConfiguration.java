package co.empathy.index.configuration;

import co.empathy.engines.EEngine;

import javax.inject.Singleton;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Index configuration to load a portion of the database with
 * a test configuration
 */
@Singleton
public class TestIndexConfiguration implements IndexConfiguration {

	// TODO move configuration to resource files
	// TODO refactor general test to ImdbTest

	public static final String INDEX_KEY = "test";
	public static final String BASE_PATH = "src/test/resources/test";
	public static final String FILE_PATH = BASE_PATH + "/test.tsv";
	public static final int BULK_SIZE = 5000;
	public static final int TOTAL_BULKS = 162622 / BULK_SIZE;

	@Override
	@NotEmpty
	public String getKey() {
		return INDEX_KEY;
	}

	@Override
	@NotEmpty
	public String getFilePath() {
		return FILE_PATH;
	}

	@Override
	@Positive
	public int getBulkSize() {
		return BULK_SIZE;
	}

	@Override
	@Positive
	public int getTotalBulks() {
		return TOTAL_BULKS;
	}

	@Override
	@NotEmpty
	public String getSource(EEngine requesterInfo) throws IOException {
		String path = BASE_PATH + "/index.json";
		return new String(Files.readAllBytes(Paths.get(path)));
	}
}
