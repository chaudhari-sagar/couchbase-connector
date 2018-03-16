package com.pfizer.couchbase.internal;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

/**
 * This class represents an extension configuration, values set in this class
 * are commonly used across multiple operations since they represent something
 * core from the extension.
 */
@Operations(CouchbaseOperations.class)
@ConnectionProviders(CouchbaseConnectionProvider.class)
public class CouchbaseConfiguration {

	@DisplayName("Hosts")
	@Parameter
	private String hosts;

	@DisplayName("Bucket")
	@Parameter
	private String bucket;

	public String getHosts() {
		return hosts;
	}

	public String getBucket() {
		return bucket;
	}

}
