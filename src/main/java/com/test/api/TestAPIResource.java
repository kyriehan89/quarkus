package com.test.api;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.api.model.Agent;
import com.test.api.service.TestAPIService;


@ApplicationScoped
@Path("/test_api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestAPIResource {
	
	private Agent agent = new Agent();
	
	@Inject
	@ConfigProperty(name = "cb_nodes", defaultValue = "localhost")
	String nodes;// sample get data from config injection

	@Inject
	@ConfigProperty(name = "cb_uname", defaultValue = "test")
	String username;// sample get data from config injection

	@Inject
	@ConfigProperty(name = "cb_pass", defaultValue = "test")
	String password;// sample get data from config injection

	@Inject
	@ConfigProperty(name = "cb_bucket", defaultValue = "test")
	String bucket;// sample get data from config injection

	@Inject
	@ConfigProperty(name = "query", defaultValue = "test")
	String defaultQuery;// sample get data from config injection

	@Inject
	TestAPIService service;

	private static final Logger LOG = LoggerFactory.getLogger(TestAPIResource.class);

	@PostConstruct
	void onStartAPI() {
		LOG.info("start initialize couchbase...");

		service.init(nodes, username, password, bucket);
	}

	@PreDestroy
	void onEndAPI() {
		LOG.info("terminating couchbase...");

		service.clearInstance();
	}

	
	
	@GET
	@Path("/getAgentDetail/{agentNo}")
	public Set<Agent> getAgentDetail(@PathParam("agentNo") String agentNo) {
		// LOG.info("start get getAgentDetail:" + agentNo);
		 
		 
		return service.getAgentDetail(agentNo);
		//return agent;
	}
	
}
