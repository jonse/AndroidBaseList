package com.yingdi.baseproject.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;
import android.widget.TextView;

import com.yingdi.baseproject.R;


/**
 * @author Usher D
 */
public abstract class TabPagerFragmentWithIndicator extends BaseFragment implements OnScrollListener {

    private View mContentView;
    protected TabHost mTabHost;
    private DjHorizontalScrollView mTabScrollView;
    private View mScrollMaskLeft;
    private View mScrollMaskRight;
    private NoCacheViewPager mViewPager;
    protected TabPagerAdapter mTabPagerAdapter;
    private TabPagerListener mTabPagerListener;

    private View mTabSelectIndicator;

    private CategoryGroup mTabs;

    /**
     * 获取所有的Tab
     */
    protected abstract CategoryGroup getTabs();

    /**
     * 获取制定位置Page显示的内容
     */
    protected abstract Fragment getPage(int position);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mContentView != null) {
            mContentView.post(new Runnable() {
                @Override
                public void run() {
                    if (mTabPagerListener != null) {
                        mTabPagerListener.onTabChanged("");
                        onScroll(0, 0);
                    }
                }
            });
            return mContentView;
        }

        mContentView = inflater.inflate(R.layout.tabs_view_pager_with_indicator, container, false);
        mTabSelectIndicator = mContentView.findViewById(R.id.tab_select_indicator);

        mTabs = getTabs();
        mTabHost = (TabHost) mContentView.findViewById(android.R.id.tabhost);
        mTabScrollView = (DjHorizontalScrollView) mContentView.findViewById(R.id.tabs_scroll_view);
        //TODO deal it well，do it for temp
        mTabScrollView.setFillViewport(true);
        mViewPager = (NoCacheViewPager) mContentView.findViewById(R.id.pager);
        mScrollMaskLeft = mContentView.findViewById(R.id.mask_left);
        mScrollMaskRight = mContentView.findViewById(R.id.mask_right);

        mTabPagerAdapter = new TabPagerAdapter(this, mTabHost, mTabs);
        mTabPagerListener = new TabPagerListener(mTabHost, mTabSelectIndicator, mViewPager);

        mTabHost.setOnTabChangedListener(mTabPagerListener);
        mViewPager.setAdapter(mTabPagerAdapter);
        mViewPager.setOnPageChangeListener(mTabPagerListener);
        mViewPager.setOffscreenPageLimit(0);
        mTabScrollView.setOnScrollListener(this);

        mContentView.post(new Runnable() {

            @Override
            public void run() {
                mTabPagerListener.onTabChanged("");
                onScroll(0, 0);
            }
        });
        return mContentView;
    }

    public void noti(CategoryGroup tabs) {
        mTabs = getTabs();
        mTabPagerAdapter.noti(tabs);
    }

    public TabHost getTabHost() {
        return mTabHost;
    }

    public AnBoCategory getCurrentTab() {
        return mTabs.getCats().get(mTabHost.getCurrentTab());
    }

    public int getCurrentTabPosition() {
        return mTabHost.getCurrentTab();
    }

    public AnBoCategory getTab(int position) {
        return mTabs.getCats().get(position);
    }

    /**
     * 设置Tab背景色
     */
    public void setTabBackgroundColor(int color) {
        mTabScrollView.setBackgroundColor(color);
    }

    private boolean mIsScrollMaskLeftShown = false;
    private boolean mIsScrollMaskRightShown = true;

    @Override
    public void onScroll(int x, int y) {
        int maxScroll = mTabScrollView.getChildAt(0).getWidth() - mTabScrollView.getWidth();
        if (!isAdded()) {
            return;
        }
        if (x == 0) {
            mIsScrollMaskLeftShown = false;
//			mScrollMaskLeft.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.top_tab_mask_hide));
            mScrollMaskLeft.setVisibility(View.GONE);
        }
        if (x >= maxScroll) {
            mIsScrollMaskRightShown = false;
//			mScrollMaskRight.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.top_tab_mask_hide));
            mScrollMaskRight.setVisibility(View.GONE);
        }
        if (x > 0 && x < maxScroll) {
            if (!mIsScrollMaskLeftShown) {
                mIsScrollMaskLeftShown = true;
                mScrollMaskLeft.setVisibility(View.VISIBLE);
//				mScrollMaskLeft.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.top_tab_mask_show));
            }
            if (!mIsScrollMaskRightShown) {
                mIsScrollMaskRightShown = true;
                mScrollMaskRight.setVisibility(View.VISIBLE);
//				mScrollMaskRight.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.top_tab_mask_show));
            }
        }
    }

    public static class TabPagerAdapter extends FragmentPagerAdapter implements TabContentFactory {

        private TabPagerFragmentWithIndicator mHostFragment;
        private CategoryGroup mTabs;
        private TabHost mTabHost;

        private Fragment mCurrentFragment;

        public TabPagerAdapter(TabPagerFragmentWithIndicator hostFragment, TabHost tabHost, CategoryGroup tabs) {
            super(hostFragment.getChildFragmentManager());
            mHostFragment = hostFragment;
            mTabHost = tabHost;
            mTabs = tabs;
            mTabHost.setup();
            if (mTabs != null) {
                for (int i = 0, count = mTabs.getCats().size(); i < count; i++) {
                    AnBoCategory tab = mTabs.getCats().get(i);
                    String tabName = tab.getName();
                    mTabHost.addTab(mTabHost.newTabSpec(tabName).setIndicator(createIndicator(i, tabName)).setContent(this));
                }
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mHostFragment.getPage(position);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                mCurrentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public int getCount() {
            if (mTabs == null) {
                return 0;
            }
            return mTabs.getCats().size();
        }

        public void noti(CategoryGroup tabs) {
            mTabs = tabs;
            notifyDataSetChanged();
        }

        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mHostFragment.getActivity());
            return v;
        }

        private View createIndicator(int index, String label) {
            View indicator = mHostFragment.inflate(R.layout.top_tab_indicator_simple);
            TabWidget.LayoutParams params = new TabWidget.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
//			if (0 == index) {
//				params.weight = 2;
//			} else {
//				params.weight = 3;
//			}
            params.weight = 1;
            TextView title = (TextView) indicator.findViewById(android.R.id.title);
            title.setText(label);
//            indicator.setLayoutParams(params);
            return indicator;
        }

    }

    private class TabPagerListener implements NoCacheViewPager.OnPageChangeListener, OnTabChangeListener {

        private NoCacheViewPager mViewPager;
        private TabHost mTabHost;

        private int mIndicatorMargin;
        private int mIndicatorMargin2;

        private View mTabSelectIndicator;
        private View mPrevTab;

        public TabPagerListener(TabHost tabHost, View tabSelectIndicator, NoCacheViewPager pager) {
            mTabHost = tabHost;
            mViewPager = pager;
//			mIndicatorMargin = getDimensionPixelSize(R.dimen.top_tab_indicator_margin);
            mIndicatorMargin = 0;
            mIndicatorMargin2 = 2 * mIndicatorMargin;
            mTabSelectIndicator = tabSelectIndicator;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {
            mTabHost.setCurrentTab(position);
            int distance = 0;
            distance = MeasureUtil.getScreenWidth() / 2 - MeasureUtil.getViewWidth(mTabHost.getTabWidget().getChildAt(position)) / 2;
            mTabScrollView.smoothScrollTo(mTabHost.getTabWidget().getChildAt(position).getLeft() - distance, 0);

            if (mSelectedListener != null) {
                mSelectedListener.onPageSelected(position);
            }

        }

        @Override
        public void onTabChanged(String tabId) {
            mViewPager.setCurrentItem(mTabHost.getCurrentTab(), true);

            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) mTabSelectIndicator.getLayoutParams();
            View selectTab = mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab());
            if (selectTab == null) {
                return;
            }
//			layoutParams.contentWidth = selectTab.getWidth() - mIndicatorMargin2;
//			mTabSelectIndicator.setLayoutParams(layoutParams);

            float fromXDelta = mPrevTab == null ? 0 : mPrevTab.getLeft() + mIndicatorMargin;
            float toXDelta = selectTab.getLeft() + mIndicatorMargin;
            TranslateAnimation moveAnim = new TranslateAnimation(fromXDelta, toXDelta, 0, 0);
            moveAnim.setDuration(200);
            moveAnim.setFillAfter(true);
            mTabSelectIndicator.startAnimation(moveAnim);

            mPrevTab = selectTab;

        }
    }

    public interface OnPageSelectedListener {
        void onPageSelected(int position);
    }

    private OnPageSelectedListener mSelectedListener;

    public void setOnPageSelectedListener(OnPageSelectedListener mSelectedListener) {
        this.mSelectedListener = mSelectedListener;
    }

}
