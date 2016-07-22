package com.example.colze.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.example.colze.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AllContacts {

	public static String sdcard = "";
	public static String sdcard2 = "";

	static {
		sdcard2 = Environment.getExternalStorageDirectory().toString()+ "/" + Config.sdcardPath;

		sdcard = sdcard2 + "/ExamResource";
	}

	public static String testPath = sdcard + "/examInOne/S14_2_0001.json";

	/**
	 * 难度
	 */
	public static final int EASY = 1;
	public static final int MIDDLE = 2;
	public static final int HARD = 3;
	public static final int FREE = 0;

	public static String getDf(int df) {
		switch (df) {
		case EASY:
			return "初级题";
		case MIDDLE:
			return "中级题";
		case HARD:
			return "高级题";
		case FREE:
			return "免费题";

		default:
			return "初级题";
		}
	}

	/**
	 * 根据难度获取对应的路劲
	 * 
	 * @param type
	 * @return
	 */
	public static String getPathByType(int type) {
		switch (type) {
		case EASY:
			return sdcard + "/subject/";
		case MIDDLE:
			return sdcard + "/subject/";
		case HARD:
			return sdcard + "/subject/hard/D3-";
		case FREE:
			return sdcard + "/subject/easy/free";
		default:
			break;
		}
		return "";
	}

	/**
	 * 获得评价
	 */
	public static String getComment(float score) {
		if (score < 60) {
			return "需努力";
		} else if (score < 80) {
			return "合格";
		} else if (score < 90) {
			return "良好";
		} else if (score <= 100) {
			return "优秀";
		} else {
			return "需努力";
		}
	}

	public static final String EXAMLOCALBEAN = "examLocalBean";

	private static final String day_1 = "Genius is one percent inspiration, ninety-nine percent perspiration. ——Thomas Edison\n\n天才是1%的天分加99%的努力。——爱迪生";
	private static final String day_2 = "Life is not fair, get used to it.——Bill Gates\n\n生活是不公平的；要去适应它。——比尔盖茨";
	private static final String day_3 = "All for one, one for all. —— Dumas pere\n\n人人为我，我为人人 ——大仲马";
	private static final String day_4 = "Knowledge is power. ——Francis Bacon\n\n知识就是力量。——培根";
	private static final String day_5 = "I am a slow walker , but I never walk backwards.——Abraham Lincoln\n\n我走得很慢，但是我从来不会后退。—— 林肯. A";
	private static final String day_6 = "He who seize the right moment, is the right man. ——Johann Wolfgang von Goethe\n\n谁把握机遇，谁就心想事成。——歌德";
	private static final String day_7 = "Stay Hungry, Stay Foolish—— Steve Jobs\n\n求知若渴,大智若愚 —— 乔布斯";
	private static final String day_8 = "Attitude is a little thing that makes a big difference.——Winston Leonard Spencer Churchill\n\n态度决定一切。——丘吉尔";
	private static final String day_9 = "It is a pleasure if you have learnt something new and put it into practice at regular intervals,isn't it?\n\n子曰：学而时习之，不亦说乎 ——孔子";
	private static final String day_10 = "Where there is a will , there is a way. ——Thomas Edison\n\n有志者，事竟成。——爱迪生";
	private static final String day_11 = "Courage is resistance to fear, mastery of fear; not absence of fear.—— Mark Twain\n\n勇气是征服恐惧，并不是没有恐惧。——马克.吐温";
	private static final String day_12 = "The two most powerful warriors are patience and time.——Lev Tolstoy\n\n时间与耐心是最强大的两个战士。——列夫 托尔斯泰";
	private static final String day_13 = "Art is a lie that tells the truth. ——Pablo Picasso\n\n艺术是揭示真理的谎言  ——毕加索";
	private static final String day_14 = "I have a dream ... ——Martin Luther King\n\n我有一个梦想  ——马丁·路德·金";
	private static final String day_15 = "A man can be destroyed but not defeated. ——Ernest Hemingway\n\n人可以被毁灭，但不可以被打败。 ——海明威";
	private static final String day_16 = "If I have seen farther than others, it is because I was standing on the shoulder of giants.——Isaac Newton\n\n站在巨人的肩膀上，看得比其他人更远 ——牛顿";
	private static final String day_17 = "Never leave that until tomorrow , which you can do today.——Benjamin Franklin\n\n今天的事不要拖到明天。——本杰明.富兰克林";
	private static final String day_18 = "The first wealth is health.——Ralph Waldo Emerson\n\n健康是人生第一财富。——爱默生. R. W";
	private static final String day_19 = "A well-spent day brings happy sleep, so life well-used brings happy death. ——Leonardo da Vinci\n\n一日充实，可以安睡。一生充实，可以无憾。 ——达芬奇";
	private static final String day_20 = "Patience is bitter, but its fruit is sweet. ——Jean Jacques Rousseau\n\n 忍耐是痛苦的，但它的果实是甜蜜的。——卢梭";
	private static final String day_21 = "This above all: to thine self be true. ——W.Shakespeare\n\n最重要的是，你必须对自己忠实。——威廉 莎士比亚";
	private static final String day_22 = "Act enthusiastic and you will be enthusiastic. ——Dale Carnegie\n\n凡是积极主动，你会变得充满热情。 ——卡耐基";
	private static final String day_23 = "You have to believe in yourself. That’s the secret of success. —— Charles Chaplin\n\n必须相信自己，这是成功的秘诀。—— 卓别林";
	private static final String day_24 = "Other men live to eat, while I eat to live.—— Socrates\n\n别人为食而生存，我为生存而食。—— 苏格拉底";
	private static final String day_25 = "If winter comes，can spring be far behind？—— P.B.Shelley\n\n冬天来了，春天还会远吗？—— 雪莱 英国诗人";
	private static final String day_26 = "Intellectuals solve problems; geniuses prevent them. —— Albert Einstein\n\n智者解决问题，天才预防问题 —— 爱因斯坦";
	private static final String day_27 = "The first and greatest victory is to conquer yourself. —— Platon\n\n第一个最伟大的胜利就是战胜自己 —— 柏拉图";
	private static final String day_28 = "Temperance is a mean with regard to pleasures. —— Aristotle\n\n节制是一条通向快乐的道路 —— 亚里士多德";
	private static final String day_29 = "We come nearest to the great when we are great in humility. —— Rabindranath Tagore\n\n当我们极谦卑时，则几近於伟大。—— 泰戈尔";
	private static final String day_30 = "Victory belongs to the most persevering. —— Napoleon Bonaparte\n\n坚持到底必将成功。—— 拿破仑";
	private static final String day_31 = "O weary night, O long and tedious night. Abate thy hours, shine comforts from the east. —— W.Shakespeare\n\n黑暗无论怎样悠长，白昼总会到来。——威廉 莎士比亚";

	public static String[] everyDayTextArr = { day_1, day_2, day_3, day_4,
			day_5, day_6, day_7, day_8, day_9, day_10, day_11, day_12, day_13,
			day_14, day_15, day_16, day_17, day_18, day_19, day_20, day_21, day_22,
			day_23, day_24, day_25, day_26, day_27, day_28, day_29, day_30, day_31 };


	/**
	 * 复制一个目录及其子目录、文件到另外一个目录
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	public void copyFolder(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			if (!dest.exists()) {
				dest.mkdir();
			}
			String files[] = src.list();
			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);
				// 递归复制
				copyFolder(srcFile, destFile);
			}
		} else {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;

			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
		}
	}

	public static void copyFileOrDir(Context context,String path) {
		AssetManager assetManager = context.getAssets();
		String assets[] = null;
		try {
			assets = assetManager.list(path);
			if (assets.length == 0) {
				copyFile(context,path);
			} else {
				String fullPath = sdcard + path;
				File dir = new File(fullPath);
				if (!dir.exists())
					dir.mkdir();
				for (int i = 0; i < assets.length; ++i) {
					copyFileOrDir(context,path + "/" + assets[i]);
				}
			}
		} catch (IOException ex) {
			Log.e("tag", "I/O Exception", ex);
		}
	}

	private static void copyFile(Context context,String filename) {
		AssetManager assetManager = context.getAssets();

		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(filename);
			String newFileName = sdcard + filename;
			out = new FileOutputStream(newFileName);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			Log.e("tag", e.getMessage());
		}

	}
}
