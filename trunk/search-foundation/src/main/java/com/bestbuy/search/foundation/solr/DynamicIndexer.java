package com.bestbuy.search.foundation.solr;

import java.io.File;

//$Id: DynamicIndexer.java 64182 2012-07-12 19:39:39Z jay.hill $


public class DynamicIndexer implements Runnable {
	private volatile boolean running = true;
	final String TARGET_DIR_NAME = "/Users/jayhill/esr/customers/bby/solr-indexer/incoming/";
	final String LOG_FILE_NAME = "";

	public void run() {
		System.out.println("Starting loop.......");
		
		while(running) {
			File targetDir = new File(TARGET_DIR_NAME);
			File[] incomingFiles = targetDir.listFiles();
			System.out.println("Looking for files in directory: " + targetDir.getAbsolutePath());
			
			if (incomingFiles != null) {
				//We've found a new file to index
				for(int i = 0; i < incomingFiles.length; i++) {
					File foundFile = incomingFiles[i];	
					System.out.println("Found file: " + foundFile.getAbsolutePath());
					
					//Index the file
					indexSolrXmlFile(foundFile);
					
					//Write to log that the file was indexed
					logSuccess(foundFile.getName());
					
					//Delete the file after it was indexed (TODO: possibly copy to a holding location?)
					deleteIndexedFile(foundFile);
				}
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}							
		}
	}

	private void indexSolrXmlFile(File foundFile) {
		System.out.println("Starting indexing of file: " + foundFile.getAbsolutePath());
		
		
		
	}
	
	private void logSuccess(String name) {
		System.out.println("Successfully indexed: " + name);		
	}
	
	private void deleteIndexedFile(File foundFile) {
		System.out.println("Deleting indexed file: " + foundFile.getAbsolutePath());
		foundFile.delete();		
	}

	public void stop() {
		System.out.println("POLLING LOOP RECEIVED STOP COMMAND");
		running = false;
	}
}
