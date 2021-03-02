package co.empathy;

import co.empathy.index.ImdbIndexer;
import co.empathy.index.Indexer;
import io.micronaut.context.BeanContext;
import io.micronaut.runtime.Micronaut;

import java.io.IOException;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
        indexImdb();
    }

    private static void indexImdb() {
        final BeanContext context = BeanContext.run();
        Indexer indexer = context.getBean(Indexer.class);
        try {
            indexer.indexFile("src/main/resources/title.basics.tsv");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}