package cognitiveprom.log.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.deckfour.xes.out.XSerializer;
import org.deckfour.xes.out.XesXmlSerializer;

import cognitiveprom.annotations.Exporter;
import cognitiveprom.exceptions.LogIOException;
import cognitiveprom.log.CognitiveLog;

/**
 * Class to export a {@link CognitiveLog} into a XES file
 * 
 * @author Andrea Burattin
 */
@Exporter(
	name = "XES file",
	fileExtension = "xes"
)
public class XesCognitiveExporter extends CognitiveLogExporter {

	@Override
	public void save(File fileName, CognitiveLog log) throws LogIOException {
		try {
			XSerializer serializer = new XesXmlSerializer();
			serializer.serialize(log.getLog(), new FileOutputStream(fileName));
		} catch (IOException e) {
			throw new LogIOException(e.getMessage());
		}
	}
}
