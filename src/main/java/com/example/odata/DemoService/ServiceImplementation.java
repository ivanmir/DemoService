package com.example.odata.DemoService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.sdk.service.prov.api.DataSourceHandler;
import com.sap.cloud.sdk.service.prov.api.EntityData;
import com.sap.cloud.sdk.service.prov.api.EntityMetadata;
import com.sap.cloud.sdk.service.prov.api.ExtensionHelper;
import com.sap.cloud.sdk.service.prov.api.annotations.ExtendRead;
import com.sap.cloud.sdk.service.prov.api.operations.Query;
import com.sap.cloud.sdk.service.prov.api.operations.Read;
import com.sap.cloud.sdk.service.prov.api.request.QueryRequest;
import com.sap.cloud.sdk.service.prov.api.request.ReadRequest;
import com.sap.cloud.sdk.service.prov.api.response.ErrorResponse;
import com.sap.cloud.sdk.service.prov.api.response.QueryResponse;
import com.sap.cloud.sdk.service.prov.api.response.ReadResponse;
import com.sap.cloud.sdk.service.prov.api.response.ReadResponseBuilder;

public class ServiceImplementation {
	
	private static Logger logger = LoggerFactory.getLogger(ServiceImplementation.class);
	
	@ExtendRead(serviceName="DemoService", entity =  "People" )
	public ReadResponse readProduct(ReadRequest readRequest, ExtensionHelper extensionHelper) throws Exception {
	         
	        logger.info("Going to read People");
	        DataSourceHandler handler = extensionHelper.getHandler();
	         
	        /*
	         * Get the EntityMetadata object from the request object. This is required because the executeRead method in the DatasourceHandler expects the list of elements to read,
	         * and complex/structured elements are expected to be flattened and given as single elements. EntityMetadata has the method getFlattenedElementNames which does exactly this.
	         * Also the entity name comes from the entityMetadata.
	         */
	        EntityMetadata entityMetadata = readRequest.getEntityMetadata();
	        EntityData entityData = handler.executeRead(entityMetadata.getName(),readRequest.getKeys(), entityMetadata.getFlattenedElementNames());
	        /*
	         * Form the the response object by first forming the ReadResponse Builder by calling ReadResponse.setSuccess();
	         * Then set the entityData into the builder and get the ReadResponse by calling the response() method.
	         */
	        ReadResponseBuilder builder = ReadResponse.setSuccess();
	        ReadResponse rr = builder.setData(entityData).response();
	         
	        return rr;
	         
	}	
	
	@Read(serviceName="DemoService", entity="People")
	public ReadResponse getPerson(ReadRequest request) {
		
		// retrieve the requested person from URI 
		Map<String, Object> keyPredicates = request.getKeys();
		Object keyObject = keyPredicates.get("UniqueId");
		Integer id = (Integer)keyObject;
		
		// search the requested person in the database
		List<Map<String, Object>> peopleList = getPeople();
		Optional<Map<String, Object>> requestedPersonMap = null;

		requestedPersonMap = peopleList
													.stream()
													.filter(e -> e.get("UniqueId").equals(id))
													.findFirst();
		if (requestedPersonMap.isPresent()) {
			Map<String, Object> requestedPerson = requestedPersonMap.get();
			return ReadResponse.setSuccess().setData(requestedPerson).response();
		} else {
			
			logger.error("Person with UniqueId " + id + " doesn't exist! Service request was invoked with invalid key value"); 
			ErrorResponse response = ErrorResponse.getBuilder()
					.setMessage("Person with UniqueId " + id + " doesn't exist!")
					.setStatusCode(404)
					.response();
			return ReadResponse.setError(response);
		}
	}

	@Query(serviceName="DemoService", entity="People")
	public QueryResponse getPeople(QueryRequest request) {

		List<Map<String, Object>> peopleMap = getPeople();

		return QueryResponse.setSuccess().setDataAsMap(peopleMap).response();
	}	

	private List<Map<String, Object>> getPeople(){
		List<Map<String, Object>> peopleMap = new ArrayList<Map<String, Object>>();
			
		peopleMap.add(createPerson(0, "Anna"));
		peopleMap.add(createPerson(1, "Berta"));
		peopleMap.add(createPerson(2, "Claudia"));
		peopleMap.add(createPerson(3, "Debbie"));
			
		return peopleMap;
	}
		
	private Map<String, Object> createPerson(int id, String name){
		Map<String, Object> personMap = new HashMap<String, Object>();
			
		personMap.put("UniqueId", id);
		personMap.put("Name", name);
			
		return personMap;
	}	
	
}
