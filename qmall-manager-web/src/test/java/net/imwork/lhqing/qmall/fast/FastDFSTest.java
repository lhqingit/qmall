package net.imwork.lhqing.qmall.fast;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

import net.imwork.lhqing.qmall.common.utils.FastDFSClient;

public class FastDFSTest {
	/**
	 * 原始方式上传文件到图片服务器
	 * 
	 * @throws Exception
	 */
	@Test
	public void testUpload() throws Exception {
		// 创建一个配置文件，文件名任意，内容是tracker服务器的地址
		// 使用全局对象加载配置文件
		ClientGlobal.init("E:/Qing_java_software/workspaces/qmall_work/eclipse_qmall_workspace/"
				+ "qmall-manager-web/src/main/resources/conf/client.conf");
		// 创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		// 通过TrackerClient获得一个TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		// 创建一个StorageServer的引用，可以是null
		StorageServer storageServer = null;
		// 创建一个StorageClient,参数需要TrackerServer和StorageServer
		StorageClient storageClient = new StorageClient(trackerServer, storageServer);
		// 使用StorageClient上传文件
		String[] strings = storageClient.upload_file("E:\\test.png", "png", null);
		// Q:遍历打印结果，用于之后访问
		// Q:例如打印结果：group1
		// M00/00/00/wKgZhVpXZCyAaAx0AABM3fN8rZA184.png
		// 那么此时的访问路径就为192.168.25.133/group1/M00/00/00/wKgZhVpXZCyAaAx0AABM3fN8rZA184.png
		for (String string : strings) {
			System.out.println(string);
		}

	}

	/**
	 * 使用提供的工具类上传文件到图片服务器
	 * 
	 * @throws Exception
	 */
	@Test
	public void testFastDFSClient() throws Exception {
		FastDFSClient fastDFSClient = new FastDFSClient(
				"E:/Qing_java_software/workspaces/qmall_work/eclipse_qmall_workspace/"
						+ "qmall-manager-web/src/main/resources/conf/client.conf");
		String fileName = fastDFSClient.uploadFile("E:\\test.png");
		System.out.println("返回的文件路径信息:192.168.25.133/" + fileName);
	}
}
