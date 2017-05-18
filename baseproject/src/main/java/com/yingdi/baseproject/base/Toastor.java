package com.yingdi.baseproject.base;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author wangyingdi
 * @date 2015-08-03
 */
public class Toastor {

	private Toast mToast;
	private static Context mContext;

	public static Toastor instance = new Toastor();

	public static void init(Context context) {
		mContext = context;
	}

	private Toastor() {
	}

	public static Toastor getInstance() {
		return instance;
	}

	private Toast getShortToast(int resId) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, resId, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(resId);
		}
		return mToast;
	}

	private Toast getShortToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
		}
		return mToast;
	}

	private Toast getLongToast(int resId) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, resId, Toast.LENGTH_LONG);
		} else {
			mToast.setText(resId);
		}
		return mToast;
	}

	private Toast getLongToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
		} else {
			mToast.setText(text);
		}
		return mToast;
	}

	private Toast getSingleToast(String text) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
		} else {
			mToast.setText(text);
		}
		return mToast;
	}

	public void showShortToast(int resId) {
		getShortToast(resId).show();
	}

	public void showShortToast(String text) {
		getShortToast(text).show();
	}

	public void showCenterShortToast(int resId) {
		Toast toast = getShortToast(resId);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void showCenterShortToast(String text) {
		Toast toast = getShortToast(text);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void showCenterLongToast(String text) {
		Toast toast = getLongToast(text);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void showLongToast(int resId) {
		getLongToast(resId).show();
	}

	public void showLongToast(String text) {
		getSingleToast(text).show();
	}

}