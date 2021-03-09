package co.empathy.index.configuration;

import javax.inject.Singleton;

@Singleton
public class ImdbIndexConfiguration implements IndexConfiguration {

	public static final String INDEX_KEY = "imdb";
	public static final String FILE_PATH = "src/main/resources/imdb/title.basics.tsv";
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
}
