package com.app.appellahealth.webservices.resp;

import com.app.appellahealth.models.Article;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mehuljoisar on 02/05/18.
 */

public class RespArticleListing extends AbstractResp {

    @SerializedName("payload")
    private List<Article> articleList;

    public RespArticleListing() {
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
