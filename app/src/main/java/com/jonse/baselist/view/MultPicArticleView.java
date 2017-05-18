package com.jonse.baselist.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.jonse.baselist.R;
import com.jonse.baselist.bean.Article;


/**
 * Created by WangYingDi on 2017/3/14.
 */
public class MultPicArticleView implements ArticleView {
    private View view;
    private Context context;

    public MultPicArticleView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.item_article_list_multpic, null, false);
    }

    @Override
    public int getType() {
        return Article.MULTI_PIC;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void populate(Article data) {

    }
}
