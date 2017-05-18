package com.yingdi.baseproject.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yingdi.baseproject.R;
import com.yingdi.baseproject.ptr.PtrDefaultHandler;
import com.yingdi.baseproject.ptr.PtrFrameLayout;
import com.yingdi.baseproject.ptr.PtrHandler;
import com.yingdi.baseproject.refresh.RefreshFrameLayout;

import java.io.Serializable;
import java.util.List;

/**
 * Base class of our list fragments
 *
 * @param <ListItemType> 列表项的数据类型
 * @author WangYingDi
 */
public abstract class BaseRefreshListFragment<ListItemType> extends BaseFragment implements ViewHolderFactory<ListItemType>, PtrHandler, Populator, OnScrollListener, Serializable {

    private ListView mList;
    protected View mLoading;
    private View mLoadState;
    private TextView mState;
    private ImageView mStateIcon;
    private View mRefreshTip;
    private View content;
    private CustomableListAdapter mAdapter;
    private View mLoadingMoreView;
    private View mNoMoreDataView;
    private TextView mUnLoad;
    private LinearLayout mIsLoading;

    private boolean mIsCanLoadMore = false;
    private boolean mNoLoading = true;
    private int mFooterCount;
    private RelativeLayout mLayout;
    private EditText mSearch;
    private View mSearchLayout;
    private View mSearchTitle;
    private View mClear;
    private InputMethodManager inputMethodManager;
    private View mSearchTitleLayout;
    private Thread thread;
    private RefreshFrameLayout mRefreshFrame;
    private boolean startThread = false;
    private boolean is_nodata = false;
    private TextView mLoadingText;
    protected boolean is_gone_search;
    protected boolean is_show_title_bar;
    private boolean SCROLLING;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what % 3) {
                case 0:
                    mLoadingText.setText("正在加载中.");
                    break;
                case 1:
                    mLoadingText.setText("正在加载中..");
                    break;
                case 2:
                    mLoadingText.setText("正在加载中...");
                    break;
            }

        }
    };
    private int i = 0;
    protected View mTitleBar;
    private View mBack;
    private TextView mTitle;
    private View mCancel;
    private TextView mRightText;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        Bundle arguments = getArguments();
        if (arguments != null) {
            is_gone_search = getArguments().getBoolean("is_gone_search", false);
            is_show_title_bar = getArguments().getBoolean("is_show_title_bar", false);
        }
        content = inflate(R.layout.refresh_fragment_list);
        mTitleBar = content.findViewById(R.id.title_bar);
        if (is_show_title_bar) {
            mTitleBar.setVisibility(View.VISIBLE);
        } else {
            mTitleBar.setVisibility(View.GONE);
        }
        mTitleBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList != null) {
                    if (mList.getFirstVisiblePosition() == 0) {
                        setAutoRefresh();
                    } else {
                        mList.setSelection(0);
                    }
                }
            }
        });
        mRefreshFrame = (RefreshFrameLayout) content.findViewById(R.id.refresh_frame);
        mBack = content.findViewById(R.id.back);
        mTitle = ((TextView) content.findViewById(R.id.title));
        mCancel = content.findViewById(R.id.cancel);
        mRightText = ((TextView) content.findViewById(R.id.right_text));
        mSearch = ((EditText) content.findViewById(R.id.search));
        mSearchLayout = content.findViewById(R.id.search_layout);
        mSearchTitle = content.findViewById(R.id.search_title);
        mSearchTitleLayout = content.findViewById(R.id.search_title_layout);
        mClear = content.findViewById(R.id.clear);
        mLayout = ((RelativeLayout) content.findViewById(R.id.layout));
        mList = (ListView) content.findViewById(R.id.list);
        mList.setDivider(null);
        mLoading = content.findViewById(R.id.loading);
        mLoadState = content.findViewById(R.id.load_state);
        mState = (TextView) content.findViewById(R.id.state);
        mStateIcon = (ImageView) content.findViewById(R.id.state_icon);
        mRefreshTip = content.findViewById(R.id.tip);
        //加载中效果
        TranslateAnimation alphaAnimation2 = new TranslateAnimation(0, 0, 0, 30);
        alphaAnimation2.setDuration(350);
        alphaAnimation2.setRepeatCount(Animation.INFINITE);
        alphaAnimation2.setRepeatMode(Animation.REVERSE);
//        content.findViewById(R.id.loading_bg).startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.loading_rotate));
        content.findViewById(R.id.loading_bg).setAnimation(alphaAnimation2);
        alphaAnimation2.start();
        content.findViewById(R.id.state_loading);
        mLoadingText = ((TextView) content.findViewById(R.id.state_loading));
//        autoIncrement(mLoadingText, 0, 12, 5000);
        mRefreshFrame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!is_nodata) {
                    onLoadNew();
                    notifyLoading();
                }
            }
        });

        mLoadingMoreView = inflate(R.layout.loading);
        mNoMoreDataView = inflate(R.layout.no_more_data);
        mIsLoading = (LinearLayout) mLoadingMoreView.findViewById(R.id.loading);
        mUnLoad = (TextView) mLoadingMoreView.findViewById(R.id.unload);

        mAdapter = new CustomableListAdapter(this) {
            @Override
            public String getKey(int i) {
                return BaseRefreshListFragment.this.getKey(i);
            }

            @Override
            public String getSecondKey(int i) {
                return BaseRefreshListFragment.this.getSecondKey(i);
            }

            @Override
            public void notifyDataChanged(List datas) {
                BaseRefreshListFragment.this.notifyDataChanged(datas);
            }
        };
//        refreshableView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
        mList.setEmptyView(mLoading);
//        mList.setOnRefreshListener(this);
        mList.setOnScrollListener(this);
        //设置没有滚动条
        mList.setVerticalScrollBarEnabled(false);
        mRefreshFrame.setLastUpdateKey(getClass().getName());
        mRefreshFrame.setPtrHandler(this);
        mRefreshFrame.disableWhenHorizontalMove(true);
//        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.loading_rotate);
//        animation.setDuration(1000);
//        mLoadingMoreView.findViewById(R.id.imageview_loading).startAnimation(animation);
        mUnLoad.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onLoadMore();
            }
        });

        mRefreshFrame.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mRefreshTip.getVisibility() == View.VISIBLE) {
                    notifyLoading();
                    onLoadNew();
                }
            }
        }, 100);
        if (is_gone_search) {
            mSearchLayout.setVisibility(View.GONE);
        }
        mSearchTitleLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchTitle.setVisibility(View.GONE);
                mSearch.setVisibility(View.VISIBLE);
                mSearch.setFocusable(true);
                mSearch.setFocusableInTouchMode(true);
                mSearch.requestFocus();
                showSoftKeyboard();
            }
        });
        mSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getListAdapter().getFilter().filter(s);
                if (s.length() > 0) {
                    mClear.setVisibility(View.VISIBLE);
                } else {
                    mClear.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {

            }
        });
        mClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearch.getText().clear();
                mSearch.setVisibility(View.GONE);
                mSearchTitle.setVisibility(View.VISIBLE);
                hideSoftKeyboard();
            }
        });
        mList.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });
        addHeaderView();
        mList.setAdapter(mAdapter);
        return content;
    }

    public void setListBackground(String color) {
        mList.setBackgroundColor(Color.parseColor(color));
    }

    public void setListDividerHide(boolean hide) {
        if (hide) {
            mList.setDivider(null);
            mList.setDividerHeight(0);

        } else {
            mList.setDivider(new ColorDrawable(0xffeeeeee));
            mList.setDividerHeight(1);
        }
    }

    public void setAutoRefresh() {
        mRefreshFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshFrame.autoRefresh(true);
            }
        }, 100);
    }

    /**
     * @param title string
     */
    protected void setTitle(String title) {
        mTitle.setText(title);
    }

    protected void setBackVisible(int visible) {
        mBack.setVisibility(visible);
    }

    //    void autoIncrement(final TextView target, final int start,
//                       final int end, long duration) {
//
//        ValueAnimator animator = ValueAnimator.ofInt(start, end);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                int animatedValue = (int) animation.getAnimatedValue();
//                animation.get
//                switch (animatedValue % 3) {
//                    case 0:
//                        target.setText("正在加载中.");
//                        break;
//                    case 1:
//                        target.setText("正在加载中..");
//                        break;
//                    case 2:
//                        target.setText("正在加载中...");
//                        break;
//                }
//            }
//        });
//        animator.setRepeatCount(Animation.INFINITE);
//        animator.setRepeatMode(Animation.RESTART);
//        animator.setDuration(duration);
//        animator.start();
//
//    }
    public void addHeaderView(View view) {
        mList.addHeaderView(view);
    }

    public void initDividerHeight(int height) {
        mList.setDivider(new ColorDrawable(0xffeeeeee));
        mList.setDividerHeight(height);
    }

    private void addFooterView(View view) {
        mList.addFooterView(view);
        mFooterCount = mFooterCount + 1;
    }

    public void notifyRefreshFinish() {
        mRefreshFrame.refreshComplete();
    }

    private void removeFooterView(View view) {
        boolean b = mList.removeFooterView(view);
        if (b)
            mFooterCount = mFooterCount - 1;
    }

    public int getFooterViewsCount() {
        return mFooterCount;
    }

    public ListView getListView() {
        return mList;
    }


    public CustomableListAdapter getListAdapter() {
        return mAdapter;
    }

    public void setOnScrollListener(OnScrollListener listener) {
        mList.setOnScrollListener(listener);
    }

    public void update(List<ListItemType> items) {
        mAdapter.update(items);

    }

    public void update(Object[] items) {
        mAdapter.update(items);
    }

    public void cleardata() {
        mAdapter.clear();
    }

    public void appendStart(List<ListItemType> items) {
        mAdapter.add(0, items);
        mAdapter.notifyDataSetChanged();
    }

    public void appendStart(ListItemType item) {
        mAdapter.add(0, item);
        mAdapter.notifyDataSetChanged();
    }

    public void appendEnd(List<ListItemType> items) {
        mAdapter.add(items);
        mAdapter.notifyDataSetChanged();
//        mList.onRefreshComplete();
    }

    public void notifyNoData() {
        if (getView() != null) {
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoading.setVisibility(View.GONE);
                    mLoadState.setVisibility(View.VISIBLE);
                    mState.setText(R.string.load_state_no_data);
                    mRefreshTip.setVisibility(View.INVISIBLE);
                }
            }, 500);
        } else {
            mLoading.setVisibility(View.GONE);
            mLoadState.setVisibility(View.VISIBLE);
            mState.setText(R.string.load_state_no_data);
            mRefreshTip.setVisibility(View.INVISIBLE);
        }

        startThread = false;
        is_nodata = true;
        cleardata();
        notifyRefreshFinish();
    }

    public void notifyNoData(final String no_data_text) {
        if (getView() != null) {
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoading.setVisibility(View.GONE);
                    mLoadState.setVisibility(View.VISIBLE);
                    mState.setText(no_data_text);
                    mRefreshTip.setVisibility(View.INVISIBLE);
                }
            }, 500);
        } else {
            mLoading.setVisibility(View.GONE);
            mLoadState.setVisibility(View.VISIBLE);
            mState.setText(no_data_text);
            mRefreshTip.setVisibility(View.INVISIBLE);
        }

        startThread = false;
        is_nodata = true;
        cleardata();
        notifyRefreshFinish();
    }

    public void notifyNoData(final String no_data_text, final int drawble_id) {
        if (getView() != null) {
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoading.setVisibility(View.GONE);
                    mLoadState.setVisibility(View.VISIBLE);
                    mState.setText(no_data_text);
                    mRefreshTip.setVisibility(View.INVISIBLE);
                    mStateIcon.setImageResource(drawble_id);
                }
            }, 500);
        } else {
            mLoading.setVisibility(View.GONE);
            mLoadState.setVisibility(View.VISIBLE);
            mState.setText(no_data_text);
            mRefreshTip.setVisibility(View.INVISIBLE);
            mStateIcon.setImageResource(drawble_id);
        }

        startThread = false;
        is_nodata = true;
        cleardata();
    }

    public void notifyLoadFailed() {
        mIsLoading.setVisibility(View.GONE);
        mUnLoad.setVisibility(View.VISIBLE);
        if (getView() != null) {
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoading.setVisibility(View.GONE);
                }
            }, 500);
        } else {
            mLoading.setVisibility(View.GONE);
        }
        mLoadState.setVisibility(View.VISIBLE);
//        mStateIcon.setImageResource(R.drawable.icon_load_failed);
        mState.setText(R.string.load_state_failed);
        mRefreshTip.setVisibility(View.VISIBLE);
        startThread = false;
        notifyRefreshFinish();
    }

    public void notifyLoading() {
//		mRefreshTip.setVisibility(View.INVISIBLE);
        mNoLoading = false;
        mLoading.setVisibility(View.VISIBLE);
        mLoadState.setVisibility(View.GONE);
        startThread = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (startThread) {
                    handler.sendMessage(handler.obtainMessage(i));
                    i++;
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    public void notifyLoadMoreFinish(boolean hasMore) {

        removeFooterView(mNoMoreDataView);
        if (hasMore) {
            if (getFooterViewsCount() == 0) {
                addFooterView(mLoadingMoreView);
            }
            mIsCanLoadMore = true;
        } else {
            mIsCanLoadMore = false;
            if (getFooterViewsCount() == 1) {
                removeFooterView(mLoadingMoreView);
            }
                addFooterView(mNoMoreDataView);
        }

        mIsLoading.setVisibility(View.VISIBLE);
        mLoadState.setVisibility(View.GONE);
        if (getView() != null) {
            getView().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoading.setVisibility(View.GONE);
                }
            }, 100);
        } else {
            mLoading.setVisibility(View.GONE);
        }
        mUnLoad.setVisibility(View.GONE);
        startThread = false;
        notifyRefreshFinish();
        mNoLoading = true;
        mList.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mList.getChildCount() == mList.getCount()) {
                    removeFooterView(mNoMoreDataView);
                }
            }
        }, 100);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mList.setOnItemClickListener(listener);
    }

    /**
     * 如果你需要给ListView添加HeaderView 调用此方法添加
     */
    protected abstract void addHeaderView();

    /**
     * 需要加载更多时调用
     */
    public abstract void onLoadMore();

    /**
     * 需要加载最新数据时调用
     */
    public abstract void onLoadNew();

    /**
     * 搜索的时候需要返回要搜索的单个数据源 不搜索不需要管
     */
    public abstract String getKey(int i);

    /**
     * 搜索的时候需要返回要搜索的第二个数据源 不搜索不需要管
     */
    public abstract String getSecondKey(int i);

    /**
     * 搜索完手动提醒数据源更新 不搜索不需要管
     */
    public abstract void notifyDataChanged(List datas);

    /**
     * 带header分类的时候增加分类名字
     */
    public abstract String getHeader(int i);

    @SuppressWarnings("unchecked")
    @Override
    public View populate(int position, View convertView, ViewGroup parent, Object item) {
        ViewHolder<ListItemType> viewHolder;
        if (convertView == null) {
            viewHolder = createViewHolder(getActivity(), position);
        } else {
            viewHolder = (ViewHolder<ListItemType>) convertView.getTag();
            if (viewHolder instanceof ForceCreateViewHolder && ((ForceCreateViewHolder<ListItemType>) viewHolder).forceCreate((ListItemType) item)) {
                viewHolder = createViewHolder(getActivity(), position);
            }
        }
        viewHolder.populate((ListItemType) item);
        convertView = viewHolder.getView();
        convertView.setTag(viewHolder);
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        switch (scrollState) {
            case SCROLL_STATE_FLING:
            case SCROLL_STATE_TOUCH_SCROLL:
                SCROLLING = true;
                break;
            case SCROLL_STATE_IDLE:
                SCROLLING = false;
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            if (mIsCanLoadMore && mNoLoading && SCROLLING) {
                mNoLoading = false;
                onLoadMore();
            }
        }
    }

    protected void showSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        if (mAdapter.getCount() == 0) {
            return false;
        }
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mList, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        onLoadNew();
    }
}