package cognitiveprom.tools;

import org.processmining.plugins.InductiveMiner.dfgOnly.Dfg;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;

public class DfgMinerResultOld {

	public IMLogInfo logInfo;
	public Dfg dfg;
	public Long maxAllowedToCut;
	
	public DfgMinerResultOld(IMLogInfo logInfo, Dfg dfg, Long maxAllowedToCut) {
		this.logInfo = logInfo;
		this.dfg = dfg;
		this.maxAllowedToCut = maxAllowedToCut;
	}
}
