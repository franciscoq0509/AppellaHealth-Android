package com.app.appellahealth.webservices.resp;

import com.app.appellahealth.models.Article;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mehuljoisar on 02/05/18.
 */

public class RespArticle extends AbstractResp {

    @SerializedName("payload")
    private Article article;

    public RespArticle() {
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
