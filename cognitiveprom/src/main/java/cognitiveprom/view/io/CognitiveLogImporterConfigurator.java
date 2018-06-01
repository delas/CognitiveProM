package cognitiveprom.view.io;

import cognitiveprom.log.io.CognitiveLogImporter;
import cognitiveprom.view.frames.MainFrame;

public interface CognitiveLogImporterConfigurator {

	public abstract void configure(MainFrame mainFrame, CognitiveLogImporter importer);
}
