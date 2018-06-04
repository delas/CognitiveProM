package cognitiveprom.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cognitiveprom.view.io.CognitiveLogImporterConfigurator;

/**
 * This annotation is used to identify all the available process importers
 * 
 * @author Andrea Burattin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Importer {

	/**
	 * The name of the importer
	 * 
	 * @return
	 */
	String name();

	/**
	 * The default file extension of this importer
	 * 
	 * @return
	 */
	String fileExtension();
	
	/**
	 * An additional GUI configurator associated to the importer
	 * 
	 * @return
	 */
	Class<? extends CognitiveLogImporterConfigurator> guiConfigurator() default CognitiveLogImporterConfigurator.class;
}
