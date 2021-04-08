package co.empathy.index.configuration;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class ConfigurationsManagerTest {

	@Inject
	ConfigurationsManager manager;

	@Test
	public void testGetConfiguration() {
		// Imdb
		var imdb = manager.getConfiguration("imdb");
		assertEquals("imdb", imdb.getKey());
		// Test
		var test = manager.getConfiguration("test");
		assertEquals("test", test.getKey());
		// Invalid
		var exception = assertThrows(IllegalArgumentException.class,
				() -> manager.getConfiguration("invalid"));
		assertEquals("The index invalid does not exist", exception.getMessage());
	}

	@Test
	public void testGetExtensions() {
		// Imdb
		var imdb = manager.getExtensions("imdb");
		assertEquals(1, imdb.size());
		assertEquals("imdb", imdb.get(0).getKey());
		// Test
		var test = manager.getExtensions("test");
		assertEquals(1, test.size());
		assertEquals("test", test.get(0).getKey());
		// Invalid
		var invalid = manager.getExtensions("invalid");
		assertEquals(0, invalid.size());
	}

}
