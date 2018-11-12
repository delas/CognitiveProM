package cognitiveprom.view.workers;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingWorker;

import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotNode;

import cognitiveprom.controllers.ApplicationController;
import cognitiveprom.log.projections.AggregationFunctions;
import cognitiveprom.log.projections.ValueProjector;
import cognitiveprom.logger.Logger;
import cognitiveprom.view.graph.CognitiveDotModel;
import cognitiveprom.view.graph.ColorPalette.Colors;

public class RendererWorker extends SwingWorker<Dot, Void> {

	private boolean done;
	private double threshold;
	private Collection<XTrace> tracesToConsider;
	private ValueProjector attribute;
	private AggregationFunctions function;
	private Colors activityColor;
	private boolean preserveAllNodesConnected;

	public RendererWorker(
			double threshold,
			Collection<XTrace> tracesToConsider,
			ValueProjector attribute,
			AggregationFunctions function,
			Colors activityColor,
			boolean preserveAllNodesConnected) {
		this.threshold = threshold;
		this.tracesToConsider = tracesToConsider;
		this.attribute = attribute;
		this.function = function;
		this.activityColor = activityColor;
		this.preserveAllNodesConnected = preserveAllNodesConnected;
	}
	
	@Override
	protected Dot doInBackground() throws Exception {
		int nodes = ApplicationController.instance().processController().model().getActivities().size();
		if (nodes > 100) {
			Logger.instance().debug("The model to render is way too big (" + nodes + " nodes)!");
			Dot dot = new Dot();
			DotNode node = dot.addNode("The model contains " + nodes + ", and this is too big.\nRight now, up to 100 nodes are supported.");
			node.setOption("shape", "box");
			node.setOption("color", "white");
			return dot;
			
		} else {
			Logger.instance().debug("Rendering started...");
			done = false;
			
			new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						if (!done) {
							ApplicationController.instance().showWaitingPage("Model rendering");
						}
					}
				}, 500);
			
			Dot dot = new CognitiveDotModel(
					ApplicationController.instance().processController().model(),
					threshold,
					tracesToConsider,
					attribute,
					function,
					activityColor,
					preserveAllNodesConnected);
			
			long time = System.currentTimeMillis();
			ApplicationController.instance().processController().showModel(dot);
			Logger.instance().debug("Show rendered : " + (System.currentTimeMillis() - time) + "ms");
			
			return dot;
		}
	}
	
	@Override
	protected void done() {
		this.done = true;
		Logger.instance().debug("Rendering complete");
		ApplicationController.instance().showMainPage();
	}
}
