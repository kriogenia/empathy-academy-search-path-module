package co.empathy.index.configuration;

import co.empathy.engines.SearchEngine;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.reactivex.annotations.NonNull;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Factory
@Requires(beans = { ImdbIndexConfiguration.class, TestIndexConfiguration.class} )
public class ConfigurationsManager {

	private final Map<String, IndexConfiguration> configurations;

	public ConfigurationsManager(
			@NonNull @Named("imdb") IndexConfiguration imdb,
			@NonNull @Named("test") IndexConfiguration test) {
		configurations = new HashMap<>();
		configurations.put("imdb", imdb);
		configurations.put("test", test);
	}

	public IndexConfiguration getConfiguration(String key) {
		IndexConfiguration configuration = configurations.get(key);
		if (configuration == null) {
			throw new IllegalArgumentException("The specified index does not exists");
		}
		return configuration;
	}

}
