package com.example.colze;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import com.example.colze.bean.LoginParam;
import com.example.colze.utils.StringUtil;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

@SuppressLint("HandlerLeak")
public class ChoiceIconActivity extends BaseActivity implements OnClickListener {
	private ImageButton mImageButton;
	private Button mButton_Boy;
	private Button mButton_Girl;
	private Button mButton_Sure;
	private ImageView mImageView_Face;
	private ImageView mImageView_Eye;
	private ImageView mImageView_Hair;
	private ImageView mImageView_Nose;
	private GridView mGridView;
	private ImageAdapter adapter;
	private HashMap<String, List<Icon>> allBPs;
	private static final String ASSETSPATH = "file:///android_asset/";
	private static final String BOYHAIR = "boyHair";
	private static final String GIRLHAIR = "girlHair";
	private static final String EYE = "eye";
	private static final String FACE = "face";
	private static final String HAIRCOLOR = "hairColor";
	private static final String MOUTH = "mouth";
	private static ArrayList<String> hair = new ArrayList<String>();

	private List<Icon> showList = new ArrayList<Icon>();

	// 图标类型（发型，发色，脸型。。。）
	private int icon_type = icon_type_hair;
	private final static int icon_type_hair = 0;
	private final static int icon_type_hair_color = 1;
	private final static int icon_type_face = 2;
	private final static int icon_type_eye = 3;
	private final static int icon_type_nose = 4;

	// 性别
	private String sex_type = BOYHAIR;

	// 发色
	private int hair_color_type = 0;

	private int[] hairColorArr = { R.color.hair_black, R.color.hair_brown,
			R.color.hair_grey, R.color.hair_white, R.color.hair_coffee,
			R.color.hair_purple, R.color.hair_yellow, R.color.hair_redblack };

	static {
		hair.add("black");
		hair.add("brown");
		hair.add("grey");
		hair.add("white");
		hair.add("coffee");
		hair.add("purple");
		hair.add("yellow");
		hair.add("redblack");
	}
	// 选择以后的结果
	// 选择以后的结果
	String faceName = "";
	String hairName = "";
	String eyeName = "";
	String noseName = "";
	private int hair_position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choiceicon);
		init();
	}

	@Override
	public void initData() {
		super.initData();
		mGridView.setAdapter(adapter);
		allBPs = new HashMap<String, List<Icon>>();
		mImageView_Nose.setOnClickListener(this);
		getHair();
		getEye();
		getFace();
		// getHairColor();
		getMouth();
		selectSex();
	}

	/**
	 * 嘴巴
	 */
	private void getMouth() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ArrayList<Icon> bps = new ArrayList<Icon>();
				for (int i = 1; i < Integer.MAX_VALUE; i++) {
					String path = MOUTH + "/b" + i + ".png";
					Bitmap bp = getImageFromAssetsFile(path);
					if (bp != null) {
						Icon icon = new Icon();
						icon.name = "b" + i;
						icon.bitmap = bp;
						bps.add(icon);
					} else {
						break;
					}
				}
				allBPs.put(MOUTH, bps);
			}
		}).start();
	}

	/**
	 * 头发颜色
	 */
	private void getHairColor() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ArrayList<Icon> bps = new ArrayList<Icon>();
				for (int i = 1; i < Integer.MAX_VALUE; i++) {
					String path = HAIRCOLOR + "/d" + i + ".png";
					Bitmap bp = getImageFromAssetsFile(path);
					if (bp != null) {
						Icon icon = new Icon();
						icon.name = "d" + i;
						icon.bitmap = bp;
						bps.add(icon);
					} else {
						break;
					}
				}
				allBPs.put(HAIRCOLOR, bps);
			}
		}).start();
	}

	/**
	 * 脸
	 */
	private void getFace() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ArrayList<Icon> bps = new ArrayList<Icon>();
				for (int i = 1; i < Integer.MAX_VALUE; i++) {
					String path = FACE + "/a" + i + ".png";
					Bitmap bp = getImageFromAssetsFile(path);
					if (bp != null) {
						Icon icon = new Icon();
						icon.name = "a" + i;
						icon.bitmap = bp;
						bps.add(icon);
					} else {
						break;
					}
				}
				allBPs.put(FACE, bps);
			}
		}).start();
	}

	/**
	 * 眼睛
	 */
	private void getEye() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ArrayList<Icon> bps = new ArrayList<Icon>();
				for (int i = 1; i < Integer.MAX_VALUE; i++) {
					String path = EYE + "/c" + i + ".png";
					Bitmap bp = getImageFromAssetsFile(path);
					if (bp != null) {
						Icon icon = new Icon();
						icon.name = "c" + i;
						icon.bitmap = bp;
						bps.add(icon);
					} else {
						break;
					}
				}
				allBPs.put(EYE, bps);
			}
		}).start();
	}

	/**
	 * 男头部
	 */
	private void getHair() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (String str : hair) {
					ArrayList<Icon> bps = new ArrayList<Icon>();
					for (int i = 1; i < Integer.MAX_VALUE; i++) {
						String path = GIRLHAIR + "/" + str + "/f" + i + "_"
								+ hairColor(str) + ".png";
						Bitmap bp = getImageFromAssetsFile(path);
						if (bp != null) {
							Icon icon = new Icon();
							icon.name = "f" + i + "_" + hairColor(str);
							icon.bitmap = bp;
							bps.add(icon);
						} else {
							break;
						}
					}
					allBPs.put(GIRLHAIR + "/" + str, bps);
				}
				for (String str : hair) {
					ArrayList<Icon> bps = new ArrayList<Icon>();
					for (int i = 1; i < Integer.MAX_VALUE; i++) {
						String path = BOYHAIR + "/" + str + "/m" + i + "_"
								+ hairColor(str) + ".png";
						Bitmap bp = getImageFromAssetsFile(path);
						if (bp != null) {
							Icon icon = new Icon();
							icon.name = "m" + i + "_" + hairColor(str);
							icon.bitmap = bp;
							bps.add(icon);
						} else {
							break;
						}
					}
					allBPs.put(BOYHAIR + "/" + str, bps);
				}
				Message message = new Message();
				message.what = 0;
				handler.sendMessage(message);
			}
		}).start();
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// for (String str : hair) {
		// ArrayList<Icon> bps = new ArrayList<Icon>();
		// for (int i = 1; i < Integer.MAX_VALUE; i++) {
		// String path = BOYHAIR + "/" + str + "/m" + i + "_"
		// + hairColor(str) + ".png";
		// Bitmap bp = getImageFromAssetsFile(path);
		// if (bp != null) {
		// Icon icon=new Icon();
		// icon.name = "m"+i+ "_"+ hairColor(str);
		// icon.bitmap = bp;
		// bps.add(icon);
		// } else {
		// break;
		// }
		// }
		// allBPs.put(BOYHAIR + "/" + str, bps);
		// }
		// }
		// }).start();
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				showHair();
				break;
			case 1:
				if (null != allBPs.get(sex_type + "/"
						+ hair.get(hair_color_type))) {
					mImageView_Hair.setImageBitmap(allBPs.get(
							sex_type + "/" + hair.get(hair_color_type)).get(
							hair_position).bitmap);
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public void initWidget() {
		super.initWidget();
		mImageButton = (ImageButton) findViewById(R.id.ib_back);
		mButton_Boy = (Button) findViewById(R.id.button_boy);
		mButton_Girl = (Button) findViewById(R.id.button_girl);
		mButton_Sure = (Button) findViewById(R.id.button_sure);
		mImageView_Face = (ImageView) findViewById(R.id.image_face);
		mImageView_Eye = (ImageView) findViewById(R.id.image_eye);
		mImageView_Hair = (ImageView) findViewById(R.id.image_hair);
		mImageView_Nose = (ImageView) findViewById(R.id.image_nose);
		mGridView = (GridView) findViewById(R.id.icon_gridview);
		adapter = new ImageAdapter();
	}

	private int hairColor(String folder) {
		return folder.equals(hair.get(0)) ? 1 : folder.equals(hair.get(1)) ? 4
				: folder.equals(hair.get(2)) ? 2
						: folder.equals(hair.get(3)) ? 3 : folder.equals(hair
								.get(4)) ? 5 : folder.equals(hair.get(5)) ? 6
								: folder.equals(hair.get(6)) ? 7 : folder
										.equals(hair.get(7)) ? 8 : 8;
	}

	private void showHair() {
		showList.clear();
		if (null != allBPs.get(sex_type + "/" + hair.get(hair_color_type))) {
			showList.addAll(allBPs.get(sex_type + "/"
					+ hair.get(hair_color_type)));
			adapter.notifyDataSetChanged();
		}
	}

	private void selectSex() {
		if (sex_type.equals(BOYHAIR)) {
			mButton_Boy.setBackgroundResource(R.drawable.icon_button_sure_rect);
			mButton_Boy.setTextColor(getResources().getColor(R.color.white));
			mButton_Girl.setBackgroundResource(R.drawable.button_selector);
			mButton_Girl.setTextColor(getResources().getColor(
					android.R.color.black));
		} else if (sex_type.equals(GIRLHAIR)) {
			mButton_Girl
					.setBackgroundResource(R.drawable.icon_button_sure_rect);
			mButton_Girl.setTextColor(getResources().getColor(R.color.white));
			mButton_Boy.setBackgroundResource(R.drawable.button_selector);
			mButton_Boy.setTextColor(getResources().getColor(
					android.R.color.black));
		}
	}

	@Override
	public void onViewClick(View v) {
		super.onViewClick(v);
//		switch (v.getId()) {
//		case R.id.ib_back:
//			finish();
//			break;
//		case R.id.button_boy:
//			sex_type = BOYHAIR;
//			selectSex();
//			if (icon_type == icon_type_hair) {
//				showHair();
//			}
//			break;
//		case R.id.button_girl:
//			sex_type = GIRLHAIR;
//			selectSex();
//			if (icon_type == icon_type_hair) {
//				showHair();
//			}
//			break;
//		case R.id.button_sure:
//			if (StringUtil.isEmpty(faceName)) {
//				showToast("脸型没有选择");
//				return;
//			}
//			if (StringUtil.isEmpty(hairName)) {
//				showToast("发型没有选择");
//				return;
//			}
//			if (StringUtil.isEmpty(eyeName)) {
//				showToast("眼眉没有选择");
//				return;
//			}
//			if (StringUtil.isEmpty(noseName)) {
//				showToast("口鼻没有选择");
//				return;
//			}
//			String facePic = hairName + "#" + faceName + "#" + eyeName + "#"
//					+ noseName;
//			int sex = sex_type == BOYHAIR ? 1 : 0;
//			LoginParam param = (LoginParam)getIntent().getSerializableExtra(
//					RegisterActivity.LOGINPARAM);
//			param.facePic = facePic;
//			param.sex = sex;
//			setResult(2002, RegisterSecondActivity.createIntent(this, param));
//			finish();
//			break;
//		case R.id.button_hair:
//			icon_type = icon_type_hair;
//			showHair();
//
//			break;
//		case R.id.button_hair_color:
//			icon_type = icon_type_hair_color;
//			showList.clear();
//			adapter.notifyDataSetChanged();
//			break;
//		case R.id.button_face:
//			icon_type = icon_type_face;
//
//			showList.clear();
//			if (null != allBPs.get(FACE)) {
//				showList.addAll(allBPs.get(FACE));
//				adapter.notifyDataSetChanged();
//			}
//			break;
//		case R.id.button_eye:
//			icon_type = icon_type_eye;
//
//			showList.clear();
//			if (null != allBPs.get(EYE)) {
//				showList.addAll(allBPs.get(EYE));
//				adapter.notifyDataSetChanged();
//			}
//			break;
//		case R.id.button_nose:
//			icon_type = icon_type_nose;
//
//			showList.clear();
//			if (null != allBPs.get(MOUTH)) {
//				showList.addAll(allBPs.get(MOUTH));
//				adapter.notifyDataSetChanged();
//			}
//			break;
//		default:
//			break;
//		}
	}

	private class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			if (icon_type == icon_type_hair_color)
				return hairColorArr.length;
			return showList.size();
		}

		@Override
		public Object getItem(int arg0) {
			if (icon_type == icon_type_hair_color)
				return hairColorArr[arg0];
			return showList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int position, View view, ViewGroup arg2) {
			ViewHolder holder;
			if (null == view) {
				holder = new ViewHolder();
				view = layoutInflater.inflate(R.layout.adapter_icon, null);
				holder.mImageView = (ImageView) view
						.findViewById(R.id.item_icon);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if (icon_type == icon_type_hair_color) {
				holder.mImageView.setImageResource(hairColorArr[position]);
			} else {
				holder.mImageView.setImageBitmap(showList.get(position).bitmap);
			}
			holder.mImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					switch (icon_type) {
					case icon_type_hair:
						hair_position = position;
						mImageView_Hair.setImageBitmap(showList.get(position).bitmap);
						hairName = showList.get(position).name;
						break;
					case icon_type_hair_color:
						hair_color_type = position;
						Message message = new Message();
						message.what = 1;
						handler.sendMessage(message);
						break;
					case icon_type_face:
						mImageView_Face.setImageBitmap(showList.get(position).bitmap);
						faceName = showList.get(position).name;
						break;
					case icon_type_eye:
						mImageView_Eye.setImageBitmap(showList.get(position).bitmap);
						eyeName = showList.get(position).name;
						break;
					case icon_type_nose:
						mImageView_Nose.setImageBitmap(showList.get(position).bitmap);
						noseName = showList.get(position).name;
						break;
					default:
						break;
					}

				}
			});
			return view;
		}

		class ViewHolder {
			ImageView mImageView;
		}
	}

	private Bitmap getImageFromAssetsFile(String fileName) {
		Bitmap image = null;
		AssetManager am = getResources().getAssets();
		InputStream is = null;
		try {
			is = am.open(fileName);
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inSampleSize = 2;
			image = BitmapFactory.decodeStream(is, null, opt);
		} catch (IOException e) {
			return null;
		} catch (OutOfMemoryError e) {
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return image;
	}

	@Override
	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.image_nose:
//			mImageView_Face.setImageBitmap(allBPs.get(FACE).get(1).bitmap);
//			mImageView_Eye.setImageBitmap(allBPs.get(EYE).get(1).bitmap);
//			mImageView_Hair.setImageBitmap(allBPs.get(
//					BOYHAIR + "/" + hair.get(1)).get(1).bitmap);
//			mImageView_Nose.setImageBitmap(allBPs.get(MOUTH).get(1).bitmap);
//			break;
//		default:
//			break;
//		}
	}

	class Icon {
		String name;
		Bitmap bitmap;
	}

	public static Intent createIntent(Context context, LoginParam param) {
		Intent intent = new Intent(context, ChoiceIconActivity.class);
		intent.putExtra(RegisterActivity.LOGINPARAM, param);
		return intent;
	}
}
