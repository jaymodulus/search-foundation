package com.bestbuy.search.foundation.solr;

import org.apache.solr.common.util.NamedList;
import org.apache.solr.search.SolrIndexSearcher;

//$Id$

public class DynamicIndexerEventListener implements org.apache.solr.core.SolrEventListener{

	public void newSearcher(SolrIndexSearcher newSearcher,
			SolrIndexSearcher currentSearcher) {
		//TODO: jah - comments and tests
		
		DynamicXmlUpdateRequestHandler dynamicIndexer = new DynamicXmlUpdateRequestHandler();

		Thread pollingThread = new Thread(dynamicIndexer);
		pollingThread.start();
	}

	public void init(NamedList args) {
		// TODO Auto-generated method stub
		
		
	}

	public void postCommit() {
		// TODO Auto-generated method stub
		
	} 

	
}
