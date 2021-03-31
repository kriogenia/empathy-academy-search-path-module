package co.empathy.index;

import co.empathy.engines.MockSearchEngine;
import co.empathy.index.configuration.test.TestBasicsIndexConfiguration;
import co.empathy.index.configuration.test.TestRatingsIndexConfiguration;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.apache.lucene.index.IndexNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseIndexerTest {

	private BaseIndexer indexer;

	@Inject
	MockSearchEngine mockEngine;

	@Inject
	TestBasicsIndexConfiguration testConfig;

	@Inject
	TestRatingsIndexConfiguration testExtension;

	@BeforeAll
	public void mockIndexer() {
		indexer = new BaseIndexer(mockEngine, testConfig);
		indexer.setExtensions(Collections.singletonList(testExtension));
	}

	@BeforeEach
	public void resetEngine() {
		mockEngine.reset();
	}

	@Test
	public void testBulkIndex() throws IOException {
		assertFalse(mockEngine.hasIndex("test"));
		indexer.bulkIndex();
		assertTrue(mockEngine.hasIndex("test"));
		assertEquals(22, mockEngine.getInserts("test"));
	}

	@Test
	public void testBulkExtend() throws IOException {
		//  Index does not exist
		var exception = assertThrows(IndexNotFoundException.class,
				() -> indexer.bulkExtend());
		assertEquals("The specified index does not exists, it can't be updated", exception.getMessage());
		// Build index
		indexer.bulkIndex();
		assertTrue(mockEngine.hasIndex("test"));
		// Extend the index
		indexer.bulkExtend();
		assertEquals(16, mockEngine.getUpdates("test"));
	}

	@Test
	public void testDelete() throws IOException {
		// Index does not exist
		assertFalse(indexer.delete());
		// Build index
		indexer.bulkIndex();
		assertTrue(mockEngine.hasIndex("test"));
		// Index deleted
		assertTrue(indexer.delete());
		assertFalse(indexer.delete());
	}

}
