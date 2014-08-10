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
import java.util.List;
import java.util.Scanner;

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

	// define Logger
	static Logger logger = Logger.getLogger(InstanceMapper.class.getName());

	/**
	 * 
	 */
	public InstanceMapper() {
	}

	/**
	 * reads the input raw file
	 * 
	 * @throws FileNotFoundException
	 */
	public static void readInputFile(int TOP_K_CANDIDATES) {
		String line = null;
		String[] arr = null;
		String oieSub = null;
		String oieProp = null;
		String oieObj = null;

		List<String> topkSubjects = null;
		List<String> topkObjects = null;

		BufferedWriter writer = null;

		long cnt = 0;
		try {
			@SuppressWarnings("resource")
			Scanner oieTriples = new Scanner(new FileInputStream(
					ReasoningClient.OIE_FILE_PATH));

			// create at the same location from where the input OIE file was
			// read from..
			writer = new BufferedWriter(new FileWriter(new File(
					ReasoningClient.OIE_FILE_PATH).getParent()
					+ "/Reverb.annotated.temporal.out"));

			// init DB
			DBWrapper.init(Constants.GET_WIKI_LINKS_APRIORI_SQL);

			logger.info("Writing output at   "
					+ new File(ReasoningClient.OIE_FILE_PATH).getParent()
					+ "/Reverb.annotated.temporal.out");

			while (oieTriples.hasNextLine()) {
				line = oieTriples.nextLine();
				arr = line.split(ReasoningClient.DELIMITER);
				oieSub = FileUtil.cleanse(arr[0]);
				oieProp = arr[1];
				oieObj = FileUtil.cleanse(arr[2]);

				// get top-k candidates of the subject
				topkSubjects = findTopKMatches(oieSub, TOP_K_CANDIDATES);

				// check if object is a time instance, if so write it out with
				// year annotation
				if (identifyTimeInstance(oieObj)) {

					// write out the time annotated entry
					FileUtil.createOutput(topkSubjects, oieProp, oieObj,
							FileUtil.getYear(oieObj), writer);

				} else {

					// get the topk instances for oieObj
					topkObjects = findTopKMatches(oieObj, TOP_K_CANDIDATES);

					// write out as it is
					FileUtil.createOutput(topkSubjects, oieProp, topkObjects,
							writer);
				}

				cnt++;

				if (cnt > 10000 && cnt % 10000 == 0) {
					logger.info("Completed processing of " + cnt + " lines..");
				}
			}
		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error("Problem wrriting out file: " + e.getMessage());
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
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
		if (arg.matches(".*\\d{4}+.*"))
			isTime = true;
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
