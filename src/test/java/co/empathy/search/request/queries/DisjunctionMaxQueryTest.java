package co.empathy.search.request.queries;

import co.empathy.engines.QueryVisitor;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotNull;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class DisjunctionMaxQueryTest {

	@Test
	public void testDisjunctionMaxQuery() {
		var query = new DisjunctionMaxQuery();
		query.add(new RequestQuery() {
			@Override
			public @NotNull Object accept(@NotNull QueryVisitor visitor) {
				return null;
			}
		});
		query.add(new RequestQuery() {
			@Override
			public @NotNull Object accept(@NotNull QueryVisitor visitor) {
				return null;
			}
		});
		assertEquals(2, query.getQueries().size());
	}

}
