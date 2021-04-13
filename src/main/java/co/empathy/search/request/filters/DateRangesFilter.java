package co.empathy.search.request.filters;

import co.empathy.engines.FilterVisitor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class DateRangesFilter extends BaseRequestFilter {

	@NotEmpty
	private final List<Range> ranges;

	@NotEmpty
	private final String format;

	public DateRangesFilter(@NotEmpty String field, @NotNull String ranges) {
		super(field);
		this.ranges = new ArrayList<>();
		for (var range : ranges.split(",")) {
			var segments = range.split("/");
			if (segments.length != 2) {
				throw new IllegalArgumentException("Invalid range [" + range +
						"] - Ranges of dates must have two edges");
			}
			this.ranges.add(new Range(segments[0], segments[1]));
		}
		this.format = "year";
	}

	@NotEmpty
	public List<Range> getRanges() {
		return ranges;
	}

	@NotEmpty
	public String getFormat() {
		return format;
	}

	@Override
	public @NotNull Object accept(@NotNull FilterVisitor visitor) {
		return visitor.transform(this);
	}

	public static class Range {

		@NotEmpty
		public final String from;

		@NotEmpty
		public final String to;

		public Range(@NotEmpty String from, @NotEmpty String to) {
			if (Integer.parseInt(from) > Integer.parseInt(to)) {
				throw new IllegalArgumentException("Invalid range [" +
						from + "/" + to +
						"] - Start date can not be after the end date");
			}
			this.from = from;
			this.to = to;
		}

	}
}
