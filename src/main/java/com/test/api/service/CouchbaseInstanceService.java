
package com.test.api.service;
import com.couchbase.client.core.cnc.tracing.ThresholdRequestTracer;
import com.couchbase.client.core.env.IoConfig;
import com.couchbase.client.core.env.PasswordAuthenticator;
import com.couchbase.client.core.env.SaslMechanism;
import com.couchbase.client.core.error.QueryException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.EnumSet;

public class CouchbaseInstanceService {

	protected static CouchbaseInstanceService instance = null;
	private static final Logger LOG = LoggerFactory.getLogger(CouchbaseInstanceService.class);
	protected ClusterEnvironment env;
	protected Cluster cluster;
	protected Bucket bucket;

	public static CouchbaseInstanceService getInstance() {
		// using singleton for single instance
		try {
			if (instance == null) {
				LOG.info("initialize CouchbaseInstanceService...");
				instance = new CouchbaseInstanceService();
			} else {
				LOG.info("CouchbaseInstanceService exists...");
			}
		} catch (Exception e) {
			LOG.error("Error CouchbaseInstanceService connection:" + e);
			instance = null;
		}

		return instance;
	}

	public JsonObject findObjectData(String query,JsonObject param) {
		QueryResult result = null;
		try {
			result = cluster.query(query, QueryOptions.queryOptions().adhoc(false).parameters(param));
			 if(result.rowsAsObject().size()>0) {
				 JsonObject objectResult = result.rowsAsObject().get(0);
				 return objectResult;
			 }else {
				 return null;
			 }
		} catch (QueryException e) {
			System.out.println("error:"+e);
			LOG.warn("Query failed with exception: " + e);
		}
		return null;
	}
	
	public void clearInstance() {

		LOG.info("start clearInstance");
		try {
			if (cluster != null) {
				LOG.info("terminating instance...");
				cluster.disconnect();
				env.shutdown();
				cluster = null;
			}
		} catch (Exception e) {
			LOG.error("Error clearInstance connection:" + e);
			cluster = null;
		}
		LOG.info("Finish clearInstance");

	}

	public void init(String nodes, String username, String password, String bucketName) {
		env = ClusterEnvironment.builder()
				.ioConfig(IoConfig.idleHttpConnectionTimeout(Duration.ofSeconds(4)))
				.requestTracer(ThresholdRequestTracer.builder(null)
				.queryThreshold(Duration.ofSeconds(12)).build())
				.build();
		
		
		PasswordAuthenticator authenticator = PasswordAuthenticator
				  .builder()
				  .username(username)
				  .password(password)
				  // enables only the PLAIN authentication mechanism, used with LDAP
				  .allowedSaslMechanisms(EnumSet.of(SaslMechanism.PLAIN))
				  .build();

		cluster = Cluster.connect(nodes,ClusterOptions
			    .clusterOptions(authenticator)
			    .environment(env));
		bucket = cluster.bucket(bucketName);
		
		
		
		
	}

	
	private static void logQuery(String query) {
		LOG.info("Executing Query: {}", query);
	}

}
