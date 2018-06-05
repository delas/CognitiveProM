package cognitiveprom.view.graph;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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
import cognitiveprom.log.projections.AggregationValues;
import cognitiveprom.model.CognitiveModel;
import cognitiveprom.tools.Utils;
import cognitiveprom.tools.XLogHelper;
import cognitiveprom.view.graph.ColorPalette.Colors;

/**
 * 
 * @author Andrea Burattin
 */
public class CognitiveDotModel extends Dot {

	private static DecimalFormat df = new DecimalFormat("#.###");

	private CognitiveModel model;
	private double threshold;
	private Collection<XTrace> tracesToConsider;
	private AggregationValues attribute;
	private AggregationFunctions function;
	private Colors activityColor;
	
	public CognitiveDotModel(
			CognitiveModel model,
			double threshold,
			Collection<XTrace> tracesToConsider,
			AggregationValues attribute,
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
	
	private static Map<String, Pair<String, Double>> getAggregated(Collection<XTrace> tracesToConsider, AggregationValues attribute, AggregationFunctions function) {
		Map<String, AggregationFunction> aggregators = new HashMap<String, AggregationFunction>();
		for (XTrace trace : tracesToConsider) {
			Set<String> processedActivities = new HashSet<String>();
			for (XEvent event : trace) {
				String activity = XLogHelper.getName(event);
				if (!processedActivities.contains(activity)) {
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
		Long maxAllowedToCut = model.getMaxAllowedToCut();

		double mostOccurringRelation = Utils.getMostFrequentRelation(eventRelations);
		double mostOccurringRelationStart = Utils.getMostFrequentRelationStart(eventRelations);
		double mostOccurringRelationEnd = Utils.getMostFrequentRelationEnd(eventRelations);
		
		Map<String, Pair<String, Double>> activityDecoration = getAggregated(tracesToConsider, attribute, function);

		
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
			
			long relationFrequency = eventRelations.countDirectlyFollows(relation);
			if (source.equals(eventRelations.getStartEventClass())) {
				
				// relations from start event
				if (relationFrequency >= (mostOccurringRelationStart * threshold)) {
					CognitiveDotEdge dotEdge = new CognitiveDotEdge(mapNodes.get(source), mapNodes.get(target), Long.toString(relationFrequency), null);
					addEdge(dotEdge);
				}
				DotEdge invisibleEdge = addEdge(mapNodes.get(source), mapNodes.get(target));
				invisibleEdge.setOption("style", "invisible");
				invisibleEdge.setOption("arrowhead", "none");
				addEdge(invisibleEdge);
				
			} else if (target.equals(eventRelations.getEndEventClass())) {
				
				// relation to end event
				if (relationFrequency >= (mostOccurringRelationEnd * threshold)) {
					CognitiveDotEdge dotEdge = new CognitiveDotEdge(mapNodes.get(source), mapNodes.get(target), Long.toString(relationFrequency), null);
					addEdge(dotEdge);
				}
				DotEdge invisibleEdge = addEdge(mapNodes.get(source), mapNodes.get(target));
				invisibleEdge.setOption("style", "invisible");
				invisibleEdge.setOption("arrowhead", "none");
				addEdge(invisibleEdge);
				
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
	}
}
