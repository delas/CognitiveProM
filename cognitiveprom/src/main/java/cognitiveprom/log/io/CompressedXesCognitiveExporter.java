package cognitiveprom.log.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.deckfour.xes.out.XSerializer;
import org.deckfour.xes.out.XesXmlGZIPSerializer;

import cognitiveprom.annotations.Exporter;
import cognitiveprom.exceptions.LogIOException;
import cognitiveprom.log.CognitiveLog;

/**
 * Class to export a {@link CognitiveLog} into a XES file
 * 
 * @author Andrea Burattin
 */
@Exporter(
	name = "Compressed XES file",
	fileExtension = "xes.gz"
)
public class CompressedXesCognitiveExporter extends CognitiveLogExporter {

	@Override
	public void save(File fileName, CognitiveLog log) throws LogIOException {
		try {
			XSerializer serializer = new XesXmlGZIPSerializer();
			serializer.serialize(log.getLog(), new FileOutputStream(fileName));
		} catch (IOException e) {
			throw new LogIOException(e.getMessage());
		}
	}
}
