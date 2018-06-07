package cognitiveprom.controllers;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.log.projections.AggregationFunctions;
import cognitiveprom.log.projections.AggregationValues;
import cognitiveprom.process.CognitiveProcess;
import cognitiveprom.view.graph.CognitiveDotModel;
import cognitiveprom.view.graph.ColorPalette;
import cognitiveprom.workers.MineLogWorker;
import cognitiveprom.workers.ModelRendererWorker;

/**
 * 
 * @author Andrea Burattin
 */
public class ProcessController {

	protected static final String KEY_ADVANCED_CONFIG_VISIBLE = "ADVANCED_CONFIGURATION_VISIBLE";
	protected static final boolean DEFAULT_ADVANCED_CONFIG_VISIBILITY = false;

	private CognitiveProcess model;
	
	private ApplicationController applicationController;
	private ConfigurationSet configuration;
	
	public ProcessController(ApplicationController applicationController) {
		this.applicationController = applicationController;
		this.configuration = applicationController.getConfiguration(ProcessController.class.getCanonicalName());
		
		// set default console visibility
		setAdvancedConfigurationVisibility(configuration.getBoolean(KEY_ADVANCED_CONFIG_VISIBLE, DEFAULT_ADVANCED_CONFIG_VISIBILITY));
	}
	
	public CognitiveProcess model() {
		return model;
	}
	
	public void setCognitiveModel(CognitiveProcess model) {
		this.model = model;
		applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().populateComponents();
	}
	
	public void mineLog() {
		new MineLogWorker(applicationController.logController().log()).execute();
	}
	
	public void showModel(CognitiveDotModel model) {
		applicationController.getMainPage().getProcessVisualizer().getGraphVisualizer().changeDot(model, true);
	}
	
	public void updateVisualization() {
		new ModelRendererWorker(
				applicationController.getMainPage().getProcessVisualizer().getAbstractionValue(),
				applicationController.logController().log().getLog(),
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
