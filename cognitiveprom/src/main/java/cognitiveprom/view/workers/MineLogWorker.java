package cognitiveprom.view.workers;

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
import cognitiveprom.logger.Logger;
import cognitiveprom.map.ProcessMap;

public class MineLogWorker extends SwingWorker<ProcessMap, Void> {

	private CognitiveLog log;
	
	public MineLogWorker(CognitiveLog log) {
		this.log = log;
	}
	
	@Override
	protected ProcessMap doInBackground() throws Exception {
		Logger.instance().debug("Mining started...");
		ApplicationController.instance().showWaitingPage("Log mining");
		
		ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		try {
			long time = System.currentTimeMillis();
			EventRelationStorage eventStorage = EventRelationStorage.Factory.createEventRelations(
					log,
					new XEventNameClassifier(),
					executorService);
			Logger.instance().debug("Computation of relations: " + (System.currentTimeMillis() - time)+ "ms");
			
			time = System.currentTimeMillis();
			Map<XEventClass, Long> maxConn = new HashMap<XEventClass, Long>();
			for (Relation r : eventStorage.getDirectlyFollowsRelations()) {
				XEventClass source = r.getSource();
				XEventClass target = r.getTarget();
				if (!source.equals(target) &&
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
			Logger.instance().debug("Dependencies connection: " + (System.currentTimeMillis() - time)+ "ms");
			
			time = System.currentTimeMillis();
			List<Long> maxConnList = new ArrayList<Long>(maxConn.values());
			Collections.sort(maxConnList);
			Logger.instance().debug("Relations sorting: " + (System.currentTimeMillis() - time)+ "ms");
	
			return new ProcessMap(
					eventStorage,
					(maxConn.size() > 0)? maxConnList.get(0) : 0);
		} finally {
			executorService.shutdown();
		}
	}

	@Override
	protected void done() {
		Logger.instance().debug("Mining complete");
		try {
			ApplicationController.instance().processController().setCognitiveModel(get());
		} catch (InterruptedException | ExecutionException e) {
			Logger.instance().error(e);
		}
		
		ApplicationController.instance().processController().updateVisualization();
	}
}
