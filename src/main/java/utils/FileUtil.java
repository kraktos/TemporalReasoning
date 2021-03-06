package utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to peform IO operations
 * 
 * @author Arnab Dutta
 */
public class FileUtil {

	static DecimalFormat df = new DecimalFormat("#.######");

	/**
	 * generic filereader.. good for small files since it is loaded into memory
	 * completely.
	 * 
	 * @param inputStream
	 * @param valueSeperator
	 * @param hasHeader
	 * @return
	 */
	public static ArrayList<ArrayList<String>> genericFileReader(
			InputStream inputStream, String valueSeperator, boolean hasHeader) {

		Scanner scan;
		scan = new Scanner(inputStream, "UTF-8");

		if (hasHeader) {
			scan.nextLine();
		}
		ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();

		while (scan.hasNextLine()) {

			ArrayList<String> tokens = new ArrayList<String>();

			String line = scan.nextLine();
			StringTokenizer st = new StringTokenizer(line, valueSeperator);

			while (st.hasMoreTokens()) {
				tokens.add(st.nextToken());
			}
			lines.add(tokens);
		}

		scan.close();

		return lines;
	}

	/**
	 * oie instance cleaning
	 * 
	 * @param arg
	 * @return
	 */
	public static String cleanse(String arg) {
		if (arg.indexOf(":") != -1)
			arg = arg.substring(arg.indexOf(":") + 1, arg.length());

		return arg.replaceAll("\\_+", " ");
	}

	/**
	 * encodes a string with special character to one with UTF-8 encoding
	 * 
	 * @param arg
	 * @return
	 */
	public static String characterToUTF8(String arg) {
		try {
			return URLEncoder.encode(arg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return arg;
	}

	/**
	 * decodes a string with UTF-8 encoding to special character
	 * 
	 * @param arg
	 * @return
	 */
	public static String utf8ToCharacter(String arg) {
		try {
			return URLDecoder.decode(arg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e2) {
			e2.printStackTrace();
		}
		return arg;
	}

	/**
	 * writer routine
	 * 
	 * @param topkSubjects
	 * @param oieProp
	 * @param topkObjects
	 * @param writer
	 * @throws IOException
	 */
	public static void createOutput(List<String> topkSubjects, String oieProp,
			List<String> topkObjects, BufferedWriter writer, double confidence,
			long cnt) throws IOException {

		// iterate the subj-object pairwise combinations
		for (String candSubj : topkSubjects) {
			for (String candObj : topkObjects) {
				writer.write(cnt
						+ "\t"
						+ df.format(Double.valueOf(candSubj.split("\t")[1])
								* Double.valueOf(candObj.split("\t")[1]))
						+ "\t" + candSubj.split("\t")[0] + "\t" + oieProp
						+ "\t" + candObj.split("\t")[0] + "\n");
			}

			// flush it out
			writer.flush();
		}

	}

	/**
	 * overloaded function
	 * 
	 * @param topkSubjects
	 * @param oieProp
	 * @param oieSubj
	 * @param year
	 * @param writer
	 * @param confidence
	 * @throws IOException
	 */
	public static void createOutput(List<String> topkSubjects, String oieProp,
			String oieSubj, String year, BufferedWriter writer,
			double confidence, long cnt) throws IOException {

		// iterate the subj-object pairwise combinations
		for (String candSubj : topkSubjects) {

			writer.write(cnt + "\t"
					+ df.format(Double.valueOf(candSubj.split("\t")[1])) + "\t"
					+ candSubj.split("\t")[0] + "\t" + oieProp + "\t" + oieSubj
					+ "\t[" + year + ", " + year + "]\n");
		}
		// flush it out
		writer.flush();
	}

	/**
	 * overloaded function
	 * 
	 * @param topkSubjects
	 * @param oieProp
	 * @param oieSubj
	 * @param year
	 * @param writer
	 * @param confidence
	 * @param cnt
	 * @throws IOException
	 */
	public static void createOutput(String oieSub, String oieProp,
			List<String> topkObjects, String year, BufferedWriter writer,
			double confidence, long cnt) throws IOException {

		// iterate the subj-object pairwise combinations
		for (String candObj : topkObjects) {

			writer.write(cnt + "\t"
					+ df.format(Double.valueOf(candObj.split("\t")[1])) + "\t"
					+ oieSub + "\t" + oieProp + "\t" + candObj.split("\t")[0]
					+ "\t[" + year + ", " + year + "]\n");
		}
		// flush it out
		writer.flush();
	}

	/**
	 * return the year from an input string
	 * 
	 * @param oieInst
	 * @return
	 */
	public static String getYear(final String oieInst) {
		final Pattern p = Pattern.compile("(\\d{4})");
		final Matcher m = p.matcher(oieInst);
		if (m.find()) {
			// Pattern p2 = Pattern.compile("[0-9]{4} [a-zA-Z]*");
			// Matcher m2 = p.matcher(oieInst);
			// if (!m2.find())
			return m.group(0);
		}
		return "";
	}

}
