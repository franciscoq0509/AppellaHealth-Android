package com.app.appellahealth.webservices.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RestClient {

    private static final String TAG = RestClient.class.getSimpleName();

    public static Api REST_CLIENT;
    public static Retrofit RETROFIT;

    private RestClient() {
    }

    public static Api get(String accessToken) {
        if(REST_CLIENT==null){
            REST_CLIENT = setupRestClient(Api.BASE_URL,accessToken);
        }
        return REST_CLIENT;
    }

    private static Api setupRestClient(String url, String accessToken) {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        RETROFIT = new Retrofit.Builder()
                .baseUrl(url)
                .client(getOkHttpClient(accessToken))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return RETROFIT.create(Api.class);
    }

    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.networkInterceptors().add(logging);
        return httpClient.build();
    }

    private static OkHttpClient getOkHttpClient(String accessToken) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (accessToken != null) {
            httpClient.networkInterceptors().add(new AddHeaderInterceptor(accessToken));
        }
        httpClient.networkInterceptors().add(logging);

        return httpClient.build();
    }


    public static class AddHeaderInterceptor implements Interceptor {

        private String accessToken;

        public AddHeaderInterceptor() {
        }

        public AddHeaderInterceptor(String accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader(Api.WebServiceParameters.xApiKey, this.accessToken);

            return chain.proceed(builder.build());
        }
    }

}
