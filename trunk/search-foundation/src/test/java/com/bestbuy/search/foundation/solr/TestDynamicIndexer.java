package com.bestbuy.search.foundation.solr;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.junit.Test;
import org.xml.sax.SAXException;

//$Id: TestDynamicIndexer.java 64237 2012-07-12 21:58:20Z jay.hill $

public class TestDynamicIndexer implements IndexingConstants{
	Logger logger = Logger.getLogger(TestDynamicIndexer.class);

	DynamicXmlUpdateRequestHandler indexer = new DynamicXmlUpdateRequestHandler();

	/*
	 * tests: 
	 * [X] is solr up? testConnectToSolr 
	 * [-] is the firstSearcher in solrconfig.xml? testConfig 
	 * [-] can we access the incoming dir?  testIsIncomingDirThere 
	 * [-] does the event listener startup with solr and launch the polling thread? testListener 
	 * [-] does our polling thread notice new files? testDetectNewFiles 
	 * [X] can we index a file in the "hot" dir? testIndexSimpleXml
	 */

	@Test
	public void testConnectToSolr() throws IOException, ParserConfigurationException, SAXException {
		
		// Note that the following property could be set through JVM level arguments too
		System.setProperty("solr.solr.home","/Users/jayhill/esr/customers/bby/bby-apache-solr-3.6.0/example/solr");
		CoreContainer.Initializer initializer = new CoreContainer.Initializer();
		CoreContainer coreContainer = initializer.initialize();
		EmbeddedSolrServer server = new EmbeddedSolrServer(coreContainer, "");

		int statusCode = 0;

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(SOLR_PING_URL);

		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));

		try {
			// Execute the method.
			statusCode = client.executeMethod(method);
			assertEquals(statusCode, HttpStatus.SC_OK);
		} catch (HttpException e) {
			logger.error("Fatal protocol violation: " + e.getMessage());
			e.printStackTrace();
			fail("HttpException: " + e.getMessage());
		} catch (IOException e) {
			logger.error("Fatal transport error: " + e.getMessage());
			e.printStackTrace();
			fail("IOException: " + e.getMessage());
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
	}

	@Test
	public void testIndexSimpleXml() throws UnsupportedEncodingException {
		int statusCode = 0; // TODO: this approach is working, but doesn't seem
							// clean enough

		HttpClient client = new HttpClient();

		// TODO: use embedded solr stub below
		PostMethod method = new PostMethod(SOLR_UPDATE_URL);

		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));

		method.addRequestHeader("Content-type", "text/xml; charset=UTF-8");
		method.setRequestEntity(new StringRequestEntity(SOLR_TEST_DOC)); // TODO: replace deprecation
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
		
		//TODO: verify that the target file was indexed

	}

	///////////////////////////////////////////////

	private void copyTestFileToIncoming(String baseDir, String baseFileName,
			String targetDirectoryName) {
		logger.info("copyTestFile moving in the test file");

		File baseFile = new File(baseDir + baseFileName);
		File newFile = new File(targetDirectoryName + baseFileName);

		if (!baseFile.exists()) { // for this
			fail("Base file: " + baseFile.getAbsolutePath()
					+ " does not exist, exiting");
		}

		logger.info("Copying " + baseFile.getAbsolutePath() + " to "
				+ newFile.getAbsolutePath());

		if (!newFile.exists()) {
			try {
				newFile.createNewFile();
			} catch (IOException e) {
				fail(e.getMessage());
			}
		}

		FileChannel source = null;
		FileChannel destination = null;

		try {
			source = new FileInputStream(baseFile).getChannel();
			destination = new FileOutputStream(newFile).getChannel();
			destination.transferFrom(source, 0, source.size());
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		} finally {
			if (source != null) {
				try {
					source.close();
				} catch (IOException e) {
					fail(e.getMessage());
				}
				if (destination != null) {
					try {
						destination.close();
					} catch (IOException e) {
						fail(e.getMessage());
					}
				}
			}
		}
	}
}
