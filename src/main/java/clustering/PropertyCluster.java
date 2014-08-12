/**
 * 
 */
package clustering;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import client.ReasoningClient;
import reasoning.mapping.SimilatityWebService;
import utils.Constants;

/**
 * Clusters a bunch of properties
 * 
 * @author adutta
 * 
 */
public class PropertyCluster {

	// define Logger
	static Logger logger = Logger.getLogger(PropertyCluster.class.getName());

	static Map<String, String> feedDBPediaProperties = new HashMap<String, String>();

	/**
	 * 
	 */
	public PropertyCluster() {

	}

	/**
	 * test point
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		BufferedWriter writerWordNet = null;
		try {
			writerWordNet = new BufferedWriter(new FileWriter(
					new File(args[0]).getParent() + "/tdbp.pairwise.sim"));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		loadFeeds();
		getPairwiseSimScore(feedDBPediaProperties, feedDBPediaProperties,
				writerWordNet, true);
	}

	/**
	 * perform clustering with the DBpedia feeds
	 * 
	 * @param revbProps
	 * @param string
	 * @throws IOException
	 */
	public static void performClustering(Map<String, String> revbProps)
			throws IOException {

		BufferedWriter writerRevSims = null;
		BufferedWriter writerDbpSims = null;
		BufferedWriter writerRevDbpSims = null;

		try {
			writerRevSims = new BufferedWriter(new FileWriter(new File(
					ReasoningClient.FEED_PROPS_FILE_PATH).getParent()
					+ "/trvb.pairwise.sim.csv"));
			writerDbpSims = new BufferedWriter(new FileWriter(new File(
					ReasoningClient.FEED_PROPS_FILE_PATH).getParent()
					+ "/tdbp.pairwise.sim.csv"));
			writerRevDbpSims = new BufferedWriter(new FileWriter(new File(
					ReasoningClient.FEED_PROPS_FILE_PATH).getParent()
					+ "/trvb.dbp.pairwise.sim.csv"));

		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		loadFeeds();

		logger.info("Writing sim scores to "
				+ ReasoningClient.FEED_PROPS_FILE_PATH
				+ "/tdbp.pairwise.sim.csv");
		getPairwiseSimScore(feedDBPediaProperties, feedDBPediaProperties,
				writerDbpSims, true);

		logger.info("Writing sim scores to "
				+ ReasoningClient.FEED_PROPS_FILE_PATH
				+ "/trvb.dbp.pairwise.sim.csv");
		getPairwiseSimScore(revbProps, feedDBPediaProperties, writerRevDbpSims,
				false);

		logger.info("Writing sim scores to "
				+ ReasoningClient.FEED_PROPS_FILE_PATH
				+ "/trvb.pairwise.sim.csv");
		getPairwiseSimScore(revbProps, revbProps, writerRevSims, true);

		try {

			writerRevSims.close();
			writerDbpSims.close();
			writerRevDbpSims.close();

		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * @param feedDBPediaProperties3
	 * @param arg2Map
	 * @param writer
	 * @param check
	 * @throws IOException
	 */
	public static void getPairwiseSimScore(Map<String, String> arg1Map,
			Map<String, String> arg2Map, BufferedWriter writer, boolean check)
			throws IOException {

		logger.info("Size of Arg1 = " + arg1Map.size());
		logger.info("Size of Arg2 = " + arg2Map.size());

		for (Entry<String, String> eOuter : arg1Map.entrySet()) {
			for (Entry<String, String> eInner : arg2Map.entrySet()) {
				if (check) {
					if (eOuter.getKey().hashCode() > eInner.getKey().hashCode()) {
						try {
							// based on Wordnet scores
							getWordNetSimilarityScores(eOuter.getKey(),
									eInner.getKey(), writer);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
					}
				} else {
					try {
						// based on Wordnet scores
						getWordNetSimilarityScores(eOuter.getKey(),
								eInner.getKey(), writer);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
			writer.flush();
		}
	}

	/**
	 * set of feed DBPedia properties, can be read from a file
	 */
	private static void loadFeeds() {

		Scanner dbpProps;
		try {
			dbpProps = new Scanner(new FileInputStream(
					ReasoningClient.FEED_PROPS_FILE_PATH));

			while (dbpProps.hasNextLine()) {
				feedDBPediaProperties.put(dbpProps.nextLine(), "");
			}

			logger.info("Loaded " + feedDBPediaProperties.size()
					+ " DBpedia feed properties into memeory.. ");

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * call the web service to compute the inter phrase similarity
	 * 
	 * @param properties
	 * 
	 * @param properties
	 * @param id2
	 * @param id
	 * @throws Exception
	 */
	private static void getWordNetSimilarityScores(String key1, String key2,
			BufferedWriter writerWordNet) throws Exception {

		double score = SimilatityWebService.getSimScore(key1, key2);

		writerWordNet.write(key1 + "\t" + key2 + "\t"
				+ Constants.formatter.format(score) + "\n");
	}
}
