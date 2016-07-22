package com.example.colze.utils.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.example.colze.Config;
import com.example.colze.utils.AllContacts;
import com.example.colze.utils.HttpUtils;
import com.example.colze.utils.Logger;
import com.example.colze.utils.ObjectCacheToFile;
import com.example.colze.utils.StringUtil;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ZipUtil {
	private final int StartDownLoad = 0;
	private final int DownLoading = 1;
	private final int EndDownLoad = 2;
	private final int DownLoadErr = 3;
	private final int StartDecompression = 4;
	private final int Decompressioning = 5;
	private final int EndDecompression = 6;
	private final int DecompressionErr = 7;
	private final int KEYSUCCESS = 8;
	private final int KEYREE = 9;
	private final String keyUrl = "http://app.gaofy.com/upload/config/"+ Config.downloadZip +"/config.json";
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case StartDownLoad: {
				if (null != listener) {
					listener.onZipStartDownLoad();
				}
			}
				break;
			case DownLoading: {
				if (null != listener) {
					Integer position = (Integer) msg.obj;
					listener.onZipDownLoading(position);
				}
			}
				break;
			case EndDownLoad: {
				if (null != listener) {
					listener.onZipEndDownLoad();
				}
				startDecompression();
			}
				break;
			case DownLoadErr: {
				if (null != listener) {
					listener.onZipDownLoadErr();
				}
			}
				break;
			case StartDecompression: {
				if (null != listener) {
					listener.onZipStartDecompression();
				}
			}
				break;
			case Decompressioning: {
				if (null != listener) {
					Integer position = (Integer) msg.obj;
					listener.onZipDecompressioning(position);
				}
			}
				break;
			case EndDecompression: {
				if (null != listener) {
					listener.onZipEndDecompression();
				}
			}
				break;
			case DecompressionErr: {
				if (null != listener) {
					listener.onZipDecompressionErr();
				}
			}
				break;
			case KEYSUCCESS: {
				if (null != ObjectCacheToFile.getCache("zipKey", String.class,
						mContext)) {
					String oldKey = ObjectCacheToFile.getCache("zipKey",
							String.class, mContext).toString();
					if (StringUtil.isEmpty(oldKey)) {
						ObjectCacheToFile.doCache("zipKey", key, mContext);
						new Thread(mRunnable_DownLoad).start();
					} else {
						if (!oldKey.equals(key)) {
							ObjectCacheToFile.doCache("zipKey", key, mContext);
							new Thread(mRunnable_DownLoad).start();
						}else
						{
							if (null != listener) {
								listener.noUpdate();
							}
						}
					}
				} else {
					ObjectCacheToFile.doCache("zipKey", key, mContext);
					new Thread(mRunnable_DownLoad).start();
				}
			}
				break;
			case KEYREE: {
				String message = msg.obj.toString();
				Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
			}
				break;
			default:
				break;
			}
		}
	};
	private Context mContext;
	private int download_precent = 0;
	private ZipListener listener = null;
	private String fileUrl = "";
	private String key = "";
	private boolean isDeleteZip = false;

	public ZipUtil(Context context, ZipListener l, String url, boolean delete) {
		this.mContext = context;
		this.listener = l;
		this.fileUrl = url;
		this.isDeleteZip = delete;
	}

	public void start()
	{
		getKey();
	}
	public void startDownLoad() {
		new Thread(mRunnable_DownLoad).start();
	}

	private void getKey() {
		new Thread(mRunnable_Key).start();
	}

	private void startDecompression() {
		new Thread(mRunnable_Zip).start();
	}

	private Runnable mRunnable_Key = new Runnable() {

		@Override
		public void run() {
			Message message = new Message();
			Logger.d("<<< keyUrl: " + keyUrl);
			String result = HttpUtils.getPostResult(keyUrl);
			if (StringUtil.isEmpty(result)) {
				message.what = KEYREE;
				message.obj = "获取时间失败";
				mHandler.sendMessage(message);
			} else {
				if (result.trim().equals("{\"code\":405,\"msg\":\"网络超时！\"}")
						|| result.trim().equals(
								"{\"code\":405,\"msg\":\"网络超时！\"}")) {
					message.what = KEYREE;
					message.obj = "网络超时";
					mHandler.sendMessage(message);
				} else {
					try {
						JSONObject keyJson = new JSONObject(result);
						String keyStr = String.valueOf(keyJson.opt("all"));
						if (StringUtil.isEmpty(keyStr)) {
							message.what = KEYREE;
							message.obj = "解析时间错误";
							mHandler.sendMessage(message);
						} else {
							key = timeTransformation(keyStr);
							mHandler.sendEmptyMessage(KEYSUCCESS);
						}
					} catch (Exception e) {
						message.what = KEYREE;
						message.obj = "解析时间错误";
						mHandler.sendMessage(message);
					}
				}
			}
		}
	};

	private Runnable mRunnable_DownLoad = new Runnable() {
		@Override
		public void run() {
			if (isSdCardExist()) {
				try {
					Logger.d("<<< fileUrl: " + fileUrl);
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(fileUrl);
					HttpResponse response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					if (null != is) {
						mHandler.sendEmptyMessage(StartDownLoad);
						String folderPath = AllContacts.sdcard2;
						File folderFile = new File(folderPath);
						if (folderFile.exists()) {
							folderFile.delete();
						}
						folderFile.mkdir();
						String filePath = AllContacts.sdcard2 + "/ExamResource.zip";
						File zipFile = new File(filePath);
						zipFile.createNewFile();
						BufferedInputStream bis = new BufferedInputStream(is);
						FileOutputStream fos = new FileOutputStream(zipFile);
						BufferedOutputStream bos = new BufferedOutputStream(fos);
						int read;
						long count = 0;
						int precent = 0;
						byte[] buffer = new byte[1024];
						while ((read = bis.read(buffer)) != -1) {
							bos.write(buffer, 0, read);
							count += read;
							precent = (int) (((double) count / length) * 100);
							if (precent - download_precent >= 1) {
								download_precent = precent;
								Message p_msg = new Message();
								p_msg.what = DownLoading;
								p_msg.obj = download_precent;
								mHandler.sendMessage(p_msg);
							}
						}
						bos.flush();
						bos.close();
						fos.flush();
						fos.close();
						is.close();
						bis.close();
						mHandler.sendEmptyMessage(EndDownLoad);
					} else {
						Toast.makeText(mContext, "下载异常", Toast.LENGTH_SHORT)
								.show();
						mHandler.sendEmptyMessage(DownLoadErr);
					}
				} catch (Exception e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(DownLoadErr);
				}
			} else {
				Toast.makeText(mContext, "无SD卡", Toast.LENGTH_SHORT).show();
			}
		}
	};
	private Runnable mRunnable_Zip = new Runnable() {
		@Override
		public void run() {
			if (isSdCardExist()) {
				String filePath = AllContacts.sdcard2 + "/ExamResource.zip";
				String outFolder = AllContacts.sdcard2;
				File file = new File(filePath);
				if (file.exists()) {
					File outFile = new File(outFolder);
					if (!outFile.exists()) {
						outFile.mkdir();
					}
					DecompressionUtil zu = new DecompressionUtil(file,
							outFolder, key, mHandler, isDeleteZip);
					zu.start();
				} else {
					Toast.makeText(mContext, "文件不存在", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(mContext, "无SD卡", Toast.LENGTH_SHORT).show();
			}
		}
	};

	public static boolean isSdCardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	private String timeTransformation(String timeStr) {
		String result = "";
		String[] timeArr = timeStr.split("-");
		for (int i = 0; i < timeArr.length; i++) {
			result += timeArr[i];
		}
		return result;
	}

	public interface ZipListener {
		public void onZipStartDownLoad();

		public void onZipDownLoading(int position);

		public void onZipEndDownLoad();

		public void onZipDownLoadErr();

		public void onZipStartDecompression();

		public void onZipDecompressioning(int position);

		public void onZipEndDecompression();

		public void onZipDecompressionErr();
		
		public void noUpdate();
		
	}
}
