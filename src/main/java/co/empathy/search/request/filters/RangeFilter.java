package co.empathy.search.request.filters;

import co.empathy.engines.FilterVisitor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RangeFilter extends BaseRequestFilter {

	@NotEmpty
	private final String from;

	@NotEmpty
	private final String to;

	@NotEmpty
	private final String format;

	public RangeFilter(@NotEmpty String field, @NotNull String range) {
		super(field);
		var segments = range.split("/");
		if (segments.length != 2) {
			throw new IllegalArgumentException("Ranges of dates must have two edges");
		}
		this.from = segments[0];
		this.to = segments[1];
		this.format = "YYYY";
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	public String getFormat() {
		return format;
	}

	@Override
	public @NotNull Object accept(FilterVisitor visitor) {
		return visitor.transform(this);
	}
}
