package com.yingdi.baseproject.refresh;

import android.content.Context;
import android.util.AttributeSet;

import com.yingdi.baseproject.ptr.PtrFrameLayout;

public class RefreshFrameLayout extends PtrFrameLayout {

    private RefreshHeader mRefreshHeader;

    public RefreshFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public RefreshFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public RefreshFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mRefreshHeader = new RefreshHeader(getContext());
        setHeaderView(mRefreshHeader);
        addPtrUIHandler(mRefreshHeader);
    }

    public RefreshHeader getHeader() {
        return mRefreshHeader;
    }

    public void setLastUpdateKey(String key) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setLastUpdateTimeKey(key);
        }
    }
}
