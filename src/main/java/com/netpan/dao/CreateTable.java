package com.netpan.dao;

import java.io.IOException;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;

import com.netpan.dao.basedao.HbaseDao;
import com.netpan.dao.conn.HbaseConn;
import com.netpan.util.Constants;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;
import org.apache.hadoop.hbase.util.Bytes;

public class CreateTable extends HbaseDao{
	/**
     * 创建表
     * @category create 'tableName','family1','family2','family3'
     * @param tableName
     * @param family
     * @throws Exception
     */
    public static void createTable(String tableName, String[] family) throws IOException {
        Admin admin = HbaseConn.getConn().getAdmin();

        TableName tn = TableName.valueOf(tableName);
		TableDescriptorBuilder builder=TableDescriptorBuilder.newBuilder(tn);
        //HTableDescriptor desc = new HTableDescriptor(tn);
        for (int i = 0; i < family.length; i++) {
			ColumnFamilyDescriptorBuilder column_builder=ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(family[i]));
			builder.setColumnFamily(column_builder.build());
            //desc.addFamily(new HColumnDescriptor(family[i]));
        }
        if (admin.tableExists(tn)) {
            System.out.println("createTable => table Exists!");
            admin.disableTable(tn);
            admin.deleteTable(tn);
            admin.createTable(builder.build());
        } else {
            admin.createTable(builder.build());
            System.out.println("createTable => create Success! => " + tn);
        }
    }
    
	@SuppressWarnings("unused")
	public static void main(String args[]) throws IOException {

		//因为hbase需要rowkey作为查询依据，所以需要创建对应索引表

		//获取全局rowkey的表
		String[] gid = {"gid"};//列族名称
		createTable(Constants.TABLE_GID, gid); 				//表名 gid_disk


		//用户相关信息的表
		String[] user_id = {"id"};//列族名称
		String[] id_user = {"user"};//列族名称
		String[] email_user = {"user"};//列族名称
		createTable(Constants.TABLE_USERId, user_id); 		//表名 user_id 用户主表
		createTable(Constants.TABLE_IDUSER, id_user); 		//表名 id_user 索引表
		createTable(Constants.TABLE_EMAILUSER, email_user); //表名 email_user 索引表


		//网盘文件的表
		String[] file = {"file"};//列族名称
		String[] user_file = {"file"};//列族名称
		createTable(Constants.TABLE_FILE, file); 			//表名  file      文件主表
		createTable(Constants.TABLE_USERFILE, user_file);	//表名  user_file 索引表


		//用户关注表
		String[] follow = {"name"};//列族名称
		String[] followed = {"userid"};//列族名称
		createTable(Constants.TABLE_FOLLOW, follow);		//表名  follow	  用户关注主表
		createTable(Constants.TABLE_FOLLOWED, followed);	//表名  followed   索引表


		//网盘文件分享表
		String[] share = {"content"};//列族名称
		String[] shareed = {"shareid"};//列族名称
		createTable(Constants.TABLE_SHARE, share);			//表名  share		文件分享主表
		createTable(Constants.TABLE_SHAREED, shareed);		//表名  shareed		索引表
	}
}
