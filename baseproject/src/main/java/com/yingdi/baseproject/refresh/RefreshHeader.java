package com.yingdi.baseproject.refresh;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yingdi.baseproject.R;
import com.yingdi.baseproject.ptr.PtrFrameLayout;
import com.yingdi.baseproject.ptr.PtrUIHandler;
import com.yingdi.baseproject.indicator.PtrIndicator;

import java.util.Date;

/**
 * 下拉刷新头部
 *
 * @author Guo Bo
 */
public class RefreshHeader extends FrameLayout implements PtrUIHandler {

    private final static String LAST_UPDATE_SETTING = "refresh_last_update";

    private final static int PROGRESS_REFRESHING = 60;
    private final static int PROGRESS_COMPLETE = 100;
    private final static int PROGRESS_PREPARE = 0;

    private final static int PROGRESS_INTERVAL = 100;
    private final static int PROGRESS_STEP = 3;

    private TextView mAction;
    private TextView mTime;

    private String mLastUpdateTimeKey;
    private long mLastUpdateTime;

    private LastUpdateTimeUpdater mLastUpdateTimeUpdater = new LastUpdateTimeUpdater();

    private ImageView mHeaderBg;
    private Context context;
    private ImageView mImage;
    private View mLoadingImage;

    public RefreshHeader(Context context) {
        super(context);
        this.context = context;
        initViews();
    }

    public RefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews();
    }

    public RefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initViews();
    }

    private void initViews() {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.refresh_header, this);
        mAction = (TextView) header.findViewById(R.id.action);
        mImage = ((ImageView) header.findViewById(R.id.image));
        mLoadingImage = header.findViewById(R.id.loading_image);
//        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate);
//        animation.setDuration(1000);
//        mLoadingImage.startAnimation(animation);
        mTime = (TextView) header.findViewById(R.id.time);
//        mHeaderBg = ((ImageView) header.findViewById(R.id.headerbg));
//        AnimationDrawable anim = (AnimationDrawable) mHeaderBg.getBackground();
//        if (anim != null)
//            anim.start();
    }

    public void setLastUpdateTimeKey(String key) {
        mLastUpdateTimeKey = key;
        readSavedLastUpdateTime();
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        mAction.setText(R.string.refresh_header_pull_refresh);
        mImage.setImageResource(R.drawable.refresh_down);
        updateLastUpdateTimeText();
        Log.d("tag", "onUIReset");
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        mAction.setText(R.string.refresh_header_pull_refresh);
        mImage.setImageResource(R.drawable.refresh_down);
        mLastUpdateTimeUpdater.start();
        mImage.setVisibility(VISIBLE);
        mLoadingImage.setVisibility(GONE);
        Log.d("tag", "onUIRefreshPrepare");
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        mAction.setText(R.string.refresh_header_refreshing);
        mImage.setVisibility(GONE);
        mLoadingImage.setVisibility(VISIBLE);
        progressTo(PROGRESS_REFRESHING);
        Log.d("tag", "onUIRefreshBegin");
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        mAction.setText(R.string.refresh_header_refresh_complete);
        updateLastUpdateTime();
        updateLastUpdateTimeText();
        mLastUpdateTimeUpdater.stop();
        Log.d("tag", "onUIRefreshComplete");
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();

        if (currentPos < mOffsetToRefresh && lastPos >= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromBottomUnderTouch(frame);
            }
        } else if (currentPos > mOffsetToRefresh && lastPos <= mOffsetToRefresh) {
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {
                crossRotateLineFromTopUnderTouch(frame);
            }
        }
    }


    private void crossRotateLineFromTopUnderTouch(PtrFrameLayout frame) {
        mAction.setText(R.string.cube_ptr_release_to_refresh);
        mImage.setImageResource(R.drawable.refresh_up);
    }

    private void crossRotateLineFromBottomUnderTouch(PtrFrameLayout frame) {
        mAction.setText(R.string.refresh_header_pull_refresh);
        mImage.setImageResource(R.drawable.refresh_down);
    }

    private int mToProgress;

    private void progressTo(int progress) {
        mToProgress = progress;
    }

    private String getLastUpdateTime() {
//        return DateUtils.getNiceDateWithSecond(getContext(), mLastUpdateTime);
        return "";
    }

    private void updateLastUpdateTime() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(LAST_UPDATE_SETTING, 0);
        if (!TextUtils.isEmpty(mLastUpdateTimeKey)) {
            mLastUpdateTime = new Date().getTime();
            sharedPreferences.edit().putLong(mLastUpdateTimeKey, mLastUpdateTime).commit();
        }
    }

    private void updateLastUpdateTimeText() {
        if (mLastUpdateTime == 0) {
            mTime.setVisibility(View.GONE);
        } else {
            mTime.setVisibility(View.VISIBLE);
            mTime.setText(getContext().getString(R.string.refresh_header_last_update, getLastUpdateTime()));
        }
    }

    private void readSavedLastUpdateTime() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(LAST_UPDATE_SETTING, 0);
        if (!TextUtils.isEmpty(mLastUpdateTimeKey)) {
            sharedPreferences.getLong(mLastUpdateTimeKey, 0);
        }
    }

    private class LastUpdateTimeUpdater implements Runnable {

        private boolean mRunning = false;

        private void start() {
            if (TextUtils.isEmpty(mLastUpdateTimeKey)) {
                return;
            }
            mRunning = true;
            run();
        }

        private void stop() {
            mRunning = false;
            removeCallbacks(this);
        }

        @Override
        public void run() {
            updateLastUpdateTimeText();
            if (mRunning) {
                postDelayed(this, 1000);
            }
        }
    }

//	/**
//	 * usher 更换下拉刷新背景
//	 */
//	public void setHeaderPicBg() {
//		mHeaderBg.setBackgroundResource(R.drawable.icon_refresh_header_white);
//	}

}
