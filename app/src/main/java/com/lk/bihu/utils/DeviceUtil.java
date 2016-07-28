package com.lk.bihu.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class DeviceUtil {
	public static final int SDK_VERSION_CUPCAKE = 19;
	public static boolean PRE_CUPCAKE = getSDKVersionNumber() >= SDK_VERSION_CUPCAKE ? true
			: false;

	public static void setTranslucent(Activity activity) {
		// 透明状态栏
		if (PRE_CUPCAKE) {
			translucentStatus(activity);

//			RelativeLayout mLayouTop = (RelativeLayout) activity
//					.findViewById(R.id.layout_top);
//
//			if (mLayouTop == null) {
//				return;
//			}
//
//			loginheight(activity.getApplicationContext(), mLayouTop);

		}
	}

	public static void translucentStatus(Activity activity) {
		// 透明状态栏
		activity.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
		// activity.getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		setStatusBarDarkMode(true, activity);
		setStatusBarDarkIcon(activity.getWindow(), true);
	}

	// public static void setTranslucent(Fragment fragment, View view) {
	// // 透明状态栏
	// if (PRE_CUPCAKE) {
	// RelativeLayout mLayouTop = (RelativeLayout) view
	// .findViewById(R.id.layout_top);
	// if (PRE_CUPCAKE) {
	// int height = dip2px(fragment.getActivity(), 65);
	// android.widget.LinearLayout.LayoutParams linearParams =
	// (LinearLayout.LayoutParams) frag_title
	// .getLayoutParams();
	// linearParams.height = height;
	// frag_title.setLayoutParams(linearParams);
	// }
	// }
	// }

	public static int getSDKVersionNumber() {
		int sdkVersion;
		try {
			sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
		} catch (NumberFormatException e) {
			sdkVersion = 0;
		}
		return sdkVersion;
	}

	public int getStatusBarHeight(Activity activity) {
		int result = 0;
		int resourceId = activity.getResources().getIdentifier(
				"status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = activity.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	/**
	 * @author WangJie 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context activity, float dpValue) {
		final float scale = activity.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);

	}

	/**
	 * @author WangJie 增加head的高度到自适配高度沉浸式
	 */
	public static void loginheight(Context activity, View loginll) {
		// int statusa= loginll.getHeight()+ getStatusHeight( activity);
		if (PRE_CUPCAKE) {
			int height = dip2px(activity, 70);
			LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) loginll
					.getLayoutParams();
			linearParams.height = height;
			loginll.setLayoutParams(linearParams);
		}
	}

	public static int getStatusBar(Context activity) {
		if (!PRE_CUPCAKE) {
			return dip2px(activity, 23);
		} else {
			return 0;
		}
	}

	// public static int getStatusHeight(Activity activity) {
	// return dip2px(activity, 70);
	// }

	/**
	 * @author WangJie 部分手机不适用 状态栏高度算法
	 * @param activity
	 * @return
	 */
	public static int getStatusHeight(Activity activity) {
		int statusHeight = 0;
		Rect localRect = new Rect();
		activity.getWindow().getDecorView()
				.getWindowVisibleDisplayFrame(localRect);
		statusHeight = localRect.top;
		if (0 == statusHeight) {
			Class<?> localClass;
			try {
				localClass = Class.forName("com.android.internal.R$dimen");
				Object localObject = localClass.newInstance();
				int i5 = Integer.parseInt(localClass
						.getField("status_bar_height").get(localObject)
						.toString());
				statusHeight = activity.getResources()
						.getDimensionPixelSize(i5);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return statusHeight;
	}

	/**
	 * 小米适配
	 * 
	 * @author WangJie
	 * @param darkmode
	 * @param activity
	 */
	public static void setStatusBarDarkMode(boolean darkmode, Activity activity) {
		Class<? extends Window> clazz = activity.getWindow().getClass();
		try {
			int darkModeFlag = 0;
			Class<?> layoutParams = Class
					.forName("android.view.MiuiWindowManager$LayoutParams");
			Field field = layoutParams
					.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
			darkModeFlag = field.getInt(layoutParams);
			Method extraFlagField = clazz.getMethod("setExtraFlags", int.class,
					int.class);
			extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag
					: 0, darkModeFlag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 魅族适配
	 * 
	 */
	public static boolean setStatusBarDarkIcon(Window window, boolean dark) {
		boolean result = false;
		if (window != null) {
			try {
				WindowManager.LayoutParams lp = window.getAttributes();
				Field darkFlag = WindowManager.LayoutParams.class
						.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
				Field meizuFlags = WindowManager.LayoutParams.class
						.getDeclaredField("meizuFlags");
				darkFlag.setAccessible(true);
				meizuFlags.setAccessible(true);
				int bit = darkFlag.getInt(null);
				int value = meizuFlags.getInt(lp);
				if (dark) {
					value |= bit;
				} else {
					value &= ~bit;
				}
				meizuFlags.setInt(lp, value);
				window.setAttributes(lp);
				result = true;
			} catch (Exception e) {
				Log.e("MeiZu", "setStatusBarDarkIcon: failed");
			}
		}
		return result;
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
