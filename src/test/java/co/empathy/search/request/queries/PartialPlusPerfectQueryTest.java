package co.empathy.search.request.queries;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class PartialPlusPerfectQueryTest {

	@Test
	public void testPartialPlusPerfectQuery() {
		var query = new PartialPlusPerfectQuery("field", "text");
		assertEquals("field", query.getField());
		assertEquals("text", query.getText());
	}

}
