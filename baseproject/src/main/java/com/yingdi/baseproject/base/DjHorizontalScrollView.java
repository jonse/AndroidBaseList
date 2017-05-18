package com.yingdi.baseproject.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

public class DjHorizontalScrollView extends HorizontalScrollView {

	private OnScrollListener mListener;

	public DjHorizontalScrollView(Context context) {
		super(context);
	}

	public DjHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DjHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mListener != null) {
			mListener.onScroll(l, t);
		}
	}

	public void setOnScrollListener(OnScrollListener listener) {
		mListener = listener;
	}

}
