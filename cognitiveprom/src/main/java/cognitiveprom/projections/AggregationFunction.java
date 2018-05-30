package cognitiveprom.projections;

import java.text.DecimalFormat;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class AggregationFunction {
	
	protected DescriptiveStatistics stat;
	protected DecimalFormat df;
	
	public AggregationFunction() {
		this.df = new DecimalFormat("#.###");
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
		} else if (value == AggregationFunctions.MEAN) {
			return stat.getMean();
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
