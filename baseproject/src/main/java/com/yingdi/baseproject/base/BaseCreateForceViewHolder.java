package com.yingdi.baseproject.base;

import android.view.View;


/**
 * Created by WangYingDi on 2017/2/15.
 */

public abstract class BaseCreateForceViewHolder implements ForceCreateViewHolder {
    protected BaseItemView baseItemView;

    @Override
    public boolean forceCreate(Object data) {
        return data == null;
    }

    public void populate(Object data) {
        if (baseItemView == null) {
            baseItemView = populate();
        }
        baseItemView.populate(data);
    }

    public abstract BaseItemView populate();

    @Override
    public View getView() {
        return baseItemView.getView();
    }
}
