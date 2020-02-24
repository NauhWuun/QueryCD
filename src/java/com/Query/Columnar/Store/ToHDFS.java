package com.Query.Columnar.Store;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class ToHDFS
{
    private final String HDFS_PATH;
    private final String HDFS_USER;
    private Configuration configuration;

    private static FileSystem fileSystem;

    /**
     *
     * @param HDFS_PATH : same as format "hdfs://127.0.0.1:8020"
     * @param HDFS_USER : input user name same as "root"
     */
    public ToHDFS(String HDFS_PATH, String HDFS_USER) {
        this.HDFS_PATH = HDFS_PATH;
        this.HDFS_USER = HDFS_USER;

        try {
            configuration = new Configuration();
            configuration.set("dfs.replication", "1");

            fileSystem = FileSystem.get(new URI(HDFS_PATH), configuration, HDFS_USER);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static FileSystem getFileSystem() {
        return fileSystem;
    }

    public static boolean mkdir(String path) throws Exception {
        return fileSystem.mkdirs(new Path(path));
    }

    /**
     * Lookup file data
     */
    public static String text(String path, String encode) throws Exception {
        FSDataInputStream inputStream = fileSystem.open(new Path(path));
        return inputStreamToString(inputStream, encode);
    }

    public void createAndWrite(String path, String context) throws Exception {
        FSDataOutputStream out = fileSystem.create(new Path(path));
        out.write(context.getBytes());
        out.flush();
        out.close();
    }

    public boolean rename(String oldPath, String newPath) throws Exception {
        return fileSystem.rename(new Path(oldPath), new Path(newPath));
    }

    public void copyFromLocalFile(String localPath, String hdfsPath) throws Exception {
        fileSystem.copyFromLocalFile(new Path(localPath), new Path(hdfsPath));
    }

    public void copyToLocalFile(String hdfsPath, String localPath) throws Exception {
        fileSystem.copyToLocalFile(new Path(hdfsPath), new Path(localPath));
    }

    public FileStatus[] listFiles(String path) throws Exception {
        return fileSystem.listStatus(new Path(path));
    }

    public RemoteIterator<LocatedFileStatus> listFilesRecursive(String path, boolean recursive) throws Exception {
        return fileSystem.listFiles(new Path(path), recursive);
    }

    public BlockLocation[] getFileBlockLocations(String path) throws Exception {
        FileStatus fileStatus = fileSystem.getFileStatus(new Path(path));
        return fileSystem.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
    }

    public boolean delete(String path) throws Exception {
        return fileSystem.delete(new Path(path), true);
    }

    private static String inputStreamToString(InputStream inputStream, String encode) {
        try {
            if (encode == null || ("".equals(encode))) {
                encode = "utf-8";
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, encode));
            StringBuilder builder = new StringBuilder();
            String str = "";
            while ((str = reader.readLine()) != null) {
                builder.append(str).append("\n");
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final String getPath() {
        return this.HDFS_PATH;
    }

    public final String getUserName() {
        return this.HDFS_USER;
    }

    public Configuration getConfigure() {
        return this.configuration;
    }
}
