package com.yingdi.baseproject.base;

import android.app.Activity;
import android.content.Context;

import java.util.List;

/**
 * Created by WangYingDi on 2017/5/18.
 */

public class BaseAchieveRefreshListFragment<ListItemType> extends BaseRefreshListFragment {

    private BaseRefreshListActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = ((BaseRefreshListActivity) activity);
    }

    @Override
    protected void addHeaderView() {
        activity.addHeaderView();
    }

    @Override
    public void onLoadMore() {
        activity.onLoadMore();
    }

    @Override
    public void onLoadNew() {
        activity.onLoadNew();
    }

    @Override
    public String getKey(int i) {
        return activity.getKey(i);
    }

    @Override
    public String getSecondKey(int i) {
        return activity.getSecondKey(i);
    }

    @Override
    public void notifyDataChanged(List datas) {
        activity.notifyDataChanged(datas);
    }

    @Override
    public String getHeader(int i) {
        return activity.getHeader(i);
    }

    @Override
    public ViewHolder createViewHolder(Context context, int position) {
        return activity.createViewHolder(context, position);
    }
}
