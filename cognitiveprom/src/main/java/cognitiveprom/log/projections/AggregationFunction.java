package cognitiveprom.log.projections;

import java.text.DecimalFormat;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class AggregationFunction {
	
	protected DescriptiveStatistics stat;
	protected DecimalFormat df;
	protected double noOfTraces;
	
	public AggregationFunction(int noOfTraces) {
		this.df = new DecimalFormat("#.###");
		this.noOfTraces = noOfTraces;
		reset();
	}
	
	public void addObservation(double n) {
		stat.addValue(n);
	}
	
	public Number getValue(AggregationFunctions value) {
		if (value == AggregationFunctions.SUM) {
			return stat.getSum();
		} else if (value == AggregationFunctions.MIN) {
			return stat.getMin();
		} else if (value == AggregationFunctions.MAX) {
			return stat.getMax();
		} else if (value == AggregationFunctions.MEAN_NO_MISSING_VALS) {
			return stat.getMean();
		} else if (value == AggregationFunctions.MEAN_ON_ALL_TRACES) {
			return stat.getSum() / noOfTraces;
		}
		return 0;
	}
	
	public String getStringValue(AggregationFunctions value) {
		return df.format(getValue(value));
	}
	
	public void reset() {
		stat = new DescriptiveStatistics();
	}
}
