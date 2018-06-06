package cognitiveprom.controllers;

import cognitiveprom.config.ConfigurationSet;
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

	protected static final String KEY_ADVANCED_CONFIG_VISIBLE = "ADVANCED_CONFIGURATION_VISIBLE";
	protected static final boolean DEFAULT_ADVANCED_CONFIG_VISIBILITY = false;

	private CognitiveModel model;
	
	private ApplicationController applicationController;
	private ConfigurationSet configuration;
	
	public CognitiveModelController(ApplicationController applicationController) {
		this.applicationController = applicationController;
		this.configuration = applicationController.getConfiguration(CognitiveModelController.class.getCanonicalName());
		
		// set default console visibility
		setAdvancedConfigurationVisibility(configuration.getBoolean(KEY_ADVANCED_CONFIG_VISIBLE, DEFAULT_ADVANCED_CONFIG_VISIBILITY));
	}
	
	public CognitiveModel model() {
		return model;
	}
	
	public void setCognitiveModel(CognitiveModel model) {
		this.model = model;
		applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().populateComponents();
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

	public void setAdvancedConfigurationVisibility(boolean visible) {
		configuration.setBoolean(KEY_ADVANCED_CONFIG_VISIBLE, visible);
		applicationController.getMainPage().getToolbar().setShowAdvancedConfigurationSelected(visible);
		applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().setVisible(visible);
	}
}
