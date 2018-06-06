package com.app.appellahealth.commons;

import android.util.Log;

import com.app.appellahealth.models.Article;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by mehuljoisar on 03/05/18.
 */

public class Constants {

    private static final String TAG = Constants.class.getSimpleName();

    public static final String ARG_CATEGORY_ID = "category_id";
    public static final String ARG_IS_DETAIL_VIEW = "is_detail_view";
    public static final String ARG_ARTICLE_ID = "article_id";
    public static final String ARG_ARTICLE = "article";
    public static final String ARG_PHOTO = "photo";

    public static List<Article> articleList = new ArrayList<>();

    public static List<Article> getMoreArticles(int index){
        int totalArticles = articleList.size();
        if(totalArticles>0 && (totalArticles>index+1)){
            return articleList.subList(index+1,articleList.size());
        }
        else{
            return new ArrayList<>();
        }
    }

    public static int getIndexOfArticleById(int articleId){
        int totalArticles = articleList.size();
        int indexOfArticleId = 0;
        if(totalArticles>0){
            for(int index=0;index<totalArticles;index++){
                Article article = articleList.get(index);
                if(article.getNewsId()==articleId){
                    indexOfArticleId = index;
                    break;
                }
            }
        }
        return indexOfArticleId;
    }

    public static int getArticleIdByIndex(int position){
        int totalArticles = articleList.size();
        int articleId = 0;
        if(totalArticles>position){
            articleId = articleList.get(position).getNewsId();
        }
        return articleId;
    }

    public static List<Article> getMoreArticlesById(int articleId){
        Log.d(TAG, "getMoreArticlesById: articleId: "+articleId);
        int totalArticles = articleList.size();
        int indexOfArticleId = getIndexOfArticleById(articleId);
        if(totalArticles>(indexOfArticleId+1)){
            return articleList.subList((indexOfArticleId+1),articleList.size());
        }
        return new ArrayList<>();

    }

//TODO: remove following
/*
    user: b@bri.io
    password: appella123
*/
}
