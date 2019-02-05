package cognitiveprom.log.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import com.google.common.base.Joiner;

import cognitiveprom.annotations.Importer;
import cognitiveprom.exceptions.LogIOException;
import cognitiveprom.log.CognitiveLog;
import cognitiveprom.log.utils.XCognitiveLogHelper;
import cognitiveprom.view.io.TSVImporterConfigurator;

/**
 * Class to import a {@link CognitiveLog} starting from a TSV file generated by
 * Tobii Studio
 * 
 * @author Andrea Burattin
 */
@Importer(
	name = "Tobii TSV file",
	fileExtension = "tsv",
	guiConfigurator = TSVImporterConfigurator.class
)
public class TSVCognitiveImporter extends CognitiveLogImporter {

	private static SimpleDateFormat dateParser = new SimpleDateFormat("HH:mm:ss.SSS");
	private Set<String> namesOfAOIs = new HashSet<String>();
	private CSVFormat format = CSVFormat.TDF;
	
	/**
	 * Sets the names of the AOIs to be identified
	 * 
	 * @param AOIs the names of the AOIs as in Tobii Studio
	 */
	public void addAOIs(String... AOIs) {
		for (String AOI : AOIs) {
			namesOfAOIs.add(AOI);
		}
	}
	
	/**
	 * Sets a customized version of the {@link CSVFormat}. By default,
	 * {@link CSVFormat#TDF} is used
	 * 
	 * @param format the customized CSV format
	 */
	public void setTSVFormat(CSVFormat format) {
		this.format = format;
	}
	
	@Override
	public CognitiveLog load(File fileName) throws LogIOException {
		if (namesOfAOIs.isEmpty()) {
			throw new LogIOException("No AOI set for import!");
		}
		
		Reader reader;
		try {
			reader = new InputStreamReader(new BOMInputStream(new FileInputStream(fileName)), "UTF-8");
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			throw new LogIOException(e.getMessage());
		}

		XLog log = XCognitiveLogHelper.prepareLog();
		XConceptExtension.instance().assignName(log, fileName.getName());
		
		CSVParser parser = null;
		try {
			parser = new CSVParser(reader, format);
			
			Map<String, Set<Integer>> aoiToIndexes = new HashMap<String, Set<Integer>>();
			Pattern aoiNamePattern = Pattern.compile("AOI\\[(" + Joiner.on('|').join(namesOfAOIs) + ")\\]Hit");
			Map<String, XTrace> subjectsToTraces = new HashMap<String, XTrace>();
			
			int FIELD_TIMESTAMP = -1;
			int FIELD_GAZE_DURATION = -1;
			int FIELD_PARTICIPANT_NAME = -1;
			
			for (CSVRecord record : parser) {
				if (aoiToIndexes.isEmpty()) {
					
					// first row is to map column names to proper values
					for (int i = 0; i < record.size(); i++) {
						String columnName = record.get(i);
						Matcher matcher = aoiNamePattern.matcher(columnName);
						if (matcher.find()) {
							String header = matcher.group(1);
							if (!aoiToIndexes.containsKey(header)) {
								aoiToIndexes.put(header, new HashSet<Integer>());
							}
							aoiToIndexes.get(header).add(i);
						} else if ("LocalTimeStamp".equals(columnName)) {
							FIELD_TIMESTAMP = i;
						} else if ("GazeEventDuration".equals(columnName)) {
							FIELD_GAZE_DURATION = i;
						} else if ("ParticipantName".equals(columnName)) {
							FIELD_PARTICIPANT_NAME = i;
						}
					}
					
					if (FIELD_TIMESTAMP == -1) {
						throw new LogIOException("Cannot find timestamp field (should be named \"" + FIELD_TIMESTAMP + "\"");
					}
					if (FIELD_PARTICIPANT_NAME == -1) {
						throw new LogIOException("Cannot find participant name field (should be named \"" + FIELD_PARTICIPANT_NAME + "\"");
					}
					if (FIELD_GAZE_DURATION == -1) {
						throw new LogIOException("Cannot find gaze duration field (should be named \"" + FIELD_GAZE_DURATION + "\"");
					}
					
				} else {
				
					// process the subject
					String subject = record.get(FIELD_PARTICIPANT_NAME);
					XTrace processInstance = subjectsToTraces.get(subject);
					if (processInstance == null) {
						processInstance = XCognitiveLogHelper.insertTraceForSubject(log, subject);
						subjectsToTraces.put(subject, processInstance);
					}
					for (String aoi : namesOfAOIs) {
						if (aoiToIndexes.containsKey(aoi)) {
							for (Integer index : aoiToIndexes.get(aoi)) {
								if ("1".equals(record.get(index))) {
									try {
										processInstance.insertOrdered(
												XCognitiveLogHelper.constructAOIFixation(
														aoi,
														dateParser.parse(record.get(FIELD_TIMESTAMP)),
														Long.parseLong(record.get(FIELD_GAZE_DURATION))));
									} catch (ParseException e) {
										throw new LogIOException(e.getMessage());
									}
									break;
								}
							}
						}
					}
				}
			}
			
		} catch (IOException e) {
			throw new LogIOException(e.getMessage());
		} finally {
			try {
				if (parser != null) {
					parser.close();
				}
			} catch (IOException e) {
				throw new LogIOException(e.getMessage());
			}
		}
		
		// finalization activities
		XCognitiveLogHelper.sortXLog(log);
		XCognitiveLogHelper.mergeEventsWithSameName(log);
		
		return new CognitiveLog(log);
	}
}
