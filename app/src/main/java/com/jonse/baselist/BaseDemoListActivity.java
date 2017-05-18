package com.jonse.baselist;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.jonse.baselist.bean.Article;
import com.jonse.baselist.view.ArticleView;
import com.jonse.baselist.view.ArticleViewFactory;
import com.yingdi.baseproject.base.BaseRefreshListActivity;
import com.yingdi.baseproject.base.ForceCreateViewHolder;
import com.yingdi.baseproject.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYingDi on 2017/5/18.
 */

public class BaseDemoListActivity extends BaseRefreshListActivity {
    private List<Article> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null) {
            getActionBar().show();
        }
        setTitle("这是一个列表基类");
    }

    /**
     * 页面第一次进来  或者下拉刷新执行这个方法
     */
    @Override
    public void onLoadNew() {
        //开启新线程模拟网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    //回到主线程更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            datas = new ArrayList<>();
                            for (int i = 0; i < 15; i++) {
                                Article article = new Article();
                                if (i % 2 == 0) {
                                    article.setType(Article.NORMAL);
                                } else if (i < 3) {
                                    article.setType(Article.BIG_PIC);
                                } else {
                                    article.setType(Article.MULTI_PIC);
                                }
                                datas.add(article);
                            }
                            update(datas);
                            //判断是否有下一页 如果有填true 没有填false
                            notifyLoadMoreFinish(true);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //这里用真是的接口的话可以使用ATService
//        ATService.getInstance().getArticleList("1", "10", new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                //网络请求成功  主线程
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //网络失败
//            }
//        });
    }

    /**
     * 翻页调用此方法
     */
    @Override
    public void onLoadMore() {
        //这里模拟网络请求 新开线程执行耗时操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    //网络请求回来  回到主线程刷新数据
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            datas = new ArrayList<>();
                            for (int i = 0; i < 15; i++) {
                                Article article = new Article();
                                if (i % 2 == 0) {
                                    article.setType(Article.NORMAL);
                                } else if (i < 3) {
                                    article.setType(Article.BIG_PIC);
                                } else {
                                    article.setType(Article.MULTI_PIC);
                                }
                                datas.add(article);
                            }
                            //追加数据调用此方法
                            appendEnd(datas);
                            notifyLoadMoreFinish(false);
                        }
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 这个方法是封装了一个ViewHolder  也就是Adapter里面的getView方法抽出来了
     * 传入一个view  这个是完整的实现方式   StandRefreshActivity 里面有最简单的实现方式
     */
    @Override
    public ViewHolder createViewHolder(Context context, int position) {
        return new ForceCreateViewHolder<Article>() {
            private ArticleView articleView;

            @Override
            public View getView() {
                return articleView.getView();
            }

            @Override
            public void populate(Article data) {
                if (articleView == null) {
                    articleView = ArticleViewFactory.createView(BaseDemoListActivity.this, data);
                }
                articleView.populate(data);
            }

            @Override
            public boolean forceCreate(Article data) {
                return articleView == null || data.getType() != articleView.getType();
            }
        };
    }

    @Override
    public void addHeaderView() {

    }

    @Override
    public String getKey(int i) {
        return null;
    }

    @Override
    public String getSecondKey(int i) {
        return null;
    }

    @Override
    public void notifyDataChanged(List datas) {

    }

    @Override
    public String getHeader(int i) {
        return null;
    }

}
