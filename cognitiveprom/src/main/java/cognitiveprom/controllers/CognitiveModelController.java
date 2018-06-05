package cognitiveprom.controllers;

import cognitiveprom.log.projections.AggregationFunctions;
import cognitiveprom.log.projections.AggregationValues;
import cognitiveprom.model.CognitiveModel;
import cognitiveprom.view.graph.CognitiveDotModel;
import cognitiveprom.view.graph.ColorPalette;
import cognitiveprom.workers.MineLogWorker;
import cognitiveprom.workers.ModelRendererWorker;

/**
 * 
 * @author Andrea Burattin
 */
public class CognitiveModelController {

	private CognitiveModel model;
	private ApplicationController applicationController;
	
	public CognitiveModelController(ApplicationController applicationController) {
		this.applicationController = applicationController;
	}
	
	public CognitiveModel model() {
		return model;
	}
	
	public void setCognitiveModel(CognitiveModel model) {
		this.model = model;
	}
	
	public void mineLog() {
		new MineLogWorker(applicationController.log().log()).execute();
	}
	
	public void showModel(CognitiveDotModel model) {
		applicationController.getMainPage().getProcessVisualizer().getGraphVisualizer().changeDot(model, true);
	}
	
	public void updateVisualization() {
		new ModelRendererWorker(
				applicationController.getMainPage().getProcessVisualizer().getAbstractionValue(),
				applicationController.log().log().getLog(),
				AggregationValues.FREQUENCY,
				AggregationFunctions.SUM,
				ColorPalette.Colors.BLUE).execute();
	}
}
