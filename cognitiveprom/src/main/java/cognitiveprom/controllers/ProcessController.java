package cognitiveprom.controllers;

import org.processmining.plugins.graphviz.dot.Dot;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.map.ProcessMap;
import cognitiveprom.view.workers.MineLogWorker;
import cognitiveprom.view.workers.RendererWorker;

/**
 * 
 * @author Andrea Burattin
 */
public class ProcessController {

	protected static final String KEY_ADVANCED_CONFIG_VISIBLE = "ADVANCED_CONFIGURATION_VISIBLE";
	protected static final boolean DEFAULT_ADVANCED_CONFIG_VISIBILITY = false;

	private ProcessMap model;
	
	private ApplicationController applicationController;
	private ConfigurationSet configuration;
	private boolean isShowingModel = false;
	
	public ProcessController(ApplicationController applicationController) {
		this.applicationController = applicationController;
		this.configuration = applicationController.getConfiguration(ProcessController.class.getCanonicalName());
		
		// set default console visibility
		setAdvancedConfigurationVisibility(configuration.getBoolean(KEY_ADVANCED_CONFIG_VISIBLE, DEFAULT_ADVANCED_CONFIG_VISIBILITY));
	}
	
	public ProcessMap model() {
		return model;
	}
	
	public void setCognitiveModel(ProcessMap model) {
		this.model = model;
		applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().populateComponents();
	}
	
	public void mineLog() {
		new MineLogWorker(applicationController.logController().log()).execute();
	}
	
	public void showModel(Dot model) {
		applicationController.getMainPage().getProcessVisualizer().getGraphVisualizer().changeDot(model, true);
		this.isShowingModel = true;
	}
	
	public boolean isShowingModel() {
		return isShowingModel;
	}
	
	public void updateVisualization() {
		new RendererWorker(
				applicationController.getMainPage().getProcessVisualizer().getAbstractionValue(),
				applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().getSelectedTraces(),
				applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().getSelectedAggregationValue(),
				applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().getSelectedAggregationFunction(),
				applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().getSelectedNodeColor()).execute();
	}

	public void setAdvancedConfigurationVisibility(boolean visible) {
		configuration.setBoolean(KEY_ADVANCED_CONFIG_VISIBLE, visible);
		applicationController.getMainPage().getToolbar().setShowAdvancedConfigurationSelected(visible);
		applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().setVisible(visible);
	}
}
