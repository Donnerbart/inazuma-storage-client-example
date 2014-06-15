package stats;

import java.util.concurrent.TimeUnit;

public class CustomStatisticValue<VALUE> extends AbstractStatisticValue<VALUE>
{
	private final ValueCollector<VALUE> valueCollector;

	public CustomStatisticValue(final String group, final String name, final long duration, final TimeUnit timeUnit, final ValueCollector<VALUE> valueCollector)
	{
		super(group, name, duration, timeUnit);
		this.valueCollector = valueCollector;
		stats.add(Stat.SUM);
		swapValue();
	}

	public CustomStatisticValue(final String group, final String name, final ValueCollector<VALUE> valueCollector)
	{
		this(group, name, DEFAULT_INTERVAL, DEFAULT_UNIT, valueCollector);
	}

	public CustomStatisticValue(final String name, final long duration, final TimeUnit timeUnit, final ValueCollector<VALUE> valueCollector)
	{
		this(null, name, duration, timeUnit, valueCollector);
	}

	public CustomStatisticValue(final String name, final ValueCollector<VALUE> valueCollector)
	{
		this(null, name, DEFAULT_INTERVAL, DEFAULT_UNIT, valueCollector);
	}

	@Override
	protected String getType()
	{
		return valueCollector.getType();
	}

	@Override
	protected void swapValue()
	{
		lastTimeRangedValueDefault = valueCollector.collectValue();
	}

	public static interface ValueCollector<VALUE>
	{
		VALUE collectValue();

		String getType();
	}
}
