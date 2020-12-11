package com.test.api.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.ReactiveBucket;
import com.couchbase.client.java.ReactiveCluster;
import com.couchbase.client.java.ReactiveCollection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;
import com.couchbase.client.java.query.ReactiveQueryResult;

import reactor.core.publisher.Mono;

public final class CouchbaseDriver {

	private final ReactiveCluster cluster;
	private final ReactiveBucket bucket;
	private final ReactiveCollection collection;
	private static final String CREATED_AT = "createdAt";
	private static final String UPDATE_AT = "lastUpdatedAt";
	private final SimpleDateFormat upsertDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public CouchbaseDriver(CouchbaseConfig appConfig) {
		ClusterOptions options = ClusterOptions.clusterOptions(appConfig.getUsername(), appConfig.getPassword());
		cluster = ReactiveCluster.connect(appConfig.getNodes(), options);
		bucket = cluster.bucket(appConfig.getBucketName());
		collection = bucket.defaultCollection();
	}

	public Mono<ReactiveQueryResult> runQuery(String statement, JsonObject params) {
		QueryOptions options = QueryOptions.queryOptions();
		return runQuery(statement, params, options);
	}

	public Mono<ReactiveQueryResult> runQuery(String statement, JsonObject params, QueryOptions options) {
		options.parameters(params);
		return cluster.query(statement, options);
	}

	public Mono<ReactiveQueryResult> runQueryConsistent(String statement, JsonObject params) {
		QueryOptions options = QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS);
		return runQuery(statement, params, options);
	}

	public Mono<GetResult> getDocById(String id) {
		return collection.get(id);
	}

	public Mono<MutationResult> delete(String id) {
		return collection.remove(id);
	}

	/*public Mono<JsonObject> upsertDoc(JsonObject doc) {
		if (!doc.containsKey(CREATED_AT) || doc.getString(CREATED_AT).isBlank()) {
			doc.put(CREATED_AT, upsertDateFormat.format(new Date()));
			if (!doc.containsKey("id") || doc.getString("id").isBlank()) {
				doc.put("id", UUID.randomUUID().toString());
			}
		}
		doc.put(UPDATE_AT, upsertDateFormat.format(new Date()));
		ReactiveCollection cbCollection = bucket.defaultCollection();
		return cbCollection.upsert(doc.getString("id"), doc).map(mr -> doc);
	}*/
}
