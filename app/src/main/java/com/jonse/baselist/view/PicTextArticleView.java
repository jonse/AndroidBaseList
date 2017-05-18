package com.jonse.baselist.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.jonse.baselist.R;
import com.jonse.baselist.bean.Article;


/**
 * Created by WangYingDi on 2017/3/14.
 */
public class PicTextArticleView implements ArticleView {
    private View view;
    private Context context;


    public PicTextArticleView(Context context) {
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.item_article_list_normal, null, false);
    }

    @Override
    public int getType() {
        return Article.NORMAL;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public void populate(Article data) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, WeexDemoActivity.class);
//                context.startActivity(intent);
            }
        });
    }
}
