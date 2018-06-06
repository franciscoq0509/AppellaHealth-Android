package com.app.appellahealth.webservices.api;

import com.app.appellahealth.BuildConfig;
import com.app.appellahealth.webservices.resp.AbstractResp;
import com.app.appellahealth.webservices.resp.RespAbstract;
import com.app.appellahealth.webservices.resp.RespArticle;
import com.app.appellahealth.webservices.resp.RespArticleListing;
import com.app.appellahealth.webservices.resp.RespLogin;
import com.app.appellahealth.webservices.resp.RespPrivacyPolicy;
import com.app.appellahealth.webservices.resp.RespUserInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    public static final String BASE_URL = BuildConfig.BASE_URL;


    public static class WebServiceMethods{
        
        public static final String login = "login";
        public static final String register = "register";
        public static final String forgotPassword = "forgot_password";
        public static final String news = "news";
        public static final String updateUser = "update_user";
        public static final String userInfo = "user_info";
        public static final String privacyPolicy = "privacy_policy";
        public static final String recordDeepView = "record_deep_view";

    }

    public static class WebServiceParameters {
        public static final String email = "email";
        public static final String password = "password";
        public static final String firstName = "first_name";
        public static final String lastName = "last_name";
        public static final String categoryId = "category_id";
        public static final String xApiKey = "x-api-key";
        public static final String newsId = "news_id";
        public static final String notifications = "notifications";
    }

    @FormUrlEncoded
    @POST(WebServiceMethods.login)
    Call<RespLogin> login(
            @Field(WebServiceParameters.email)String email,
            @Field(WebServiceParameters.password)String password
    );

    @FormUrlEncoded
    @POST(WebServiceMethods.register)
    Call<RespAbstract> register(
            @Field(WebServiceParameters.email)String email,
            @Field(WebServiceParameters.password)String password,
            @Field(WebServiceParameters.firstName)String firstName,
            @Field(WebServiceParameters.lastName)String lastName
    );

    @FormUrlEncoded
    @POST(WebServiceMethods.forgotPassword)
    Call<RespAbstract> forgotPassword(
            @Field(WebServiceParameters.email)String email
    );

    @GET(WebServiceMethods.privacyPolicy)
    Call<RespPrivacyPolicy> privacyPolicy(
    );

    @GET(WebServiceMethods.userInfo)
    Call<RespUserInfo> userInfo(
    );

    @FormUrlEncoded
    @POST(WebServiceMethods.updateUser)
    Call<RespAbstract> updateUser(
            @Field(WebServiceParameters.notifications)int notifications
    );


    @GET(WebServiceMethods.news)
    Call<RespArticleListing> getArticleListing(
            @Query(WebServiceParameters.categoryId) String categoryId
    );


    @GET(WebServiceMethods.news+"/{news_id}")
    Call<RespArticle> getArticleDetails(
            @Path(WebServiceParameters.newsId) Integer newsId
    );

    @POST(WebServiceMethods.news+"/"+WebServiceMethods.recordDeepView+"/{news_id}")
    Call<RespAbstract> recordDeepView(
            @Path(WebServiceParameters.newsId) Integer newsId
    );



}
