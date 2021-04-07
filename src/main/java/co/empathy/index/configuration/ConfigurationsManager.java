package co.empathy.index.configuration;

import co.empathy.exceptions.InvalidIndexException;
import co.empathy.index.configuration.imdb.ImdbBasicsIndexConfiguration;
import co.empathy.index.configuration.test.TestBasicsIndexConfiguration;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.reactivex.annotations.NonNull;

import javax.inject.Named;
import javax.validation.constraints.NotNull;
import java.util.*;

@Factory
@Requires(beans = { ImdbBasicsIndexConfiguration.class, TestBasicsIndexConfiguration.class} )
public class ConfigurationsManager {

	private final Map<String, IndexConfiguration> configurations;
	private final Map<String, List<IndexConfiguration>> extensions;

	public ConfigurationsManager(
			@NonNull @Named("imdbbasics") IndexConfiguration imdb,
			@NonNull @Named("testbasics") IndexConfiguration test,
			@NonNull @Named("imdbratings") IndexConfiguration imdbRatings,
			@NonNull @Named("testratings") IndexConfiguration testRatings) {
		configurations = new HashMap<>();
		configurations.put("imdb", imdb);
		configurations.put("test", test);
		extensions = new HashMap<>();
		extensions.put("imdb", Arrays.asList(imdbRatings));
		extensions.put("test", Arrays.asList(testRatings));
	}

	/**
	 * @param key   Key of the configuration
	 * @return      Requested configuration
	 * @throws IllegalArgumentException if the requested configuration does not exists
	 */
	@NotNull
	public IndexConfiguration getConfiguration(String key) {
		IndexConfiguration configuration = configurations.get(key);
		if (configuration == null) {
			throw new InvalidIndexException("The index " + key + " does not exist");
		}
		return configuration;
	}

	/**
	 * @param key   Key of the extensions
	 * @return      Extension of the specified key, empty list if it does not have
	 */
	@NotNull
	public List<IndexConfiguration> getExtensions(String key) {
		var list = extensions.get(key);
		if (list == null) {
			return new ArrayList<>();
		}
		return list;
	}

}
