package co.empathy.engines.elastic;

import co.empathy.engines.QueryVisitor;
import co.empathy.search.request.queries.DisjunctionMaxQuery;
import co.empathy.search.request.queries.PartialPlusPerfectQuery;
import co.empathy.search.request.queries.RequestQuery;
import io.micronaut.context.annotation.Prototype;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import javax.validation.constraints.NotNull;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Prototype
public class ElasticQueryVisitor implements QueryVisitor {

	@Override
	@NotNull
	public QueryBuilder transform(@NotNull DisjunctionMaxQuery query) {
		var dmQuery = QueryBuilders.disMaxQuery();
		query.getQueries().forEach((q) -> dmQuery.add((QueryBuilder) q.accept(this)));
		return dmQuery;
	}

	@Override
	@NotNull
	public QueryBuilder transform(@NotNull PartialPlusPerfectQuery query) {
		var field = query.getField();
		var text = query.getText();
		var boolQuery = QueryBuilders.boolQuery();
		var partial = matchQuery(field + ".partial", text);
		partial.minimumShouldMatch("0<-50% 3<-75%");
		boolQuery.should(partial);
		boolQuery.should(matchPhraseQuery(field + ".perfect", text));
		return boolQuery;
	}

}
