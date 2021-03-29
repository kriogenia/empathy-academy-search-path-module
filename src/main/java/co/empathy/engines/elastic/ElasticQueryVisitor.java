package co.empathy.engines.elastic;

import co.empathy.engines.QueryVisitor;
import co.empathy.search.request.queries.DisjunctionMaxQuery;
import co.empathy.search.request.queries.PartialPlusPerfectQuery;
import io.micronaut.context.annotation.Prototype;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import javax.validation.constraints.NotNull;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Prototype
public class ElasticQueryVisitor implements QueryVisitor {

	@Override
	public @NotNull QueryBuilder transform(DisjunctionMaxQuery query) {
ga
		return null;
	}

	@Override
	public @NotNull QueryBuilder transform(PartialPlusPerfectQuery query) {
		var field = query.getField();
		var text = query.getText();
		var boolQuery = QueryBuilders.boolQuery();
		var partial = matchQuery(field + ".partial", text);
		partial.minimumShouldMatch("3<-75%");
		boolQuery.should(partial);
		boolQuery.should(matchPhraseQuery(field + ".perfect", text));
		return boolQuery;
	}

}
