package com.bestbuy.search.foundation.solr;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.core.CoreContainer;
import org.apache.solr.util.AbstractSolrTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class TestEmbeddedSolr extends AbstractSolrTestCase {
	
	private String solrHome = "/Users/jayhill/esr/customers/bby/bby-apache-solr-3.6.0/example/solr";

    @Before 
    public void initialize() {
    	System.setProperty("solr.solr.home", solrHome);
    	System.setProperty("solr.data.dir", "/Users/jayhill/esr/customers/bby/bby-apache-solr-3.6.0/example/solr/data/");
    	System.out.println("-----------solr home: " + System.getProperty("solr.solr.home"));
     }
    
    @Test
    public void testShit() {
    	assertEquals(2,2);
    }

//    
//    @After
//    public void tearDown() throws Exception {
//    
//    }
    
//	@Test
//	public void testEmbeddedSolr() {
//	
//		System.out.println("000000");
//		
//		// Note that the following property could be set through JVM level arguments too
//		  System.setProperty("solr.solr.home", "/Users/jayhill/esr/customers/bby/bby-apache-solr-3.6.0/example/solr");
//		  CoreContainer.Initializer initializer = new CoreContainer.Initializer();
//		  CoreContainer coreContainer = null;
//		  
//		  System.out.println("99999");
//		  
//		try {
//			coreContainer = initializer.initialize();
//			System.out.println("11111");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			System.out.println("222");
//
//			e.printStackTrace();
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			System.out.println("333");
//
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			System.out.println("444");
//
//			e.printStackTrace();
//		}
//		
//		System.out.println("888");
//		
//		EmbeddedSolrServer server = new EmbeddedSolrServer(coreContainer, "");
//		
//	}
//
    
    @Override
    public String getSolrHome() {
    	return solrHome;
    }
    
	@Override
	public String getSchemaFile() {
		// TODO Auto-generated method stub
		return "/Users/jayhill/esr/customers/bby/bby-apache-solr-3.6.0/example/solr/conf/schema.xml";
	}

	@Override
	public String getSolrConfigFile() {
		// TODO Auto-generated method stub
		return "/Users/jayhill/esr/customers/bby/bby-apache-solr-3.6.0/example/solr/conf/solrconfig.xml";
	}


}
