package com.pfizer.couchbase.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.mule.runtime.extension.api.annotation.metadata.OutputResolver;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.MediaType;

import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.RawJsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.query.N1qlParams;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.view.SpatialViewQuery;
import com.couchbase.client.java.view.SpatialViewResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is a container for operations, every public method in this class
 * will be taken as an extension operation.
 */
public class CouchbaseOperations {

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * Upsert
	 *
	 * @param id
	 *            The id of the document to upsert.
	 * @param json
	 *            The document to upsert.
	 * @return The returned document
	 */
	@MediaType(value = ANY, strict = false)
	public String upsert(@Config CouchbaseConfiguration configuration, String id, String json) {
		return ClusterSingleton.getInstance(configuration.getHosts(), configuration.getBucket()).getBucket()
				.upsert(RawJsonDocument.create(id, json)).content();

	}
	
	/**
	 * Upsert Stream
	 *
	 * @param id
	 *            The id of the document to upsert.
	 * @param json
	 *            The document to upsert.
	 * @return The returned document
	 * @throws Exception 
	 */
	@MediaType(value = ANY, strict = false)
	public String upsertStream(@Config CouchbaseConfiguration configuration, String id, InputStream json) throws Exception {
		String contents = IOUtils.toString(json, StandardCharsets.UTF_8.name());
		return ClusterSingleton.getInstance(configuration.getHosts(), configuration.getBucket()).getBucket()
				.upsert(RawJsonDocument.create(id, contents)).content();
	}

	/**
	 * Get Document By Id
	 *
	 * @param id
	 *            The id of the document to get.
	 * @return The returned JSON document
	 * @throws JsonProcessingException
	 */
	@MediaType(value = ANY, strict = false)
	public String getDocumentById(@Config CouchbaseConfiguration configuration, String id) throws JsonProcessingException {
		JsonDocument doc = ClusterSingleton.getInstance(configuration.getHosts(), configuration.getBucket()).getBucket()
				.get(id);
		if (doc != null && (!doc.content().isEmpty())) {
			return mapper.writeValueAsString(
					ClusterSingleton.getInstance(configuration.getHosts(), configuration.getBucket()).getBucket()
							.get(id).content().toMap());
		} else {
			return null;
		}
	}

	/**
	 * N1QL query
	 *
	 * @param parameters
	 *            List of parameters for the parameterized query.
	 * @param parameterizedQuery
	 *            The parameterized query.
	 * @param createPrimaryIndex
	 *            Whether or not create a primary index.
	 * @param ignoreIfPrimaryIndexExists
	 *            Ignore creating a primary index if one already exists.
	 * @param adhoc
	 *            Allows to specify if this query is adhoc or not.
	 * @return N1qlQueryResult containing each row.
	 */
	@MediaType(value = ANY, strict = false)
	@OutputResolver(output = N1QLQueryOutputResolver.class)
	public N1qlQueryResult n1qlQuery(@Config CouchbaseConfiguration configuration, String parameterizedQuery, List<?> parameters, boolean createPrimaryIndex,
			boolean ignoreIfPrimaryIndexExists, boolean adhoc) {
		ClusterSingleton.getInstance(configuration.getHosts(), configuration.getBucket()).getBucket().bucketManager()
				.createN1qlPrimaryIndex(ignoreIfPrimaryIndexExists, createPrimaryIndex);
		N1qlParams params = N1qlParams.build().adhoc(adhoc);
		N1qlQueryResult result = ClusterSingleton.getInstance(configuration.getHosts(), configuration.getBucket())
				.getBucket().query(N1qlQuery.parameterized(parameterizedQuery, JsonArray.from(parameters), params));
		return result;
	}

	/**
	 * Spatial View query
	 *
	 * @param designDocumentName
	 *            The name of the design document.
	 * @param spatialViewName
	 *            The name of the spatial view.
	 * @param startRange
	 *            The startRange values.
	 * @param endRange
	 *            The endRange values.
	 * @return The returned document
	 */
	@MediaType(value = ANY, strict = false)
	@OutputResolver(output = N1QLQueryOutputResolver.class)
	public SpatialViewResult spatialViewQuery(@Config CouchbaseConfiguration configuration, String designDocumentName,
			String spatialViewName, List<Integer> startRanges, List<Integer> endRanges) {
		SpatialViewQuery query = SpatialViewQuery.from(designDocumentName, spatialViewName);
		if (startRanges != null) {
			query.startRange(JsonArray.from(startRanges));
		}
		if (endRanges != null) {
			query.endRange(JsonArray.from(endRanges));
		}
		SpatialViewResult result = ClusterSingleton.getInstance(configuration.getHosts(), configuration.getBucket())
				.getBucket().query(query);

		return result;
	}

}
