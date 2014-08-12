/**
 * 
 */
package client;

import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import reasoning.mapping.InstanceMapper;
import clustering.PropertyCluster;

/**
 * Client for initiating the pipeline
 * 
 * @author adutta
 * 
 */
public class ReasoningClient {
	// define Logger
	static Logger logger = Logger.getLogger(ReasoningClient.class.getName());

	/**
	 * 
	 */
	public ReasoningClient() {

	}

	public static String OIE_FILE_PATH = null;
	public static String FEED_PROPS_FILE_PATH = null;
	private static int TOP_K_CANDIDATES = 0;
	public static String DELIMITER = null;

	/**
	 * @param args
	 * @throws IOException
	 */

	public static void main(String[] args) throws IOException {

		if (args.length < 4)
			System.err
					.println("Usage : java -jar Reason.jar <inputFilePath> <topK>");
		else {
			OIE_FILE_PATH = args[0];
			FEED_PROPS_FILE_PATH = args[1];
			TOP_K_CANDIDATES = Integer.parseInt(args[2]);
			DELIMITER = args[3];

			logger.info("Reading OIE file from " + OIE_FILE_PATH);

			// read the top frequent most reverb properties..we don't need all
			// the properties
			// call DBand retrieve a set of TOPK properties
			Map<String, String> topKRevbProps = InstanceMapper
					.getTopkReverbProps();

			logger.info("Retrieved top 500 reverb proeprties");
			InstanceMapper.readInputFile(TOP_K_CANDIDATES, topKRevbProps);

			// read the the temporal reverb properties
			Map<String, String> temporalRevbProps = InstanceMapper
					.getTemporalReverbProps();

			// use the feed dbpedia properties to create clusters
			PropertyCluster.performClustering(temporalRevbProps);

		}
	}
}
