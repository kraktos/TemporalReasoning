/**
 * 
 */
package client;

import org.apache.log4j.Logger;

import reasoning.mapping.InstanceMapper;

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
	private static int TOP_K_CANDIDATES = 0;
	public static String DELIMITER = null;

	/**
	 * @param args
	 */

	public static void main(String[] args) {

		if (args.length < 3)
			System.err
					.println("Usage : java -jar Reason.jar <inputFilePath> <topK>");
		else {
			OIE_FILE_PATH = args[0];
			TOP_K_CANDIDATES = Integer.parseInt(args[1]);
			DELIMITER = args[2];

			logger.info("Reading OIE file from " + OIE_FILE_PATH);
			InstanceMapper.readInputFile(TOP_K_CANDIDATES);
		}
	}

}
