/**
 * 
 */
package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import utils.FileUtil;

/**
 * Client for initiating the pipeline
 * 
 * @author adutta
 * 
 */
public class ReasoningClient {

	/**
	 * 
	 */
	public ReasoningClient() {

	}

	private static String OIE_FILE_PATH = null;
	private static int TOP_K_CANDIDATES = 0;

	/**
	 * @param args
	 */

	public static void main(String[] args) {

		if (args.length < 2)
			System.err
					.println("Usage : java -jar Reason.jar <inputFilePath> <topK>");
		else {
			OIE_FILE_PATH = args[0];
			TOP_K_CANDIDATES = Integer.parseInt(args[1]);
			readInputFile();
		}
	}

	/**
	 * reads the input raw file
	 * 
	 * @throws FileNotFoundException
	 */
	private static void readInputFile() {
		try {
			ArrayList<ArrayList<String>> oieTriples = FileUtil
					.genericFileReader(new FileInputStream(OIE_FILE_PATH), ";",
							false);

			for (ArrayList<String> line : oieTriples) {
				for (String element : line) {
					System.out.print(element + "\t");
				}
				System.out.print("\n");
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}

}
