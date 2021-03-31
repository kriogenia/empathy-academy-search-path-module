package co.empathy.index;

import co.empathy.engines.MockSearchEngine;
import co.empathy.index.configuration.test.TestBasicsIndexConfiguration;
import co.empathy.index.configuration.test.TestRatingsIndexConfiguration;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import java.util.Collections;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseIndexerTest {

	@Inject
	BaseIndexer indexer;

	@Inject
	MockSearchEngine mockEngine;

	@Inject
	TestBasicsIndexConfiguration testConfig;

	@Inject
	TestRatingsIndexConfiguration testExtension;

	@BeforeAll
	public void mockIndexer() {
		indexer.setEngine(mockEngine);
		indexer.setConfiguration(testConfig);
		indexer.setExtensions(Collections.singletonList(testExtension));
	}

	@Test
	public void testIndex() {

	}

}
