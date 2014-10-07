/**
 * 
 */
package reasoning.mapping;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import utils.Constants;
import utils.FileUtil;
import client.ReasoningClient;
import dbConnectivity.DBWrapper;

/**
 * performs the instance mapping task
 * 
 * @author adutta
 * 
 */
public class InstanceMapper {

	private static final String SEPARATOR = "~";

	private static final int TOPK_REV_PROPS = 500;

	// define Logger
	static Logger logger = Logger.getLogger(InstanceMapper.class.getName());

	private static Map<String, String> temporalReverbProps = new HashMap<String, String>();
	private static Map<String, String> revProps = new HashMap<String, String>();

	private static String dbpProp;

	/**
	 * 
	 */
	public InstanceMapper() {
	}

	/**
	 * reads the input raw file
	 * 
	 * @param relevantReverbProperties
	 * 
	 * @throws FileNotFoundException
	 */
	public static void readInputFile(int TOP_K_CANDIDATES,
			Map<String, String> relevantReverbProperties,
			boolean temporalPropsKnown) {

		String line = null;
		String[] arr = null;
		String oieSub = null;
		String oieProp = null;
		String oieObj = null;
		double confidence = 0;
		String prop;

		Map<Long, String> DICTIONARY = new HashMap<Long, String>();

		List<String> topkSubjects = null;
		List<String> topkObjects = null;

		BufferedWriter writer = null;
		BufferedWriter dictWriter = null;

		long cnt = 1;

		try {
			@SuppressWarnings("resource")
			Scanner oieTriples = new Scanner(new FileInputStream(
					ReasoningClient.OIE_FILE_PATH));

			// create at the same location from where the input OIE file was
			// read from..
			writer = new BufferedWriter(new FileWriter(new File(
					ReasoningClient.OIE_FILE_PATH).getParent()
					+ "/2.Reverb.annotated.top" + TOP_K_CANDIDATES + ".out"));

			dictWriter = new BufferedWriter(new FileWriter(new File(
					ReasoningClient.OIE_FILE_PATH).getParent()
					+ "/2.Reverb.annotated.dictionary.top."
					+ TOP_K_CANDIDATES
					+ ".out"));

			logger.info("Writing output at   "
					+ new File(ReasoningClient.OIE_FILE_PATH).getParent()
					+ "/2.Reverb.annotated.top" + TOP_K_CANDIDATES + ".out");

			// // init DB
			DBWrapper.init(Constants.GET_WIKI_LINKS_APRIORI_SQL);

			while (oieTriples.hasNextLine()) {
				line = oieTriples.nextLine();
				if(line.indexOf("building owners") != -1)
					System.out.println();
				
				arr = line.split(ReasoningClient.DELIMITER);
				oieSub = FileUtil.cleanse(arr[0]);
				oieProp = arr[1];
				oieObj = FileUtil.cleanse(arr[2]);
				confidence = Double.parseDouble(arr[3]);

				String delimit = null;
				String value = oieSub + "\t" + oieProp + "\t" + oieObj + "\t"
						+ confidence;

				// only if the reverb property is a top-k property
				// if its a temporal property
				if (relevantReverbProperties.containsKey("T~" + oieProp)) {

					// relevantReverbProperties.containsKey("T~" + oieProp)
					// add the unique line to dictionary
					if (!DICTIONARY.containsKey(cnt)) {

						// set delimiter
						delimit = "T~";

						if (identifyTimeInstance(oieSub)
								&& !identifyTimeInstance(oieObj)) {

							// add the unique line to dictionary

							DICTIONARY.put(cnt, value);

							// // get top-k candidates of the subject
							topkObjects = findTopKMatches(oieObj,
									TOP_K_CANDIDATES);
							//
							// // write out the time annotated entry
							// FileUtil.createOutput(oieSub,
							// revProps.get(delimit + oieProp),
							// topkObjects, FileUtil.getYear(oieSub),
							// writer, confidence, cnt);

							FileUtil.createOutput(topkObjects,
									revProps.get(delimit + oieProp), oieSub,
									FileUtil.getYear(oieSub), writer,
									confidence, cnt);

							cnt++;

						} else if (!identifyTimeInstance(oieSub)
								&& identifyTimeInstance(oieObj)) {

							// add the unique line to dictionary
							DICTIONARY.put(cnt, value);

							// get top-k candidates of the subject
							topkSubjects = findTopKMatches(oieSub,
									TOP_K_CANDIDATES);

							// write out the time annotated entry
							// FileUtil.createOutput(topkSubjects,
							// revProps.get(delimit + oieProp), oieObj,
							// FileUtil.getYear(oieObj), writer,
							// confidence, cnt);

							FileUtil.createOutput(topkSubjects,
									revProps.get(delimit + oieProp), oieObj,
									FileUtil.getYear(oieObj), writer,
									confidence, cnt);

							cnt++;

						}
					}
				} else if (relevantReverbProperties
						.containsKey("NT~" + oieProp)) {

					if (!identifyTimeInstance(oieSub)
							&& !identifyTimeInstance(oieObj)) {

						// add the unique line to dictionary
						DICTIONARY.put(cnt, value);

						// set delimiter
						delimit = "NT~";

						// // get top-k candidates of the subject
						topkSubjects = findTopKMatches(oieSub, TOP_K_CANDIDATES);
						// get top-k candidates of the subject
						topkObjects = findTopKMatches(oieObj, TOP_K_CANDIDATES);
						// write out the time annotated entry

						if (revProps.get(delimit + oieProp).indexOf("_INV") == -1) {
							prop = revProps.get(delimit + oieProp);
							FileUtil.createOutput(topkSubjects, prop,
									topkObjects, writer, confidence, cnt);

						} else {
							prop = StringUtils
									.replace(revProps.get(delimit + oieProp),
											"_INV", "");

							FileUtil.createOutput(topkObjects, prop,
									topkSubjects, writer, confidence, cnt);
						}

						cnt++;
					}

				}
				if (cnt > 1000 && cnt % 1000 == 0) {
					logger.info("Completed processing of " + cnt + " lines..");
				}

			}

			for (Entry<Long, String> e : DICTIONARY.entrySet()) {
				dictWriter.write(e.getKey() + "\t" + e.getValue() + "\n");
			}

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error("Problem wrriting out file: " + e.getMessage());
		} finally {
			try {
				DBWrapper.shutDown();
				writer.close();
				dictWriter.flush();
				dictWriter.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	private static boolean isValidTemporalPropPattern(String oieProp,
			Map<String, String> relevantReverbProperties) {

		String pattern = null;
		String temporalIdentifier = null;

		for (Map.Entry<String, String> entry : relevantReverbProperties
				.entrySet()) {

			temporalIdentifier = entry.getKey().split(SEPARATOR)[0];
			pattern = " " + entry.getKey().split(SEPARATOR)[1] + " ";

			dbpProp = entry.getValue();
			if (StringUtils.containsIgnoreCase(oieProp, pattern)
					&& temporalIdentifier.equals("T"))
				return true;
		}
		return false;
	}

	private static boolean isValidNonTemporalPropPattern(String oieProp,
			Map<String, String> relevantReverbProperties) {

		String pattern = null;
		String temporalIdentifier = null;

		for (Map.Entry<String, String> entry : relevantReverbProperties
				.entrySet()) {

			temporalIdentifier = entry.getKey().split(SEPARATOR)[0];
			pattern = " " + entry.getKey().split(SEPARATOR)[1] + " ";

			dbpProp = entry.getValue();
			if (StringUtils.containsIgnoreCase(oieProp, pattern)
					&& temporalIdentifier.equals("NT"))
				return true;
		}
		return false;
	}

	/**
	 * load the clustered reverb properties in memory
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Map<String, String> loadProperties()
			throws FileNotFoundException {

		String[] arr = null;
		String line = null;
		String key = null;

		Scanner clusters = new Scanner(new FileInputStream(
				ReasoningClient.CLUSTERED_PROPERTIES_FILE_PATH));

		while (clusters.hasNextLine()) {
			line = clusters.nextLine();
			arr = line.split("\t");
			revProps.put(arr[0] + SEPARATOR + arr[1], arr[2]);
		}
		return revProps;
	}

	/**
	 * @return the temporalReverbProps
	 */
	public static Map<String, String> getTemporalReverbProps() {
		return temporalReverbProps;
	}

	/**
	 * get the list of top-k Reverb properties
	 * 
	 * @return List of properties
	 */
	public static Map<String, String> getTopkReverbProps() {

		// init DB
		DBWrapper.init(Constants.GET_DISTINCT_REVERB_PROPERTIES);

		Map<String, String> results = DBWrapper
				.fetchDistinctReverbProperties(TOPK_REV_PROPS);

		return (results != null) ? results : new HashMap<String, String>();

	}

	/**
	 * detect if the instance is an year TODO: include more sophisticated
	 * patterns
	 * 
	 * @param arg
	 * @return
	 */
	private static boolean identifyTimeInstance(String arg) {
		boolean isTime = false;
		if (arg.matches(".*\\d{4}+.*")) {
			Pattern p2 = Pattern.compile("[0-9]{4} [a-zA-Z]*");
			Matcher m2 = p2.matcher(arg);
			if (!m2.find())
				isTime = true;
		}
		return isTime;
	}

	/**
	 * queries DB for pre-stored topk matches
	 * 
	 * @param oieInst
	 * @param TOP_K_CANDIDATES
	 * @return
	 */
	public static List<String> findTopKMatches(String oieInst,
			int TOP_K_CANDIDATES) {
		// get the top-k concepts, confidence pairs
		return DBWrapper.fetchTopKLinksWikiPrepProb(oieInst, TOP_K_CANDIDATES);
	}

}
