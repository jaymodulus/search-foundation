package com.bestbuy.search.foundation.solr;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.bestbuy.search.foundation.solr.DynamicIndexer;

//$Id: TestDynamicIndexer.java 64237 2012-07-12 21:58:20Z jay.hill $

public class TestDynamicIndexer {
	final String USER_DIR = "/Users/jayhill/esr/customers/bby";
	final String SOLR_TEST_DOC = "<add><doc><field name=\"skuid\">11133333</field></doc></add>";
	final String TARGET_DIR_NAME = USER_DIR + "/solr-indexer/incoming/";
	final String BASE_DIR = USER_DIR + "/solr-indexer/test-file-base/";
	final String BASE_FILE_NAME = "unit-test.xml";
	final String SOLR_URL = "http://localhost:8983/solr";
	final String SOLR_UPDATE_URL = SOLR_URL + "/update";

	Logger logger = Logger.getLogger(TestDynamicIndexer.class);

	DynamicIndexer indexer = new DynamicIndexer();
	
	/*
	 * tests:
	 * - is solr up?  testConnectToSolr
	 * - is the firstSearch in solrconfig.xml?  testConfig
	 * - can we access the incoming dir?  testIsIncomingDirThere
	 * - does our polling thread notice new files?  testDetectNewFiles
	 * - can we index a file in the "hot" dir?   testIndexSimpleXml
	 */



	// @Test
	public void testConnectToSolr() {
		int statusCode = 0; // TODO: this approach is working, but doesn't seem
							// clean enough

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://localhost:8983/solr/admin/ping"); 

		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));

		try {
			// Execute the method.
			statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + method.getStatusLine());
				fail();
			}
		} catch (HttpException e) {
			logger.error("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			logger.error("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
			fail();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}

	}
	
	
	//@Test
	public void testDetectNewFiles() throws InterruptedException, IOException {
		// run poller
		logger.info("Test: starting the indexer.....");
		Thread pollingThread = new Thread(indexer);
		pollingThread.start();

		// copy file to the directory being polled
		logger.info("Test: copying the dummy file into place.....");
		copyTestFileToIncoming(BASE_DIR, BASE_FILE_NAME, TARGET_DIR_NAME);

		logger.info("Test: Stop the polling thread.");
		indexer.stop();

		// TODO: how to test this? or should it just be part of another test?
		//assertEquals(1,1);
	}

	@Test
	public void testIndexSimpleXml() throws UnsupportedEncodingException {
		int statusCode = 0; // TODO: this approach is working, but doesn't seem
							// clean enough

		HttpClient client = new HttpClient();

		// TODO: use embedded solr stub below
		PostMethod method = new PostMethod("http://localhost:8983/solr/update"); // TODO:make
																					// constant,
																					// then
																					// use
																					// properties

		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));

		method.addRequestHeader("Content-type", "text/xml; charset=UTF-8");
		method.setRequestEntity(new StringRequestEntity(SOLR_TEST_DOC)); // TODO:
																			// replace
																			// deprecation
		method.getParams().setParameter("commit", "true");

		try {
			// Execute the method.
			statusCode = client.executeMethod(method);

			if (statusCode != HttpStatus.SC_OK) {
				logger.error("Method failed: " + method.getStatusLine());
			}

			// Read the response body.
			byte[] responseBody = method.getResponseBody();
			String responseString = new String(responseBody);
			logger.info(responseString);
		} catch (HttpException e) {
			logger.error("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			logger.error("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
			fail();
		} finally {
			// Release the connection.
			method.releaseConnection();
		}

	}

	// /////////////////////////////////////////////

	private void copyTestFileToIncoming(String baseDir, String baseFileName,
			String targetDirectoryName) throws IOException {
		File baseFile = new File(baseDir + baseFileName);
		File newFile = new File(targetDirectoryName + baseFileName);

		if (!baseFile.exists()) {
			logger.info("Base file: " + baseFile.getAbsolutePath()
					+ " does not exist, exiting"); // TODO: create an Exception
													// for this
			fail();
		}

		logger.info("Copying " + baseFile.getAbsolutePath() + " to "
				+ newFile.getAbsolutePath());

		if (!newFile.exists()) {
			newFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(baseFile).getChannel();
			destination = new FileOutputStream(newFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} finally {
			if (source != null) {
				source.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}
}
