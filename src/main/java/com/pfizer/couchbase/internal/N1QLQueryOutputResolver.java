package com.pfizer.couchbase.internal;

import static org.mule.metadata.api.model.MetadataFormat.JAVA;
import org.mule.metadata.api.builder.BaseTypeBuilder;
import org.mule.metadata.api.model.BinaryType;
import org.mule.metadata.api.model.MetadataType;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.metadata.MetadataContext;
import org.mule.runtime.api.metadata.MetadataResolvingException;
import org.mule.runtime.api.metadata.resolving.OutputTypeResolver;

/**
 * Couchbase {@link OutputTypeResolver} implementation for the basic operations that
 * always return a {@link BinaryType}.
 *
 * @since 1.0
 */
public class N1QLQueryOutputResolver implements OutputTypeResolver<Object> {

	private static final BinaryType BINARY_TYPE = BaseTypeBuilder.create(JAVA).binaryType().build();

	@Override
	public String getCategoryName() {
		return "COUCHBASE";
	}

	@Override
	public MetadataType getOutputType(MetadataContext context, Object key)
			throws MetadataResolvingException, ConnectionException {
		return BINARY_TYPE;
	}

}
