package cognitiveprom.log.io;

import java.io.File;

import org.deckfour.xes.in.XParser;
import org.deckfour.xes.in.XesXmlParser;

import cognitiveprom.annotations.Importer;
import cognitiveprom.exceptions.LogImportException;
import cognitiveprom.log.CognitiveLog;

/**
 * Class to import a {@link CognitiveLog} starting from a XES file
 * 
 * @author Andrea Burattin
 */
@Importer(
	name = "Load XES file",
	fileExtension = "xes"
)
public class XesCognitiveImporter extends CognitiveLogImporter {

	@Override
	public String getDescription() {
		return "Load XES file";
	}

	@Override
	public String getSupportedFileExtension() {
		return "xes";
	}
	
	@Override
	public CognitiveLog load(File fileName) throws LogImportException {
		try {
			XParser parser = new XesXmlParser();
			return new CognitiveLog(parser.parse(fileName).get(0));
		} catch (Exception e) {
			throw new LogImportException(e.getMessage());
		}
	}
}
