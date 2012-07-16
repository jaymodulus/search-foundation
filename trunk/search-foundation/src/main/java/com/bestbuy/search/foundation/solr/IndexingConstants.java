package com.bestbuy.search.foundation.solr;

public interface IndexingConstants {

	final String SOLR_TEST_DOC = "<add><doc><field name=\"skuid\">11133333</field></doc></add>";

	static String BBY_DIR = "/Users/jayhill/esr/customers/bby/";

	static String TARGET_DIR_NAME = BBY_DIR + "/bby-apache-solr-3.6.0/example/solr/data/incoming/";
	static String BASE_DIR = BBY_DIR + "bby-apache-solr-3.6.0/example/exampledocs/";
	static String BASE_FILE_NAME = "unit-test.xml";

	static String SOLR_URL = "http://localhost:8983/solr";
	static String SOLR_UPDATE_URL = SOLR_URL + "/update";
	static String SOLR_PING_URL = SOLR_URL + "/admin/ping";
	
}
