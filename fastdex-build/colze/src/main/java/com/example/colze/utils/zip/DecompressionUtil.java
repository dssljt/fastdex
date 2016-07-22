package com.example.colze.utils.zip;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.example.colze.utils.Logger;
import com.example.colze.utils.StringUtil;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.progress.ProgressMonitor;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

/**
 * @author rzq
 * @function 封装一个解压工具类,以后如果需要压缩相关的方法,都放到此类中
 */
@SuppressLint("DefaultLocale")
public class DecompressionUtil {
	private final int StartDecompression = 4;
	private final int Decompressioning = 5;
	private final int EndDecompression = 6;
	private final int DecompressionErr = 7;
	private File zipFile;
	private String filePath;
	private String pwd;
	private Handler mHandler;
	private boolean isDeleteZip;
	private ZipFile zFile;

	public DecompressionUtil(File zipFile, String filePath, String pwd,
			Handler handler, boolean isDeleteZip) {
		this.zipFile = zipFile;
		this.filePath = filePath;
		this.pwd = pwd;
		this.mHandler = handler;
		this.isDeleteZip = isDeleteZip;
	}

	public void start() {
		if (StringUtil.isEmpty(filePath) || StringUtil.isEmpty(pwd)
				|| null == zipFile) {
			return;
		}
		new Thread(mRunnable).start();
	}

	private Runnable mRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				Logger.d("<<< pwd" + pwd);
				zFile = new ZipFile(zipFile);
				zFile.setFileNameCharset("GBK");
				if (!zFile.isValidZipFile()) {
					throw new ZipException("exception!");
				}
				File destDir = new File(filePath);
				if (destDir.isDirectory() && !destDir.exists()) {
					destDir.mkdir();
				}
				if (zFile.isEncrypted()) {
					zFile.setPassword(MD5(pwd));
				}
				zFile.setRunInThread(true); // true 在子线程中进行解压 , false主线程中解压
				zFile.extractAll(filePath); // 将压缩文件解压到filePath中...

				final ProgressMonitor progressMonitor = zFile.getProgressMonitor();
				int precentDone = 0;
				mHandler.sendEmptyMessage(StartDecompression);
				while (true) {
					// 每隔10ms,发送一个解压进度出去
					Thread.sleep(10);
					precentDone = progressMonitor.getPercentDone();
					Logger.d("<<< unzip file: " + progressMonitor.getFileName());
					Message p_msg = new Message();
					p_msg.what = Decompressioning;
					p_msg.obj = precentDone;
					mHandler.sendMessage(p_msg);
					if (precentDone >= 100) {
						break;
					}
				}
				mHandler.sendEmptyMessage(EndDecompression);
			} catch (Exception e) {
				e.printStackTrace();
				mHandler.sendEmptyMessage(DecompressionErr);
			} finally {
				if (isDeleteZip) {
					//TODO 暂时不删除
					//zipFile.delete();// 将原压缩文件删除
				}
			}
		}
	};

	public class CompressStatus {
		public final static int START = 10000;
		public final static int HANDLING = 10001;
		public final static int COMPLETED = 10002;
		public final static int ERROR = 10003;

		public final static String PERCENT = "PERCENT";
		public final static String ERROR_COM = "ERROR";
	}

	private static String MD5(String sourceStr) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(sourceStr.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e);
		}
		return result.substring(8, 24).toUpperCase();
	}

}