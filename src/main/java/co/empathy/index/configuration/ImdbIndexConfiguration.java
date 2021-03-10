package co.empathy.index.configuration;

import co.empathy.engines.EEngine;

import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Singleton
public class ImdbIndexConfiguration implements IndexConfiguration {

	// TODO move configuration to resource files

	public static final String INDEX_KEY = "imdb";
	public static final String BASE_PATH = "src/main/resources/imdb";
	public static final String FILE_PATH = BASE_PATH + "/title.basics.tsv";
	public static final int BULK_SIZE = 5000;
	public static final int TOTAL_BULKS = 7660000 / BULK_SIZE;

	@Override
	public String getKey() {
		return INDEX_KEY;
	}

	@Override
	public String getFilePath() {
		return FILE_PATH;
	}

	@Override
	public int getBulkSize() {
		return BULK_SIZE;
	}

	@Override
	public int getTotalBulks() {
		return TOTAL_BULKS;
	}

	@Override
	public String getSource(EEngine requesterInfo) throws IOException {
		String path = BASE_PATH + "/" + requesterInfo.getKey() + "_index_config.json";
		return new String(Files.readAllBytes(Paths.get(path)));
	}
}
