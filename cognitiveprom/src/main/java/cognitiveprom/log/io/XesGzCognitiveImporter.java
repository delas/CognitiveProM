package cognitiveprom.log.io;

import java.io.File;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.deckfour.xes.in.XParser;
import org.deckfour.xes.model.XLog;
import org.processmining.framework.util.progress.XMonitoredInputStream;
import org.processmining.framework.util.progress.XProgressListener;
import org.xeslite.parser.XesLiteXmlParser;

import cognitiveprom.annotations.Importer;
import cognitiveprom.exceptions.LogIOException;
import cognitiveprom.log.CognitiveLog;

/**
 * Class to import a {@link CognitiveLog} starting from a XES file
 * 
 * @author Andrea Burattin
 */
@Importer(
	name = "Compressed XES file",
	fileExtension = "gz"
)
public class XesGzCognitiveImporter extends CognitiveLogImporter {

	@Override
	public CognitiveLog load(File fileName) throws LogIOException {
		try {
			XParser parser = new XesLiteXmlParser(true);
			List<XLog> logs = parser.parse(new GZIPInputStream(new XMonitoredInputStream(fileName, new XProgressListener() {
				@Override
				public void updateProgress(int progress, int maxProgress) {
					triggerProgressUpdate(0, progress, maxProgress);
				}
				@Override
				public boolean isAborted() {
					return false;
				}
			})));
			CognitiveLog log = new CognitiveLog(logs.get(0));
			
			return log;
		} catch (Exception e) {
			throw new LogIOException(e.getMessage());
		}
	}
}
