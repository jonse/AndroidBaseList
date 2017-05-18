package com.yingdi.baseproject.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Base class of our fragments
 *
 * @author Guo Bo
 */
public class BaseFragment extends Fragment {

	/**
	 * @see Resources#getDrawable(int)
	 */
	public Drawable getDrawable(int id) {
		return getResources().getDrawable(id);
	}

	/**
	 * @see Resources#getColor(int)
	 */
	public int getColor(int id) {
		return getResources().getColor(id);
	}

	public Drawable getColorDrawable(int id) {
		return getResources().getDrawable(id);
	}

	/**
	 * @see Resources#getStringArray(int)
	 */
	public String[] getStringArray(int id) {
		return getResources().getStringArray(id);
	}

	/**
	 * @see Resources#getDimensionPixelSize(int)
	 */
	public int getDimensionPixelSize(int id) {
		return getResources().getDimensionPixelSize(id);
	}

	/**
	 * @see LayoutInflater#inflate(int, android.view.ViewGroup)
	 */
	public View inflate(int res) {
		// NullPointerException bug fix
		if (getActivity() == null && !isAdded()) {
			return null;
		}
		return LayoutInflater.from(getActivity()).inflate(res, null);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void setStatusBarColor(int color) {
		Activity ac = getActivity();
		if (ac == null) {
			throw new IllegalStateException("Fragment " + this + " not attached to Activity");
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			ac.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			ac.getWindow().setStatusBarColor(color);
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ViewGroup contentWrapper = (ViewGroup) getView();
		contentWrapper.removeAllViews();
	}
}
