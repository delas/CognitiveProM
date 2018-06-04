package cognitiveprom.workers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingWorker;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.processmining.dataawarecnetminer.model.EventRelationStorage;
import org.processmining.models.causalgraph.Relation;

import cognitiveprom.controllers.ApplicationController;
import cognitiveprom.log.CognitiveLog;
import cognitiveprom.model.CognitiveModel;
import cognitiveprom.utils.Logger;

public class MineLogWorker extends SwingWorker<CognitiveModel, Void> {

	private CognitiveLog log;
	
	public MineLogWorker(CognitiveLog log) {
		this.log = log;
	}
	
	@Override
	protected CognitiveModel doInBackground() throws Exception {
		Logger.instance().info("Mining started...");
		ApplicationController.instance().getMainPage().showWaitingPanel();
		
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		EventRelationStorage eventStorage = EventRelationStorage.Factory.createEventRelations(
				log,
				new XEventNameClassifier(),
				executorService);
		
		Map<XEventClass, Long> maxConn = new HashMap<XEventClass, Long>();
		for (Relation r : eventStorage.getDirectlyFollowsRelations()) {
			XEventClass source = r.getSource();
			XEventClass target = r.getTarget();
			if (	!source.equals(target) &&
					!source.equals(eventStorage.getStartEventClass()) &&
					!target.equals(eventStorage.getEndEventClass())) {
				maxConn.put(source,
						Math.max(
								eventStorage.countDirectlyFollows(r),
								(maxConn.containsKey(source)? maxConn.get(source) : 0)));
				maxConn.put(target,
						Math.max(
								eventStorage.countDirectlyFollows(r),
								(maxConn.containsKey(target)? maxConn.get(target) : 0)));
			}
		}
		List<Long> maxConnList = new ArrayList<Long>(maxConn.values());
		Collections.sort(maxConnList);

		return new CognitiveModel(
				eventStorage,
				(maxConn.size() > 0)? maxConnList.get(0) : 0);
	}

	@Override
	protected void done() {
		Logger.instance().info("Mining complete");
		try {
			ApplicationController.instance().log().setCognitiveModel(get());
		} catch (InterruptedException | ExecutionException e) { }
		ApplicationController.instance().getMainPage().showProcessVisualizer();
	}
}
