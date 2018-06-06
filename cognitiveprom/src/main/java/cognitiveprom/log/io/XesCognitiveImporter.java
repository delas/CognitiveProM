package cognitiveprom.log.io;

import java.io.File;

import org.deckfour.xes.in.XParser;
import org.deckfour.xes.in.XesXmlParser;

import cognitiveprom.annotations.Importer;
import cognitiveprom.exceptions.LogIOException;
import cognitiveprom.log.CognitiveLog;

/**
 * Class to import a {@link CognitiveLog} starting from a XES file
 * 
 * @author Andrea Burattin
 */
@Importer(
	name = "XES file",
	fileExtension = "xes"
)
public class XesCognitiveImporter extends CognitiveLogImporter {

	@Override
	public CognitiveLog load(File fileName) throws LogIOException {
		try {
			XParser parser = new XesXmlParser();
			return new CognitiveLog(parser.parse(fileName).get(0));
		} catch (Exception e) {
			throw new LogIOException(e.getMessage());
		}
	}
}
