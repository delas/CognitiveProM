package cognitiveprom.log.extension;

import org.deckfour.xes.classification.XEventAttributeClassifier;

public class XCognitiveEventClassifier extends XEventAttributeClassifier {

	private static final long serialVersionUID = 6644629488383302625L;

	public XCognitiveEventClassifier() {
		super("Area of Interest", XCognitiveExtension.KEY_AOI);
	}
}
