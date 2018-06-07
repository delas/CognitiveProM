package cognitiveprom.view.workers;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotNode;

import cognitiveprom.controllers.ApplicationController;
import cognitiveprom.log.projections.AggregationFunctions;
import cognitiveprom.log.projections.AggregationValues;
import cognitiveprom.utils.Logger;
import cognitiveprom.view.graph.CognitiveDotModel;
import cognitiveprom.view.graph.ColorPalette.Colors;

public class RendererWorker extends SwingWorker<Dot, Void> {

	private boolean done;
	private double threshold;
	private Collection<XTrace> tracesToConsider;
	private AggregationValues attribute;
	private AggregationFunctions function;
	private Colors activityColor;

	public RendererWorker(
			double threshold,
			Collection<XTrace> tracesToConsider,
			AggregationValues attribute,
			AggregationFunctions function,
			Colors activityColor) {
		this.threshold = threshold;
		this.tracesToConsider = tracesToConsider;
		this.attribute = attribute;
		this.function = function;
		this.activityColor = activityColor;
	}
	
	@Override
	protected Dot doInBackground() throws Exception {
		int nodes = ApplicationController.instance().processController().model().getActivities().size();
		if (nodes > 100) {
			Logger.instance().debug("The model to render is way too big!");
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
							if (ApplicationController.instance().getMainPage().getProcessVisualizer().getGraphVisualizer().getDot().getEdges().size() > 0) {
								ApplicationController.instance().getMainPage().getWaitingPanel().setTranslucent(true);
							}
							ApplicationController.instance().getMainPage().showWaitingPanel("Rendering model...");
						}
					}
				}, 500);
			
			return new CognitiveDotModel(
					ApplicationController.instance().processController().model(),
					threshold,
					tracesToConsider,
					attribute,
					function,
					activityColor);
		}
	}
	
	@Override
	protected void done() {
		this.done = true;
		Logger.instance().debug("Rendering complete");
		try {
			ApplicationController.instance().processController().showModel(get());
		} catch (InterruptedException | ExecutionException e) { }
		
		ApplicationController.instance().getMainPage().getWaitingPanel().setTranslucent(false);
		ApplicationController.instance().getMainPage().showProcessVisualizer();
	}
}
