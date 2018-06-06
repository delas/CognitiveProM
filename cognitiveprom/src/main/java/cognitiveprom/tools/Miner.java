package cognitiveprom.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.causalactivitygraph.models.CausalActivityGraph;
import org.processmining.causalactivitygraphcreator.algorithms.DiscoverCausalActivityGraphAlgorithm;
import org.processmining.causalactivitygraphcreator.parameters.DiscoverCausalActivityGraphParameters;
import org.processmining.contexts.cli.CLIContext;
import org.processmining.contexts.cli.CLIPluginContext;
import org.processmining.dataawarecnetminer.model.EventRelationStorage;

public class Miner {

//	public static DfgMinerResult mineDfg(XLog log) {
//		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
//		EventRelationStorage eventStorage = EventRelationStorage.Factory.createEventRelations(
//				log,
//				new XEventNameClassifier(),
//				executorService);
//		
//		Map<XEventClass, Long> maxConn = new HashMap<XEventClass, Long>();
//		for (Relation r : eventStorage.getDirectlyFollowsRelations()) {
//			XEventClass source = r.getSource();
//			XEventClass target = r.getTarget();
//			if (	!source.equals(target) &&
//					!source.equals(eventStorage.getStartEventClass()) &&
//					!target.equals(eventStorage.getEndEventClass())) {
//				maxConn.put(source,
//						Math.max(
//								eventStorage.countDirectlyFollows(r),
//								(maxConn.containsKey(source)? maxConn.get(source) : 0)));
//				maxConn.put(target,
//						Math.max(
//								eventStorage.countDirectlyFollows(r),
//								(maxConn.containsKey(target)? maxConn.get(target) : 0)));
//			}
//		}
//		List<Long> maxConnList = new ArrayList<Long>(maxConn.values());
//		Collections.sort(maxConnList);
//
//		DfgMinerResult dfg = new DfgMinerResult();
//		dfg.ers = eventStorage;
//		dfg.maxAllowedToCut = (maxConn.size() > 0)? maxConnList.get(0) : 0;
//		
//		return dfg;
//	}
	
//	public static DfgMinerResultOld mineDfgOld(XLog log) {
//		IMLog logIM = new IMLogImpl(log, new XEventNameClassifier(), new XLifeCycleClassifierIgnore());
//		IMLogInfo logInfo = IMLog2IMLogInfoDefault.log2logInfo(logIM);
//		Dfg dfg = logInfo.getDfg();
//		
//		Map<XEventClass, Long> maxConn = new HashMap<XEventClass, Long>();
//		for (Long edge : dfg.getDirectlyFollowsEdges()) {
//			XEventClass source = dfg.getDirectlyFollowsEdgeSource(edge);
//			XEventClass target = dfg.getDirectlyFollowsEdgeTarget(edge);
//			if (!source.equals(target)) {
//				maxConn.put(source,
//						Math.max(
//								dfg.getDirectlyFollowsEdgeCardinality(edge),
//								(maxConn.containsKey(source)? maxConn.get(source) : 0)));
//				maxConn.put(target,
//						Math.max(
//								dfg.getDirectlyFollowsEdgeCardinality(edge),
//								(maxConn.containsKey(target)? maxConn.get(target) : 0)));
//			}
//		}
//		
//		List<Long> maxConnList = new ArrayList<Long>(maxConn.values());
//		Collections.sort(maxConnList);
//		Long maxAllowedToCut = maxConnList.get(0);
//		
//		return new DfgMinerResultOld(logInfo, dfg, maxAllowedToCut);
//	}
	
	public static CausalActivityGraph mineCag(XLog log) {
		CLIContext context = new CLIContext();
		CLIPluginContext pluginContext = new CLIPluginContext(context, "test");

		DiscoverCausalActivityGraphParameters parameters = new DiscoverCausalActivityGraphParameters(log);
		parameters.setMiner("Heuristics");
		parameters.setZeroValue(0.5);
		DiscoverCausalActivityGraphAlgorithm miner = new DiscoverCausalActivityGraphAlgorithm();
		return miner.apply(pluginContext, log, parameters);
		
		
	}
}
