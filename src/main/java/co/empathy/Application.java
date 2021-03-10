package co.empathy;

import co.empathy.index.Indexer;
import co.empathy.index.configuration.TestIndexConfiguration;
import io.micronaut.context.BeanContext;
import io.micronaut.runtime.Micronaut;

import java.io.IOException;

public class Application {

	public static void main(String[] args) {
		Micronaut.run(Application.class, args);
		//indexImdb();
	}

	private static void indexImdb() {
		final BeanContext context = BeanContext.run();
		Indexer indexer = context.getBean(Indexer.class);
		// Uncomment to load the reduced database
		indexer.setConfiguration(context.getBean(TestIndexConfiguration.class));
		try {
			indexer.bulkIndexFile();
			//indexer.indexFile();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}