package com.pfizer.couchbase.internal;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;

public class ClusterSingleton
{
    private static CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder().connectTimeout(10000)
            .socketConnectTimeout(10000).autoreleaseAfter(5000).build();
    
    private static ClusterSingleton singleton = null;  
    /**
     * Cluster
     */
    private Cluster cluster;

    /**
     * Bucket
     */
    private Bucket bucket;

    private ClusterSingleton(String hosts, String bucket) {
        cluster = CouchbaseCluster.create(env, hosts);
        this.bucket = cluster.openBucket(bucket);
    }

    public synchronized static ClusterSingleton getInstance(String hosts, String bucket) {
        if(singleton == null) {
            singleton = new ClusterSingleton(hosts, bucket);
        }
        return singleton;
    }

    public Bucket getBucket()
    {
        return this.bucket;
    }

}