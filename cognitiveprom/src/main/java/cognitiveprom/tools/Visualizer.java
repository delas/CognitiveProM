package cognitiveprom.tools;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.causalactivitygraph.models.CausalActivityGraph;
import org.processmining.dataawarecnetminer.model.EventRelationStorage;
import org.processmining.models.causalgraph.Relation;
import org.processmining.plugins.InductiveMiner.dfgOnly.Dfg;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotCluster;
import org.processmining.plugins.graphviz.dot.DotEdge;
import org.processmining.plugins.graphviz.dot.DotNode;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset.Entry;

import cognitiveprom.graphical.CognitiveDotEdge;
import cognitiveprom.graphical.CognitiveDotEndNode;
import cognitiveprom.graphical.CognitiveDotNode;
import cognitiveprom.graphical.CognitiveDotStartNode;

public class Visualizer {
	
	private static DecimalFormat df = new DecimalFormat("#.###");
	
	public static Dot visualize(DfgMinerResult dfgResult, double threshold) {
		EventRelationStorage eventRelations = dfgResult.ers;
		Long maxAllowedToCut = dfgResult.maxAllowedToCut;
		
		double mostOccurringActivity = Utils.getMostFrequencActivity(eventRelations);
		double mostOccurringRelation = Utils.getMostFrequentRelation(eventRelations);
		double mostOccurringRelationStart = Utils.getMostFrequentRelationStart(eventRelations);
		double mostOccurringRelationEnd = Utils.getMostFrequentRelationEnd(eventRelations);
		
		Dot dot = new Dot();
		dot.setOption("outputorder", "edgesfirst");
		
		Map<XEventClass, DotNode> mapNodes = new HashMap<XEventClass, DotNode>();
		
		// adding nodes
		DotCluster s = dot.addCluster();
		DotCluster n = dot.addCluster();
		DotCluster e = dot.addCluster();
		
		s.setOption("style", "invis");
		n.setOption("style", "invis");
		e.setOption("style", "invis");
		
		Set<XEventClass> activities = Utils.getActivities(eventRelations);
		for(XEventClass act : activities) {
			DotNode node = null;
			if (act.equals(eventRelations.getStartEventClass())) {
				node = new CognitiveDotStartNode();
				s.addNode(node);
			} else if (act.equals(eventRelations.getEndEventClass())) {
				node = new CognitiveDotEndNode();
				e.addNode(node);
			} else {
				long frequency = eventRelations.countOccurence(act);
				node = new CognitiveDotNode(
						act.toString(),
						Long.toString(frequency),
						frequency / mostOccurringActivity);
				n.addNode(node);
			}
			mapNodes.put(act, node);
		}

		// adding relations
		for (Entry<Relation> entry : eventRelations.getDirectlyFollowsRelations().entrySet()) {
			Relation relation = entry.getElement();
			XEventClass source = relation.getSource();
			XEventClass target = relation.getTarget();
			
			long relationFrequency = eventRelations.countDirectlyFollows(relation);
			if (source.equals(eventRelations.getStartEventClass())) {
				
				// relations from start event
				if (relationFrequency >= (mostOccurringRelationStart * threshold)) {
					CognitiveDotEdge dotEdge = new CognitiveDotEdge(mapNodes.get(source), mapNodes.get(target), Long.toString(relationFrequency), null);
					dot.addEdge(dotEdge);
				}
				DotEdge invisibleEdge = dot.addEdge(mapNodes.get(source), mapNodes.get(target));
				invisibleEdge.setOption("style", "invisible");
				invisibleEdge.setOption("arrowhead", "none");
				dot.addEdge(invisibleEdge);
				
			} else if (target.equals(eventRelations.getEndEventClass())) {
				
				// relation to end event
				if (relationFrequency >= (mostOccurringRelationEnd * threshold)) {
					CognitiveDotEdge dotEdge = new CognitiveDotEdge(mapNodes.get(source), mapNodes.get(target), Long.toString(relationFrequency), null);
					dot.addEdge(dotEdge);
				}
				DotEdge invisibleEdge = dot.addEdge(mapNodes.get(source), mapNodes.get(target));
				invisibleEdge.setOption("style", "invisible");
				invisibleEdge.setOption("arrowhead", "none");
				dot.addEdge(invisibleEdge);
				
			} else {
				
				// normal relation
				double weight = (double) relationFrequency / mostOccurringRelation;
				if (relationFrequency >= (maxAllowedToCut * threshold)) {
					CognitiveDotNode dotSourceNode = (CognitiveDotNode) mapNodes.get(source);
					CognitiveDotNode dotTargetNode = (CognitiveDotNode) mapNodes.get(target);
					CognitiveDotEdge dotEdge = new CognitiveDotEdge(dotSourceNode, dotTargetNode, df.format(relationFrequency), weight);
					n.addEdge(dotEdge);
				}
				
			}
		}
		
		return dot;
	}


	
	
	public static Dot visualize(DfgMinerResultOld dfgResult, double threshold) {
		IMLogInfo logInfo = dfgResult.logInfo;
		Dfg dfg = dfgResult.dfg;
		Long maxAllowedToCut = dfgResult.maxAllowedToCut;
		
		Dot dot = new Dot();
		dot.setOption("outputorder", "edgesfirst");
		double mostOccurringDFRelation = dfg.getMostOccuringDirectlyFollowsEdgeCardinality();
		double mostOccurringActivity = Utils.getMostFrequencActivity(logInfo);
		
		Map<XEventClass, CognitiveDotNode> map = new HashMap<XEventClass, CognitiveDotNode>();
		for (XEventClass act : dfg.getActivities()) {
			long frequency = logInfo.getActivities().getCardinalityOf(act);
			CognitiveDotNode dotNode = new CognitiveDotNode(act.toString(), Long.toString(frequency), frequency / mostOccurringActivity);
			map.put(act, dotNode);
			dot.addNode(dotNode);
		}
		
		for (Long edge : dfg.getDirectlyFollowsEdges()) {
			long frequency = dfg.getDirectlyFollowsEdgeCardinality(edge);
			double weight = (double) frequency / mostOccurringDFRelation;
			if (frequency >= (maxAllowedToCut * threshold)) {
				CognitiveDotNode source = map.get(dfg.getDirectlyFollowsEdgeSource(edge));
				CognitiveDotNode target = map.get(dfg.getDirectlyFollowsEdgeTarget(edge));
				CognitiveDotEdge dotEdge = new CognitiveDotEdge(source, target, df.format(frequency), weight);
				dot.addEdge(dotEdge);
			}
		}
		
		CognitiveDotStartNode startDotNode = new CognitiveDotStartNode();
		dot.addNode(startDotNode);
		boolean moreThanOneStart = Iterables.size(dfg.getStartActivities()) > 1;
		for(XEventClass startAct : dfg.getStartActivities()) {
			long frequency = dfg.getStartActivityCardinality(startAct);
//			if (frequency >= (dfg.getMostOccurringEndActivityCardinality() * threshold)) {
				CognitiveDotEdge dotEdge = new CognitiveDotEdge(startDotNode, map.get(startAct), Long.toString(frequency), null);
				dot.addEdge(dotEdge);
//			}
			if (moreThanOneStart) {
				DotEdge invisibleStart = dot.addEdge(startDotNode, map.get(startAct));
				invisibleStart.setOption("style", "invisible");
				invisibleStart.setOption("arrowhead", "none");
			}
		}
		
		CognitiveDotEndNode endDotNode = new CognitiveDotEndNode();
		dot.addNode(endDotNode);
		boolean moreThanOneEnd = Iterables.size(dfg.getEndActivities()) > 1;
		for(XEventClass endAct : dfg.getEndActivities()) {
			long frequency = dfg.getEndActivityCardinality(endAct);
			if (frequency >= (dfg.getMostOccurringEndActivityCardinality() * threshold)) {
				CognitiveDotEdge dotEdge = new CognitiveDotEdge(map.get(endAct), endDotNode, Long.toString(frequency), null);
				dot.addEdge(dotEdge);
			}
			if (moreThanOneEnd) {
				DotEdge invisibleEnd = dot.addEdge(map.get(endAct), endDotNode);
				invisibleEnd.setOption("style", "invisible");
				invisibleEnd.setOption("arrowhead", "none");
			}
		}
		
		return dot;
	}
	
	public static Dot visualize(CausalActivityGraph cag) {
		Dot dot = new Dot();
		dot.setOption("outputorder", "edgesfirst");
		
		Map<XEventClass, CognitiveDotNode> map = new HashMap<XEventClass, CognitiveDotNode>();
		for (XEventClass act : cag.getActivities()) {
			CognitiveDotNode dotNode = new CognitiveDotNode(act.toString(), null, null);
			map.put(act, dotNode);
			dot.addNode(dotNode);

		}
		Set<XEventClass> startNodes = new HashSet<XEventClass>(cag.getActivities());
		Set<XEventClass> endNodes = new HashSet<XEventClass>(cag.getActivities());
		
		for (Relation relation : cag.getCausalRelations()) {
			CognitiveDotNode source = map.get(relation.getSource());
			CognitiveDotNode target = map.get(relation.getTarget());
			startNodes.remove(relation.getTarget());
			endNodes.remove(relation.getSource());
			CognitiveDotEdge dotEdge = new CognitiveDotEdge(source, target, null, cag.getCausality(relation.getSource(), relation.getTarget()));
			dot.addEdge(dotEdge);
		}
		
		CognitiveDotStartNode start = new CognitiveDotStartNode();
		dot.addNode(start);
		for (XEventClass startNode : startNodes) {
			dot.addEdge(new CognitiveDotEdge(start, map.get(startNode), null, null));
		}
		
		CognitiveDotEndNode end = new CognitiveDotEndNode();
		dot.addNode(end);
		for (XEventClass endNode : endNodes) {
			dot.addEdge(new CognitiveDotEdge(map.get(endNode), end, null, null));
		}
		
		return dot;
	}

}
