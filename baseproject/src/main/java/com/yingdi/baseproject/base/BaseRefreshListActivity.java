package com.yingdi.baseproject.base;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

/**
 * 列表Activity
 *
 * @author WangYingDi
 */
public abstract class BaseRefreshListActivity<ListItemType> extends BaseActivity implements ViewHolderFactory<ListItemType> {

    protected BaseAchieveRefreshListFragment<ListItemType> mListFragment;
    protected boolean is_gone_search;
    protected boolean is_show_initialLetter;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mListFragment = new BaseRefreshListFragment1<ListItemType>() {
//            @Override
//            public ViewHolder<ListItemType> createViewHolder(Context context,int position) {
//                return BaseRefreshListActivity.this.createViewHolder(context,position);
//            }
//
//            @Override
//            protected void addHeaderView() {
//                BaseRefreshListActivity.this.addHeaderView();
//            }
//
//            @Override
//            public void onLoadMore() {
//                BaseRefreshListActivity.this.onLoadMore();
//            }
//
//            @Override
//            public void onLoadNew() {
//                BaseRefreshListActivity.this.onLoadNew();
//            }
//
//            @Override
//            public String getKey(int i) {
//                return BaseRefreshListActivity.this.getKey(i);
//            }
//
//            @Override
//            public String getSecondKey(int i) {
//                return BaseRefreshListActivity.this.getSecondKey(i);
//            }
//
//            @Override
//            public void notifyDataChanged(List datas) {
//                BaseRefreshListActivity.this.notifyDataChanged(datas);
//            }
//
//            @Override
//            public String getHeader(int i) {
//                return BaseRefreshListActivity.this.getHeader(i);
//            }
//        };
        mListFragment=new BaseAchieveRefreshListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("is_gone_search", is_gone_search);
        bundle.putBoolean("is_show_initialLetter", is_show_initialLetter);
        mListFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, mListFragment).commit();
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.getCustomView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListView mList = getFragment().getListView();
                    if (mList != null) {
                        if (mList.getFirstVisiblePosition() == 0) {
                            getFragment().setAutoRefresh();
                        } else {
                            mList.setSelection(0);
                        }
                    }
                }
            });
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        getFragment().getListView().setOnItemClickListener(listener);
    }

    public BaseRefreshListFragment<ListItemType> getFragment() {
        return mListFragment;
    }

    /**
     * 更新列表数据
     */
    public void update(List<ListItemType> items) {
        mListFragment.update(items);
    }

    /**
     * 添加指定的项到列表末尾
     */
    public void appendEnd(List<ListItemType> items) {
        mListFragment.appendEnd(items);
    }

    /**
     * 添加指定的项到列表最前
     */
    public void appendStart(List<ListItemType> items) {
        mListFragment.appendStart(items);
    }

    public void notifyNoData() {
        mListFragment.notifyNoData();
    }
    public void notifyNoData(String no_data_text) {
        mListFragment.notifyNoData(no_data_text);
    }
    public void notifyLoadFailed() {
        mListFragment.notifyLoadFailed();
    }

    public void notifyLoading() {
        mListFragment.notifyLoading();
    }

    public void notifyLoadMoreFinish(boolean hasMore) {
        mListFragment.notifyLoadMoreFinish(hasMore);
    }

    public void notifyDataSetChanged() {
        mListFragment.getListAdapter().notifyDataSetChanged();
    }

    /**
     * 需要加载更多时调用
     */
    public abstract void onLoadMore();

    /**
     * 需要加载更多时调用
     */
    public abstract void onLoadNew();

    /**
     * 如果你需要给ListView添加HeaderView 调用此方法
     */
    public abstract void addHeaderView();

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
}
