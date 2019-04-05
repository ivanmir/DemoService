package com.example.odata.DemoService;

import com.sap.cloud.sdk.result.ElementName;

import lombok.Data;

@Data
public class PersonEntity {

	@ElementName( "UniqueId" )
	private final String uniqueId;
	@ElementName( "Name" )
	@Sap(filterable=true, sortable=true)
	private final String name;
	
}
