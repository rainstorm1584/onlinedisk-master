package com.netpan;


import org.apache.avro.TestAnnotation;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class HDFSDemo {

    public static void main(String[] args) {

    }

    @Test
    public void testMkdir() throws URISyntaxException,IOException,InterruptedException{

        URI uri = new URI("hdfs://192.168.140.140:9000");

        Configuration configuration = new Configuration(); //core-site.xml hdfs-site.xml
        configuration.set("dfs.replication",String.valueOf(1));


        String user = "hadoop";

        FileSystem fileSystem = FileSystem.get(uri, configuration,user);

        fileSystem.mkdirs(new Path("/est/it"));

        fileSystem.close();
    }
    @Test
    public void testCopyFromLocalFile() throws URISyntaxException, IOException, InterruptedException {

        //1、获取文件系统
        Configuration configuration = new Configuration();
        configuration.set("dfs.replication", "2");
        FileSystem fs = FileSystem.get(new URI("hdfs://192.168.140.140:9000"), configuration, "hadoop");

        //执行上传文件操作
        fs.copyFromLocalFile(new Path("D:\\test\\123.txt"), new Path("/east/it/"));

        //关闭资源
        fs.close();

    }
    @Test
    public void testCopyToLocalFile() throws URISyntaxException, IOException, InterruptedException {

        //1、获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://192.168.140.140:9000"), configuration, "hadoop");

        //2、执行下载操作
        // boolean delSrc 指是否将原文件删除
        // Path src 指要下载的文件路径
        // Path dst 指将文件下载到的路径
        // boolean useRawLocalFileSystem 是否开启文件校验
        fs.copyToLocalFile(false, new Path("/east/it/123.txt"), new Path("D:\\test1"), true);
        /* 这个命令会将HDFS中的/dateset/456文件下载到本地f:\text\lqs.txt文件。
           fs是FileSystem对象,代表一个文件系统,这里应该是HDFS文件系统。
           copyToLocalFile方法用于从文件系统中拷贝文件到本地。
           它有4个参数:
           - 删除源文件,这里设置为false,表示下载后HDFS中的文件不删除
           - 源文件路径,这里是/dateset/456,表示下载HDFS中的/dateset/456文件
           - 本地目标文件路径,这里是f:\text\lqs.txt,表示下载到本地的f:\text目录下的lqs.txt文件
           - 覆盖目标文件,这里设置为true,表示如果本地已存在lqs.txt文件,会覆盖*/
        //3、关闭资源
        fs.close();

    }
    @Test
    public void testDelete() throws URISyntaxException, IOException, InterruptedException {
        //1、获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://192.168.140.140:9000"), configuration, "hadoop");

        //执行删除操作
        fs.delete(new Path("/west"), true);
        /*这个命令会删除HDFS中的/test目录及其下所有子文件和子目录。
         fs是FileSystem对象,代表一个文件系统,这里应该是HDFS文件系统。
         delete方法用于删除文件系统中的文件或目录。
         它有2个参数:
         - 路径:指定要删除的文件或目录的路径,这里是/test目录
         - 是否递归删除:如果待删除对象是目录,则设置为true表示递归删除目录下所有子文件和子目录,设置为false表示只删除空目录
         所以,该命令的作用是:
         - 删除HDFS中的/test目录
         - 由于第二个参数recursive设置为true,所以会递归删除/test目录下的所有子文件和子目录
         - /test目录及其下所有内容都会被删除*/
        //关闭资源
        fs.close();

    }
    @Test
    public void testRename() throws URISyntaxException, IOException, InterruptedException {

        //1、获取文件系统
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://192.168.140.140:9000"), configuration, "hadoop");

        //2、修改文件名称
        fs.rename(new Path("/test/test1/lqs.txt"), new Path("/test/test1/lqstest.txt"));

        //移动文件到指定目录，注意：目的目录必须要存在，否则会移动失败
        boolean result = fs.rename(new Path("/test/test1/lqs.txt"), new Path("/lqs/test/test.txt"));
        if (result) {
            System.out.println("移动文件成功");
        } else {
            System.out.println("移动文件失败");
        }

        //移动文件并修改移动后的文件名。注意：目的目录必须要存在，否则会移动失败
        boolean result1 = fs.rename(new Path("/xiyo/test/test1/lqs.txt"), new Path("/lqs/test/test.txt"));
        if (result1) {
            System.out.println("移动文件并修改移动后的文件名成功");
        } else {
            System.out.println("移动文件并修改移动后的文件名失败");
        }

        //3、关闭资源
        fs.close();

    }
    @Test
    public void testListStatus() throws URISyntaxException, IOException, InterruptedException {
        Configuration configuration = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://192.168.140.140:9000"), configuration, "hadoop");

        FileStatus[] fileStatuses = fs.listStatus(new Path("/hadoop"));

        for (FileStatus fileStatus : fileStatuses) {
            //如果是文件者输出名字
            if (fileStatus.isFile()) {
                System.out.println("-:" + fileStatus.getPath());
            } else {
                System.out.println("d:" + fileStatus.getPath());
            }


            System.out.println("++++++++++++++" + fileStatus.getPath() + "++++++++++++++");
            System.out.println(fileStatus.getPermission());
            System.out.println(fileStatus.getOwner());
            System.out.println(fileStatus.getGroup());
            System.out.println(fileStatus.getLen());
            System.out.println(fileStatus.getModificationTime());
            System.out.println(fileStatus.getReplication());
            System.out.println(fileStatus.getBlockSize());
            System.out.println(fileStatus.getPath().getName());

            //获取块信息
            long blockLocations = fileStatus.getBlockSize();
            System.out.println(Arrays.toString(new long[]{blockLocations}));

        }

        fs.close();

    }
    @Test
    public void testListFiles() throws URISyntaxException, IOException, InterruptedException {

        //1、获取文件系统,获取的的是hadoop的配置信息文件：Configuration: core-default.xml, core-site.xml, hdfs-default.xml, hdfs-site.xml, mapred-default.xml, mapred-site.xml, yarn-default.xml, yarn-site.xml
        Configuration configuration=new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://192.168.140.140:9000"), configuration, "hadoop");

        //2、获取文件详情.注意是获取的文件，而不是文件夹（目录）
        //final boolean recursive 是否递归查找
        RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"), true);

        System.out.println(listFiles.hasNext());
        while (listFiles.hasNext()){
            LocatedFileStatus fileStatus=listFiles.next();

            System.out.println("++++++++++++++"+fileStatus.getPath()+"++++++++++++++");
            System.out.println(fileStatus.getPermission());
            System.out.println(fileStatus.getOwner());
            System.out.println(fileStatus.getGroup());
            System.out.println(fileStatus.getLen());
            System.out.println(fileStatus.getModificationTime());
            System.out.println(fileStatus.getReplication());
            System.out.println(fileStatus.getBlockSize());
            System.out.println(fileStatus.getPath().getName());

            //获取块信息
            BlockLocation[] blockLocations=fileStatus.getBlockLocations();
            System.out.println(Arrays.toString(blockLocations));
        }

        //3、关闭资源
        fs.close();

    }
}

