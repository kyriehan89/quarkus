package com.test.api.tools;

public class CouchbaseConfig {

	private String username;
	private String password;
	private String nodes;
	private String bucketName;

	public String getUsername() {
		return username;
	}

	public CouchbaseConfig setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public CouchbaseConfig setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getNodes() {
		return nodes;
	}

	public CouchbaseConfig setNodes(String nodes) {
		this.nodes = nodes;
		return this;
	}

	public String getBucketName() {
		return bucketName;
	}

	public CouchbaseConfig setBucketName(String bucketName) {
		this.bucketName = bucketName;
		return this;
	}

	@Override
	public String toString() {
		return "CouchbaseConfig [username=" + username + ", password=" + password + ", nodes=" + nodes + ", bucketName="
				+ bucketName + "]";
	}
}
