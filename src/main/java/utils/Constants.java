/**
 * 
 */

package utils;

import java.text.DecimalFormat;

/**
 * This class stores a set of constants required for the application
 * 
 * @author Arnab Dutta
 */
public class Constants {
	public static enum OIE {
		NELL, REVERB
	}

	public static final String DELIMIT = "\",";

	/**
	 * delimiter for the the CSV file coming as input from extraction engines'
	 * output
	 */
	public static final String DELIMIT_IE_FILE = ",";

	public static String VIRTUSO_ENDPOINT = "http://wifo5-38.informatik.uni-mannheim.de:8890/sparql";

	public static String YAGO_KB = "http://yago-knowledge.org";

	public static String YAGO_NS = "http://yago-knowledge.org/resource/";

	/**
	 * DBPedia End point URL
	 */
	public static String DBPEDIA_SPARQL_ENDPOINT_LOCAL = "http://wifo5-32.informatik.uni-mannheim.de:8891/sparql";

	public static String DBPEDIA_SPARQL_ENDPOINT = "http://dbpedia.org/sparql";

	public static String DBPEDIA_SPARQL_ENDPOINT_LIVE_DBP = "http://live.dbpedia.org/sparql";

	// ExperimentAutomation.DBPEDIA_SPARQL_ENDPOINT;
	// "http://wifo5-32.informatik.uni-mannheim.de:8890/sparql";
	// "http://wifo5-32.informatik.uni-mannheim.de:8890/sparql";

	// "http://dbpedia.org/sparql";
	// "http://live.dbpedia.org/sparql";

	// *****************DIRECTORY LOCATIONS
	// ************************************************

	/**
	 * location for storing the predicate distribution patterns based on some
	 * integer values of the subjects and objects of the instances
	 */
	public static final String DBPEDIA_PREDICATE_DISTRIBUTION = "/home/arnab/Work/data/DBPedia/property";

	/**
	 * location of all the raw CSV files dumped from DBPedia SPARQL endpoint
	 */
	public static final String DBPEDIA_DATA_DIR = "/home/arnab/Work/data/DBPedia/data";

	/**
	 * location of the directory where the DBPEdia indices for entities are
	 * stored
	 */
	public static final String DBPEDIA_ENT_INDEX_DIR = "/home/arnab/Work/data/DBPedia/indexFiles";

	/**
	 * location of the directory where the NELL indices for entities are stored.
	 * Gold Standard creation
	 */
	public static final String NELL_ENT_INDEX_DIR = "/home/arnab/Work/data/NELL/indexFiles";

	/**
	 * location of the directory where the DBPedia SO indices for entities are
	 * stored. Baseline creation
	 */
	public static final String DBPEDIA_INFO_INDEX_DIR = "/home/arnab/Work/data/DBPedia/infoIndex";

	/**
	 * location of the directory where the ReVerb indices for entities are
	 * stored. Gold Standard creation
	 */
	public static final String REVERB_ENT_INDEX_DIR = "/home/arnab/Work/data/ReVerb/index";

	/**
	 * location of the directory where the indices for predicates are stored
	 */
	public static final String DBPEDIA_PROP_INDEX_DIR = "/home/arnab/Work/data/DBPedia/propIndexFiles";

	/**
	 * Delimiter to separate the URI and the lable of DBPedia entries
	 */
	public static final String DBPEDIA_DATA_DELIMIT = "~!~";

	/**
	 * only the URIs with the following header will be used for indexing
	 */
	public static final String DBPEDIA_HEADER = "http://dbpedia.org/";

	/**
	 * Filter out the YAGO links
	 */
	public static final String YAGO_HEADER = "http://dbpedia.org/class/yago";

	// *****************INDEXING STRATEGIES
	// ************************************************

	/**
	 * allowable text for indexing, do not index Chinese, Japanese, Korean,
	 * Russian etc labels
	 */
	public static final String ALLOWED_ENGLISH_TEXT = "[^\\w_\\s()'.:,]";

	/**
	 * Filter to remove certain punctuations from the uri
	 */
	public static final String URI_FILTER = "[():,.\\s'-]";

	/**
	 * Filter to remove certain punctuations from the labels
	 */
	public static final String LABEL_FILTER = "[():,']";

	/**
	 * change here to use different analyzers
	 */
	// public static final Analyzer LUCENE_ANALYZER = new
	// StandardAnalyzer(Version.LUCENE_40);

	/**
	 * flag to determine whether to concat to old indices or recreate all from
	 * scratch
	 */
	public static final boolean EMPTY_INDICES = true;

	/**
	 * Flag to denote if indexing is to be done or query on old indices
	 */
	public static final boolean INDEX_AGAIN = false;

	// *****************FETCH STRATEGIES
	// ***************************************************
	// tweaking these can dramatically effect the query response time

	/**
	 * percentage length of common (non-fuzzy) prefix in the user query you want
	 * to match, higher value makes it to search over smaller data matches not
	 * on all of them. Here it means 80% of the query term should contain in the
	 * result sets
	 */
	public static final float PREFIX_LENGTH_PERCENT = 0.1F;

	/**
	 * default 50% similarity and above, lower this value to fetch even lesser
	 * similar items
	 */
	public static final double SIMILARITY = 100.00;

	/**
	 * change the value to fetch these many records, Lucene uses this to fetch
	 * maximum these many matching documents
	 */
	public static final int MAX_RESULTS = 50;

	/**
	 * Number of top k matching elements you wish to retrieve
	 */
	// public static final int TOPK = 5;

	/**
	 * Sample query to test the indexed DBPedia data
	 */
	public static final String SAMPLE_QUERY = "shaw";

	// *****************IE Engines output locations
	// ***************************************************

	/**
	 * location of the output file generated the IE Engine ReVerb
	 */
	public static final String NELL_DATA_PATH = "/home/arnab/Work/data/NELL/Nell.csv";

	/**
	 * location of the output file generated the IE Engine ReVerb
	 */
	public static final String REVERB_DATA_PATH = "src/main/resources/input/highConfidenceReverbData.csv";

	/**
	 * Delimiter used to parse the ReVerb extracted tuples
	 */
	public static final String REVERB_IE_DELIMIT = ";";

	/**
	 * Delimiter used to parse the ReVerb extracted tuples
	 */
	public static final String NELL_IE_DELIMIT = ",";

	/**
	 * output location of the predicate list after calculating jaccard score for
	 * each
	 */
	public static final String PREDICATE_FREQ_FILEPATH = "/home/arnab/Work/data/NELL/predFreq_2.txt";

	// *****************WEB INTERFACE
	// PARAMETES***************************************************

	/**
	 * If this is turned on the then the system performs a predictive search
	 * else just a simple search based on the input terms
	 */
	public static final boolean PREDICTIVE_SEARCH_MODE = true;

	/**
	 * only those entities with a match of value higher than this will be taken
	 * into consideration for further processing.
	 */
	public static final double THRESHOLD_SCORE = 80;

	// *****************Database Parameters
	// PARAMETES***************************************************
	public static final String INSERT_FACT_SQL = "INSERT INTO \"UNCERTAIN_KB\"(\"SUB\", \"PRED\", \"OBJ\", \"CONFIDENCE\") VALUES (?, ?, ?, ?)";

	public static final String INSERT_PROPERTY_DOMAIN_RANGE_SQL = "INSERT INTO \"PREDICATE_DOMAIN_RANGE\"(\"PREDICATE\", \"DOMAIN\", \"RANGE\") VALUES (?, ?, ?)";

	public static final String GET_WIKI_STAT = "select distinct entity from stats where anchor=?";

	public static final String GET_LINK_COUNT = "select count(*) as cnt  from link_anchors l, title_2_id t where t.title = ? and l.anchor=? and l.target = t.id";

	/**
	 * SQL to insert a gold standard instance for NELL
	 */
	public static final String INSERT_GOLD_STANDARD = "INSERT INTO goldStandard (E_SUB, E_PRED, E_OBJ, D_SUB, D_PRED, D_OBJ, SUB_LINK_CNT, OBJ_LINK_CNT ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


	/**
	 * given a surface form, fetch top titles it refers to
	 */
	public static final String GET_WIKI_TITLES_SQL = "select URI, SUM(COUNT) as cnt from wikiPrep where SF = ? group by BINARY URI order by cnt desc limit ?";

	public static final String GET_MOST_FREQUENT_SENSE = "select  URI from wikiPrep  where SF =? group by BINARY URI order by COUNT desc limit 1";
	/**
	 * SQL to fetch the probabilities of the same as links from terms to
	 * concepts
	 */
	public static final String GET_WIKI_LINKS_APRIORI_SQL = "select  URI, (SUM(COUNT)/(select  SUM(COUNT) from wikiPrep  where SF =?)) as p from wikiPrep  where SF =? group by BINARY URI order by p desc limit ?";

	public static final String GET_REFINED_MAPPINGS_SQL = "select DBP_SUB, DBP_OBJ from OIE_REFINED where OIE_SUB=? and OIE_PRED=? and OIE_OBJ=?";

	public static final String GET_NELL_CONF = "select confidence from nell where subject = ? and predicate = ? and object = ?";

	// "select  t.title, count(*) as cnt from link_anchors l, title_2_id t where l.anchor=? and l.target=t.id group by t.title order by cnt desc limit 2";

	// "select URI from surfaceForms where SF =? order by PROB desc" ;
	// "select  t.title, count(*) as cnt from link_anchors l, title_2_id t where l.anchor=? and l.target=t.id group by t.title order by cnt desc limit ?";

	public static final String INSERT_SURFACE_FORMS_SQL = "INSERT INTO surfaceForms_2_uri (uri, surface, count) VALUES (?, ?, ?)";

	public static final String INSERT_DB_SURFACE_FORMS_SQL = "INSERT INTO surfaceForms (URI, SF, PROB) VALUES (?, ?, ?)";

	/**
	 * fetch the top matching DBPedia predicates co-occurring with a given NELL
	 * predicate
	 */
	// "select *, count(*) as cnt from goldStandardClean where E_PRED =? group by D_PRED order by cnt desc"
	// ;
	// "select count(*) as cnt, D_PRED from goldStandardClean where E_PRED =? group by D_PRED order by cnt desc";

	/**
	 * fetch all the NELL predicates matched with the DBPedia dataset
	 */
	public static final String GET_NELL_PREDICATES = "select E_PRED, count(*) as cnt from goldStandardClean group by E_PRED order by cnt desc";

	public static String GET_DISTINCT_REVERB_PROPERTIES = "select distinct(PROP), count(*) as c from REVERB_WEIGHTED_TYPES group by PROP order by c desc limit ?";

	public static final String GET_TOP_PROPERTIES_REVERB = "select distinct PROP, count(*) as c from REVERB_WEIGHTED_TYPES group by PROP order by c desc limit ?";

	/*
	 * DB Details
	 */

	// DB Driver name
	public static String DRIVER_NAME = "com.mysql.jdbc.Driver";

	// Url to conenct to the Database
	// public static String CONNECTION_URL = "jdbc:mysql://134.155.86.39/";
	public static String CONNECTION_URL = "jdbc:mysql://134.155.95.117:3306/";

	// name of the database
	public static String DB_NAME = "wikiStat";

	// user of the database. Make sure this user is created for the DB
	public static String DB_USER = "root";

	// password for the user
	public static String DB_PWD = "mannheim1234";

	public static DecimalFormat formatter = new DecimalFormat("#.############");

}
