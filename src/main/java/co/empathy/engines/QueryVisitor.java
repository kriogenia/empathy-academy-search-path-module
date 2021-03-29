package co.empathy.engines;

import co.empathy.search.request.queries.PartialPlusPerfectQuery;
import org.elasticsearch.index.query.QueryBuilder;

import javax.validation.constraints.NotNull;

public interface QueryVisitor {

	@NotNull
	QueryBuilder transform(PartialPlusPerfectQuery query);

}
