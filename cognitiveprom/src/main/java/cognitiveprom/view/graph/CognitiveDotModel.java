package cognitiveprom.view.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.processmining.dataawarecnetminer.model.EventRelationStorage;
import org.processmining.framework.util.Pair;
import org.processmining.models.causalgraph.Relation;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotCluster;
import org.processmining.plugins.graphviz.dot.DotEdge;
import org.processmining.plugins.graphviz.dot.DotNode;

import com.google.common.collect.Multiset.Entry;

import cognitiveprom.log.projections.AggregationFunction;
import cognitiveprom.log.projections.AggregationFunctions;
import cognitiveprom.log.projections.ValueProjector;
import cognitiveprom.log.utils.XCognitiveLogHelper;
import cognitiveprom.map.ProcessMap;
import cognitiveprom.view.graph.ColorPalette.Colors;

/**
 * 
 * @author Andrea Burattin
 */
public class CognitiveDotModel extends Dot {

	private ProcessMap model;
	private double threshold;
	private Collection<XTrace> tracesToConsider;
	private ValueProjector attribute;
	private AggregationFunctions function;
	private Colors activityColor;
	
	public CognitiveDotModel(
			ProcessMap model,
			double threshold,
			Collection<XTrace> tracesToConsider,
			ValueProjector attribute,
			AggregationFunctions function,
			Colors activityColor) {
		
		this.model = model;
		this.threshold = threshold;
		this.tracesToConsider = tracesToConsider;
		this.attribute = attribute;
		this.function = function;
		this.activityColor = activityColor;
		
		realize();
	}

	private Map<Pair<String, String>, Pair<String, Double>> getAggregatedRelations() {
		Map<Pair<String, String>, AggregationFunction> aggregators = new HashMap<Pair<String, String>, AggregationFunction>();
		for (XTrace trace : tracesToConsider) {
			// start case
			Pair<String, String> startPair = new Pair<String, String>(EventRelationStorage.ARTIFICIAL_START, XCognitiveLogHelper.getAOIName(trace.get(0)));
			List<Double> vals = attribute.getValues(trace, startPair.getFirst(), startPair.getSecond());
			if (!aggregators.containsKey(startPair) && vals.size() > 0) {
				aggregators.put(startPair, new AggregationFunction());
			}
			for (Double value : vals) {
				aggregators.get(startPair).addObservation(value);
			}
			
			// end case
			Pair<String, String> endPair = new Pair<String, String>(XCognitiveLogHelper.getAOIName(trace.get(trace.size() - 1)), EventRelationStorage.ARTIFICIAL_END);
			vals = attribute.getValues(trace, endPair.getFirst(), endPair.getSecond());
			if (!aggregators.containsKey(endPair) && vals.size() > 0) {
				aggregators.put(endPair, new AggregationFunction());
			}
			for (Double value : vals) {
				aggregators.get(endPair).addObservation(value);
			}
			
			// normal case
			Set<Pair<String, String>> processedRelations = new HashSet<Pair<String, String>>();
			for (int i = 0; i < trace.size() - 1; i++) {
				// we want to process the activity only once per trace
				Pair<String, String> relation = new Pair<String, String>(XCognitiveLogHelper.getAOIName(trace.get(i)), XCognitiveLogHelper.getAOIName(trace.get(i + 1)));
				if (!processedRelations.contains(relation)) {
					vals = attribute.getValues(trace, relation.getFirst(), relation.getSecond());
					if (!aggregators.containsKey(relation) && vals.size() > 0) {
						aggregators.put(relation, new AggregationFunction());
					}
					for (Double value : vals) {
						aggregators.get(relation).addObservation(value);
					}
					processedRelations.add(relation);
				}
			}
		}
		
		Double max = Double.MIN_VALUE;
		for (Pair<String, String> relation : aggregators.keySet()) {
			max = Math.max(max, aggregators.get(relation).getValue(function).doubleValue());
		}
		
		Map<Pair<String, String>, Pair<String, Double>> values = new HashMap<Pair<String, String>, Pair<String, Double>>();
		for (Pair<String, String> relation : aggregators.keySet()) {
			AggregationFunction af = aggregators.get(relation);
			Pair<String, Double> p = new Pair<String, Double>(af.getStringValue(function), af.getValue(function).doubleValue() / max);
			values.put(relation, p);
		}
		
		return values;
	}
	
	private Map<String, Pair<String, Double>> getAggregatedActivities() {
		Map<String, AggregationFunction> aggregators = new HashMap<String, AggregationFunction>();
		for (XTrace trace : tracesToConsider) {
			// we want to process the activity only once per trace
			Set<String> processedActivities = new HashSet<String>();
			for (XEvent event : trace) {
				String activity = XCognitiveLogHelper.getAOIName(event);
				if (!processedActivities.contains(activity) && attribute.getValues(trace, activity).size() > 0) {
					if (!aggregators.containsKey(activity)) {
						aggregators.put(activity, new AggregationFunction());
					}
					for(Double value : attribute.getValues(trace, activity)) {
						aggregators.get(activity).addObservation(value);
					}
					processedActivities.add(activity);
				}
			}
		}
		
		Double max = Double.MIN_VALUE;
		for (String activity : aggregators.keySet()) {
			max = Math.max(max, aggregators.get(activity).getValue(function).doubleValue());
		}
		
		Map<String, Pair<String, Double>> values = new HashMap<String, Pair<String, Double>>();
		for (String activity : aggregators.keySet()) {
			AggregationFunction af = aggregators.get(activity);
			Pair<String, Double> p = new Pair<String, Double>(af.getStringValue(function), af.getValue(function).doubleValue() / max);
			values.put(activity, p);
		}
		
		return values;
	}
	
	private void realize() {
		EventRelationStorage eventRelations = model.getEventRelationStorage();

		double mostOccurringRelationStart = model.getMostFrequentRelationStart();
		double mostOccurringRelationEnd = model.getMostFrequentRelationEnd();
		
		Map<String, Pair<String, Double>> activityDecoration = getAggregatedActivities();
		Map<Pair<String, String>, Pair<String, Double>> relationDecoration = getAggregatedRelations();
		
		setOption("outputorder", "edgesfirst");
		
		// main clusters
		DotCluster s = addCluster();
		DotCluster n = addCluster();
		DotCluster e = addCluster();
		
		s.setOption("style", "invis");
		n.setOption("style", "invis");
		e.setOption("style", "invis");
		
		// adding all nodes
		Map<XEventClass, DotNode> mapNodes = new HashMap<XEventClass, DotNode>();
		for(XEventClass act : model.getActivities()) {
			DotNode node = null;
			if (act.equals(eventRelations.getStartEventClass())) {
				node = new CognitiveDotStartNode();
				s.addNode(node);
			} else if (act.equals(eventRelations.getEndEventClass())) {
				node = new CognitiveDotEndNode();
				e.addNode(node);
			} else {
				String activity = act.toString();
				node = new CognitiveDotNode(
						act.toString(),
						(activityDecoration.containsKey(activity))? activityDecoration.get(activity).getFirst() : null,
						(activityDecoration.containsKey(activity))? activityDecoration.get(activity).getSecond() : null,
						activityColor);
				n.addNode(node);
			}
			mapNodes.put(act, node);
		}
		
		// adding relations
		for (Entry<Relation> entry : eventRelations.getDirectlyFollowsRelations().entrySet()) {
			Relation relation = entry.getElement();
			XEventClass source = relation.getSource();
			XEventClass target = relation.getTarget();
			Pair<String, String> relationPair = new Pair<String, String>(source.toString(), target.toString());
			
			long relationFrequency = eventRelations.countDirectlyFollows(relation);
			if (source.equals(eventRelations.getStartEventClass())) {
				
				// relations from start event
				if (relationFrequency >= (mostOccurringRelationStart * threshold)) {
					CognitiveDotEdge dotEdge = new CognitiveDotEdge(
							mapNodes.get(source),
							mapNodes.get(target),
							(relationDecoration.containsKey(relationPair))? relationDecoration.get(relationPair).getFirst() : null,
							(relationDecoration.containsKey(relationPair))? relationDecoration.get(relationPair).getSecond() : null);
					addEdge(dotEdge);
				}
				DotEdge invisibleEdge = addEdge(mapNodes.get(source), mapNodes.get(target));
				invisibleEdge.setOption("style", "invisible");
				invisibleEdge.setOption("arrowhead", "none");
				addEdge(invisibleEdge);
				
			} else if (target.equals(eventRelations.getEndEventClass())) {
				
				// relation to end event
				if (relationFrequency >= (mostOccurringRelationEnd * threshold)) {
					CognitiveDotEdge dotEdge = new CognitiveDotEdge(
							mapNodes.get(source),
							mapNodes.get(target),
							(relationDecoration.containsKey(relationPair))? relationDecoration.get(relationPair).getFirst() : null,
							(relationDecoration.containsKey(relationPair))? relationDecoration.get(relationPair).getSecond() : null);
					addEdge(dotEdge);
				}
				DotEdge invisibleEdge = addEdge(mapNodes.get(source), mapNodes.get(target));
				invisibleEdge.setOption("style", "invisible");
				invisibleEdge.setOption("arrowhead", "none");
				addEdge(invisibleEdge);
				
			} else {
				
				// normal relation
				if (relationFrequency >= (model.getMaxAllowedToCut() * threshold)) {
					CognitiveDotNode dotSourceNode = (CognitiveDotNode) mapNodes.get(source);
					CognitiveDotNode dotTargetNode = (CognitiveDotNode) mapNodes.get(target);
					CognitiveDotEdge dotEdge = new CognitiveDotEdge(
							dotSourceNode,
							dotTargetNode,
							(relationDecoration.containsKey(relationPair))? relationDecoration.get(relationPair).getFirst() : null,
							(relationDecoration.containsKey(relationPair))? relationDecoration.get(relationPair).getSecond() : null);
					n.addEdge(dotEdge);
				}
				
			}
		}
	}
}
