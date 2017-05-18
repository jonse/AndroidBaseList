package com.yingdi.baseproject.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by WangYingDi on 2017/2/15.
 */

public abstract class BaseItemView {
    protected LayoutInflater inflate;
    protected Context context;
    protected View view;

    public BaseItemView(Context context) {
        this.context = context;
        inflate = LayoutInflater.from(context);
        view=inflateView();

    }

    public abstract View inflateView();

    public abstract void populate(Object object);

    public View getView() {
        return view;
    }
}
