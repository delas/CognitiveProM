package cognitiveprom.projections;

import java.text.DecimalFormat;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public abstract class AggregationFunction implements Cloneable {
	
	public static AggregationFunction construct(AggregationFunctions function) {
		if (function == AggregationFunctions.SUM) {
			return new AggregationFunction("Sum", "#.###") {
				@Override
				public Number getValue() {
					return adapt(stat.getSum());
				}
			};
		} else if (function == AggregationFunctions.MEAN) {
			return new AggregationFunction("Mean", "#.###") {
				@Override
				public Number getValue() {
					return adapt(stat.getMean());
				}
			};
		} else if (function == AggregationFunctions.MIN) {
			return new AggregationFunction("Min", "#.###") {
				@Override
				public Number getValue() {
					return adapt(stat.getMin());
				}
			};
		} else if (function == AggregationFunctions.MAX) {
			return new AggregationFunction("Max", "#.###") {
				@Override
				public Number getValue() {
					return adapt(stat.getMax());
				}
			};
		} else if (function == AggregationFunctions.MEDIAN) {
			return new AggregationFunction("Median", "#.###") {
				@Override
				public Number getValue() {
					return adapt(stat.getPercentile(50));
				}
			};
		}
		return null;
	}
	
	private static Number adapt(Number number) {
//		if ((long) number.doubleValue() == number.doubleValue()) {
//			return number.longValue();
//		}
		return number;
	}
	
	private String name;
	protected DescriptiveStatistics stat;
	protected DecimalFormat df;
	
	private AggregationFunction(String name, String format) {
		this.name = name;
		this.df = new DecimalFormat(format);
		reset();
	}
	
	public void addObservation(double n) {
		stat.addValue(n);
	}
	
	public abstract Number getValue();
	
	public String getStringValue() {
		return df.format(getValue());
	}
	
	public String getName() {
		return name;
	}
	
	public void reset() {
		stat = new DescriptiveStatistics();
	}
}
