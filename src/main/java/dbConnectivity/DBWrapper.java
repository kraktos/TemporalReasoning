/**
 * 
 */

package dbConnectivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import utils.FileUtil;

/**
 * Wrapper class to initiate the DB operations. Used on top of
 * {@link DBConnection}
 * 
 * @author Arnab Dutta
 */
public class DBWrapper {

	public static final String GS_DELIMITER = "~~";

	// define Logger
	static Logger logger = Logger.getLogger(DBWrapper.class.getName());

	// DB connection instance, one per servlet
	static Connection connection = null;

	// DBCOnnection
	static DBConnection dbConnection = null;

	// prepared statement instance
	static PreparedStatement pstmt = null;

	static int batchCounter = 0;

	/**
	 * initiats the connection parameters
	 * 
	 * @param sql
	 */
	public static void init(String sql) {
		try {
			// instantiate the DB connection
			dbConnection = new DBConnection();

			// retrieve the freshly created connection instance
			connection = dbConnection.getConnection();

			// create a statement
			pstmt = connection.prepareStatement(sql);
			connection.setAutoCommit(false);

		} catch (SQLException ex) {
			ex.printStackTrace();
			logger.error("Connection Failed! Check output console"
					+ ex.getMessage());
		}
	}

	public static List<String> fetchTopKWikiTitles(String arg, int topK) {
		ResultSet rs = null;
		List<String> results = null;

		try {
			pstmt.setString(1, arg);
			pstmt.setInt(2, topK);

			// run the query finally
			rs = pstmt.executeQuery();
			results = new ArrayList<String>();

			while (rs.next()) {
				results.add(rs.getString(1) + "~~" + rs.getLong(2));
			}

		} catch (Exception e) {
			logger.error(" exception while fetching " + arg + " "
					+ e.getMessage());
		}

		return results;
	}

	public static List<String> fetchTopKLinksWikiPrepProb(String arg, int limit) {
		ResultSet rs = null;
		List<String> results = null;

		DecimalFormat decimalFormatter = new DecimalFormat("0.00000");

		try {
			pstmt.setString(1, arg.trim());
			pstmt.setString(2, arg.trim());
			pstmt.setInt(3, limit);

			rs = pstmt.executeQuery();
			results = new ArrayList<String>();

			while (rs.next()) {

				results.add(FileUtil.characterToUTF8((rs.getString(1))
						.replaceAll("\\s", "_"))
						+ "\t"
						+ decimalFormatter.format(rs.getDouble(2)));
			}

		} catch (Exception e) {
			logger.error(" exception while fetching " + arg + " "
					+ e.getMessage());
		}

		return results;
	}

	public static void shutDown() {

		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (Exception excp) {
			}
		}

		dbConnection.shutDown();

	}

	public static List<String> fetchTopReverbProperties(long topk) {
		List<String> types = new ArrayList<String>();

		try {
			pstmt.setLong(1, topk);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				types.add(rs.getString(1) + "\t" + rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return types;
	}

}
