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
	static Logger logger = Logger.getLogger(DBWrapper.class.getName());

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

		try {
			Scanner oieTriples = new Scanner(new FileInputStream(
					ReasoningClient.OIE_FILE_PATH));

			// create at the same location from where the input OIE file was
			// read from..
			writer = new BufferedWriter(new FileWriter(new File(
					ReasoningClient.OIE_FILE_PATH).getParent()
					+ "/Reverb.annotated.temporal.out"));

			// init DB
			DBWrapper.init(Constants.GET_WIKI_LINKS_APRIORI_SQL);

			while (oieTriples.hasNextLine()) {
				line = oieTriples.nextLine();
				arr = line.split(ReasoningClient.DELIMITER);
				oieSub = FileUtil.cleanse(arr[0]);
				oieProp = arr[1];
				oieObj = FileUtil.cleanse(arr[2]);

				topkSubjects = findTopKMatches(oieSub, TOP_K_CANDIDATES);
				topkObjects = findTopKMatches(oieObj, TOP_K_CANDIDATES);

				FileUtil.createOutput(topkSubjects, oieProp, topkObjects,
						writer);
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Problem wrriting out file: " + e.getMessage());
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

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
