package com.test.api.service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.json.JsonObject;
import com.test.api.model.Agent;
import com.test.api.query.StringQuery;
import com.test.api.service.CouchbaseInstanceService;


@ApplicationScoped
public class TestAPIService {
	
	protected CouchbaseInstanceService couchbaseService;
	private static final Logger LOG = LoggerFactory.getLogger(TestAPIService.class);


    
	public void init(String nodes,String username,String password,String bucketName) {	
		try {
			couchbaseService = CouchbaseInstanceService.getInstance();
			couchbaseService.init(nodes,username,password,bucketName);
		    		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error(e.toString());
		}
		
	}
	
	
	public Set<Agent> getAgentDetail(String agentNo) {
		Set<Agent> resultAgents = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>())) ;
		String query = StringQuery.getAgentDetail;
		
		JsonObject param = JsonObject.create();
		param.put("agent_number", "TEST::"+agentNo);
		
        try {        	
           JsonObject resultQuery = couchbaseService.findObjectData(query,param);
           
           Agent resultAgent = new Agent();
           if(resultQuery!=null) {
        	   resultAgent.setAgent_number(resultQuery.getString("agent_number"));
               resultAgent.setAgent_type(resultQuery.getString("agent_type"));
               resultAgent.setAcc_name(resultQuery.getString("acc_name"));
               resultAgent.setAccount_no(resultQuery.getString("account_no"));
               resultAgent.setAgent_address1(resultQuery.getString("agent_address1"));
               resultAgent.setAgent_address2(resultQuery.getString("agent_address2"));
               resultAgent.setAgent_address3(resultQuery.getString("agent_address3"));
               resultAgent.setAgent_address4(resultQuery.getString("agent_address4"));
               resultAgent.setAgent_address5(resultQuery.getString("agent_address5"));
               resultAgent.setAgent_name(resultQuery.getString("agent_name"));
               resultAgent.setAgent_office_cd(resultQuery.getString("agent_office_cd"));
               resultAgent.setAgent_status(resultQuery.getString("agent_status"));
               resultAgent.setCity(resultQuery.getString("city"));
               resultAgent.setClient_number(resultQuery.getString("client_number"));
               resultAgent.setClient_phone(resultQuery.getString("client_phone"));
               resultAgent.setClient_postal_cd(resultQuery.getString("client_postal_cd"));
               resultAgent.setDob(resultQuery.getString("dob"));
               resultAgent.setEducation(resultQuery.getString("education"));
               resultAgent.setEmail(resultQuery.getString("email"));
               resultAgent.setMobile_phone1(resultQuery.getString("mobile_phone1"));
               resultAgent.setReligion(resultQuery.getString("religion"));
               resultAgent.setSalutation(resultQuery.getString("salutation"));
               resultAgent.setSex(resultQuery.getString("sex"));
               resultAgent.setUpdated(resultQuery.getString("updated"));
               
               
               resultAgents.add(resultAgent);
               
           }
           return resultAgents;
        } catch (Exception e) {
        	LOG.error(e.toString());
        }

		return null;
	}
	
	
	public void clearInstance() {
		if(couchbaseService!=null)
			couchbaseService.clearInstance();
	}
	
	

}