package com.yingdi.baseproject.base;

import android.view.View;

public interface ViewHolder<T> {

	View getView();

	void populate(T data);

}
