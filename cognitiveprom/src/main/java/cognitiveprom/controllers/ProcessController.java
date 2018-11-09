package cognitiveprom.controllers;

import java.util.Collection;

import org.deckfour.xes.model.XTrace;
import org.processmining.plugins.graphviz.dot.Dot;

import cognitiveprom.config.ConfigurationSet;
import cognitiveprom.log.projections.AggregationFunctions;
import cognitiveprom.log.projections.ValueProjector;
import cognitiveprom.logger.Logger;
import cognitiveprom.map.ProcessMap;
import cognitiveprom.view.graph.ColorPalette.Colors;
import cognitiveprom.view.workers.MineLogWorker;
import cognitiveprom.view.workers.RendererWorker;

/**
 * 
 * @author Andrea Burattin
 */
public class ProcessController {

	protected static final String KEY_ADVANCED_CONFIG_VISIBLE = "ADVANCED_CONFIGURATION_VISIBLE";
	protected static final boolean DEFAULT_ADVANCED_CONFIG_VISIBILITY = false;
	protected static final String KEY_TRACES_VISIBLE = "TRACES_VISIBLE";
	protected static final boolean DEFAULT_TRACES_VISIBILITY = false;

	private ProcessMap model;
	
	private ApplicationController applicationController;
	private ConfigurationSet configuration;
	private boolean isShowingModel = false;
	
	public ProcessController(ApplicationController applicationController) {
		this.applicationController = applicationController;
		this.configuration = applicationController.getConfiguration(ProcessController.class.getCanonicalName());
		
		// set default console visibility
		setAdvancedConfigurationVisibility(configuration.getBoolean(KEY_ADVANCED_CONFIG_VISIBLE, DEFAULT_ADVANCED_CONFIG_VISIBILITY));
		setTracesVisibility(configuration.getBoolean(KEY_TRACES_VISIBLE, DEFAULT_TRACES_VISIBILITY));
	}
	
	public void reset() {
		isShowingModel = false;
		model = null;
	}
	
	public ProcessMap model() {
		return model;
	}
	
	public void setCognitiveModel(ProcessMap model) {
		this.model = model;
		long time = System.currentTimeMillis();
		applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().populateComponents();
		Logger.instance().debug("Advanced configuration population: " + (System.currentTimeMillis() - time) + "ms");
		
//		time = System.currentTimeMillis();
//		applicationController.getMainPage().getProcessVisualizer().getShowTracesPanel().populateComponents();
//		Logger.instance().debug("Traces population: " + (System.currentTimeMillis() - time));
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
		long time = System.currentTimeMillis();
		double threshold = applicationController.getMainPage().getProcessVisualizer().getAbstractionValue();
		Collection<XTrace> tracesToConsider = applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().getSelectedTraces();
		ValueProjector attribute = applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().getSelectedAggregationValue();
		AggregationFunctions function = applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().getSelectedAggregationFunction();
		Colors activityColor = applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().getSelectedNodeColor();
		Logger.instance().debug("Preparation for rendering: " + (System.currentTimeMillis() - time) + "ms");
		
		new RendererWorker(threshold, tracesToConsider, attribute, function, activityColor).execute();
	}

	public void setAdvancedConfigurationVisibility(boolean visible) {
		configuration.setBoolean(KEY_ADVANCED_CONFIG_VISIBLE, visible);
		applicationController.getMainPage().getToolbar().setShowAdvancedConfigurationSelected(visible);
		applicationController.getMainPage().getProcessVisualizer().getAdvancedConfigurationPanel().setVisible(visible);
	}

	public void setTracesVisibility(boolean visible) {
		configuration.setBoolean(KEY_TRACES_VISIBLE, visible);
//		applicationController.getMainPage().getToolbar().setShowTracesSelected(visible);
		applicationController.getMainPage().getProcessVisualizer().getShowTracesPanel().setVisible(visible);
	}
}
