package com.jonse.baselist.view;

import android.content.Context;

import com.jonse.baselist.bean.Article;


/**
 * Created by WangYingDi on 2017/3/14.
 */

public class ArticleViewFactory {
    public static ArticleView createView(Context context, Article article) {

        ArticleView articleView;

        switch (article.getType()) {

            case Article.NORMAL:
                articleView = new PicTextArticleView(context);
                break;
            case Article.BIG_PIC:
                articleView = new BigPicArticleView(context);
                break;
            case Article.MULTI_PIC:
                articleView = new MultPicArticleView(context);
                break;
            default:
                articleView = new PicTextArticleView(context);
                break;
        }

        articleView.populate(article);

        return articleView;
    }
}
