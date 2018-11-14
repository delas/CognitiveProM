package cognitiveprom.utils;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.processmining.framework.util.Pair;

import cognitiveprom.annotations.Exporter;
import cognitiveprom.annotations.Importer;
import cognitiveprom.log.io.CognitiveLogExporter;
import cognitiveprom.log.io.CognitiveLogImporter;
import cognitiveprom.log.io.RegisteredIO;
import cognitiveprom.logger.Logger;
import cognitiveprom.view.io.CognitiveLogImporterConfigurator;

/**
 * This class contains utility methods to assign proper file filters, according
 * to the available {@link CognitiveLogImporter}s or
 * {@link CognitiveLogExporter}s.
 * 
 * <p>
 * This class uses the methods in {@link RegisteredIO} in order to
 * retrieve the available importers and exporters.
 * 
 * @author Andrea Burattin
 */
public class FileFilterHelper {

	protected static String FILE_HELPER_DESCRIPTION = "%s (*.%s)";
	
	/**
	 * This class assigns, to the provided file chooser, the {@link FileFilter}s
	 * according to the available {@link Importer}s.
	 * 
	 * @param fileChooser the file chooser that will receive the file filters
	 */
	public static void assignImportFileFilters(JFileChooser fileChooser) {
		fileChooser.setAcceptAllFileFilterUsed(true);
		for (Class<?> importer : RegisteredIO.getAllImporters()) {
			final Importer annotation = importer.getAnnotation(Importer.class);
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
					String.format(FILE_HELPER_DESCRIPTION,
							annotation.name(),
							annotation.fileExtension()),
					annotation.fileExtension()));
		}
	}
	
	/**
	 * This class assigns, to the provided file chooser, the {@link FileFilter}s
	 * according to the available {@link Exporter}s.
	 * 
	 * @param fileChooser the file chooser that will receive the file filters
	 */
	public static void assignExportFileFilters(JFileChooser fileChooser) {
		fileChooser.setAcceptAllFileFilterUsed(false);
		for (Class<?> exporter : RegisteredIO.getAllExporters()) {
			final Exporter annotation = exporter.getAnnotation(Exporter.class);
			fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(
					String.format(FILE_HELPER_DESCRIPTION,
							annotation.name(),
							annotation.fileExtension()),
					annotation.fileExtension()));
		}
	}
	
	/**
	 * This method fixes the name of the provided file by adding the required
	 * extensions whether it is required
	 * 
	 * @param currentFileName the current file name
	 * @param fileFilter the file filter
	 * @return the fixed file name
	 */
	public static String fixFileName(String currentFileName, FileNameExtensionFilter fileFilter) {
		String extension = fileFilter.getExtensions()[0];
		if (!currentFileName.endsWith("." + extension)) {
			return currentFileName + "." + extension;
		}
		return currentFileName;
	}
	
	/**
	 * This method generates a new instance of a file exporter starting from the
	 * file extension provided
	 * 
	 * @param fileFilter the file extension to consider
	 * @return the file exporter
	 */
	public static CognitiveLogExporter getExporterFromFileName(FileNameExtensionFilter fileFilter) {
		for (Class<?> exporter : RegisteredIO.getAllExporters()) {
			Exporter annotation = exporter.getAnnotation(Exporter.class);
			String description = String.format(FILE_HELPER_DESCRIPTION, annotation.name(), annotation.fileExtension());
			if (description.equals(fileFilter.getDescription())) {
				try {
					return (CognitiveLogExporter) exporter.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					Logger.instance().error(e);
				}
			}
		}
		return null;
	}
	
	/**
	 * This method generates a new instance of a {@link CognitiveLogImporter}
	 * starting from the file filter provided
	 * 
	 * @param fileFilter the file filter to consider
	 * @return the file importer
	 */
	public static Pair<CognitiveLogImporter, CognitiveLogImporterConfigurator> getImporterFromFileFilter(FileNameExtensionFilter fileFilter) {
		for (Class<?> importer : RegisteredIO.getAllImporters()) {
			Importer annotation = importer.getAnnotation(Importer.class);
			String description = String.format(FILE_HELPER_DESCRIPTION, annotation.name(), annotation.fileExtension());
			if (description.equals(fileFilter.getDescription())) {
				try {
					CognitiveLogImporterConfigurator configurator = null;
					if (!annotation.guiConfigurator().equals(CognitiveLogImporterConfigurator.class)) {
						configurator = (CognitiveLogImporterConfigurator) annotation.guiConfigurator().newInstance();
					}
					return new Pair<CognitiveLogImporter, CognitiveLogImporterConfigurator>(
							(CognitiveLogImporter) importer.newInstance(),
							configurator
						);
				} catch (InstantiationException | IllegalAccessException e) {
					Logger.instance().error(e);
				}
			}
		}
		return null;
	}
	
	/**
	 * This method generates a new instance of a {@link CognitiveLogImporter}
	 * starting from the file name provided
	 * 
	 * @param fileName the file name to consider
	 * @return the file importer
	 */
	public static Pair<CognitiveLogImporter, CognitiveLogImporterConfigurator> getImporterFromFileName(String fileName) {
		for (Class<?> importer : RegisteredIO.getAllImporters()) {
			Importer annotation = importer.getAnnotation(Importer.class);
			if (fileName.endsWith(annotation.fileExtension())) {
				try {
					CognitiveLogImporterConfigurator configurator = null;
					if (!annotation.guiConfigurator().equals(CognitiveLogImporterConfigurator.class)) {
						configurator = (CognitiveLogImporterConfigurator) annotation.guiConfigurator().newInstance();
					}
					return new Pair<CognitiveLogImporter, CognitiveLogImporterConfigurator>(
							(CognitiveLogImporter) importer.newInstance(),
							configurator
						);
				} catch (InstantiationException | IllegalAccessException e) {
					Logger.instance().error(e);
				}
			}
		}
		return null;
	}
}
