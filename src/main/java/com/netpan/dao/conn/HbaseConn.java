package com.netpan.dao.conn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class HbaseConn {
	private Configuration configuration = null;
	private Connection connection = null;
    
    private static class SingletonHolder{
    	private static final HbaseConn INSTANCE = new HbaseConn();
    }
    
    private HbaseConn(){
    	try {
    		configuration = HBaseConfiguration.create();
            //改成本机的hbase的zk地址
        	configuration.set("hbase.zookeeper.quorum", "192.168.140.140:2181");
        	//configuration.set("hbase.rootdir", "hdfs://oracle:9000/hbase");
        	connection = ConnectionFactory.createConnection(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static final Connection getConn() {
    	return SingletonHolder.INSTANCE.connection;
    }
}