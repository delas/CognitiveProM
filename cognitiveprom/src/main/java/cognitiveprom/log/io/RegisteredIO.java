package cognitiveprom.log.io;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.reflections.Reflections;

import cognitiveprom.annotations.Exporter;
import cognitiveprom.annotations.Importer;

/**
 * This class contains utility methods to retrieve all the registered file
 * importer and exporters.
 * 
 * @author Andrea Burattin
 */
public class RegisteredIO {

	/**
	 * This method returns all the registered importers (i.e., classes annotated
	 * as {@link Importer})
	 * 
	 * @return a set of importers
	 */
	public static List<Class<?>> getAllImporters() {
		Reflections reflections = new Reflections("cognitiveprom");
		List<Class<?>> importers = new LinkedList<Class<?>>(reflections.getTypesAnnotatedWith(Importer.class));
		Collections.sort(importers, new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				String s1 = o1.getAnnotation(Importer.class).name();
				String s2 = o2.getAnnotation(Importer.class).name();
				return s1.compareTo(s2);
			}
		});
		return importers;
	}
	
	/**
	 * This method returns all the registered exporter (i.e., classes annotated
	 * as {@link Exporter})
	 * 
	 * @return a set of importers
	 */
	public static List<Class<?>> getAllExporters() {
		Reflections reflections = new Reflections("cognitiveprom");
		List<Class<?>> importers = new LinkedList<Class<?>>(reflections.getTypesAnnotatedWith(Exporter.class));
		Collections.sort(importers, new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				String s1 = o1.getAnnotation(Exporter.class).name();
				String s2 = o2.getAnnotation(Exporter.class).name();
				return s1.compareTo(s2);
			}
		});
		return importers;
	}
}
