package com.bestbuy.search.foundation.solr;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrCore;
import org.apache.solr.handler.XmlUpdateRequestHandler;
import org.apache.solr.util.plugin.SolrCoreAware;

//$Id$

public class DynamicXmlUpdateRequestHandler extends XmlUpdateRequestHandler implements IndexingConstants, SolrCoreAware, Runnable {
	private volatile boolean running = true;
	final int POLL_INTERVAL_MS = 5000;
	
	Logger logger = Logger.getLogger(DynamicIndexer.class);
	
	public DynamicXmlUpdateRequestHandler() {
		super();	
		
		//TODO: we're implementing SolrCoreAware, use that to get properties. Set properties in solrconfig.xml
	}

	@SuppressWarnings("rawtypes")
	public void init(NamedList args) {
		super.init(args);
		logger.info("DynamicXmlUpdateRequestHandler - init method hit");
	}

	public void inform(SolrCore core) {		
	}

	public void run() {		
		while(running) {
			File targetDir = new File(TARGET_DIR_NAME);
			File[] incomingFiles = targetDir.listFiles();
			logger.info("DynamicXmlUpdateRequestHandler - looking for Solr XML in directory: " + targetDir.getAbsolutePath());
						
			if (incomingFiles != null) {
				//We've found a new file to index
				for(int i = 0; i < incomingFiles.length; i++) {
					File foundFile = incomingFiles[i];	
					logger.info("Found Solr XML file: " + foundFile.getAbsolutePath());
					
					//Index the file
					//indexSolrXmlFile(foundFile);
					
					//verfifyIndexing(skuid); //TODO: alert how? how to track? stream logs? index log files?
					
					//Write to log that the file was indexed
					//logSuccess(foundFile.getName());
					
					//Delete the file after it was indexed (TODO: possibly copy to a holding location for BU?)
					deleteIndexedFile(foundFile);
				}
			}
			
			try {
				Thread.sleep(POLL_INTERVAL_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}							
		}
	}
	
	private void deleteIndexedFile(File foundFile) {
		if (! foundFile.delete()) {
			logger.error("Unable to delete file after indexing: " + foundFile.getAbsolutePath());
		}
		
	}

	public void stop() {
		logger.warn("DynamicXmlUpdateRequestHandler - Received stop signal. Stopping thread now.");
		running = false;
	}

}
