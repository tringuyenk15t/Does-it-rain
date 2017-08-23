package com.tri_nguyen.android.doesitrain.utils;


import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Tri Nguyen on 8/13/2017.
 */

public class NetworkUtils {

    public static final String BASE_URL = "http://api.openweathermap.org";
    public static final String BASE_URL_FOR_IMG = "http://openweathermap.org/img/w/";
    private static final String API_KEY = "d0496d0db7e356842c8774c14d764e88";
    private static final String ID_QUERY = "APPID";

    private static OkHttpClient.Builder httpClientBuilder;
    private static Retrofit.Builder builder =
                new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

    public static <T> T createService(Class<T> serviceClass){
        httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                HttpUrl.Builder builder = chain.request().url().newBuilder();
                builder.addQueryParameter(ID_QUERY,API_KEY);

                HttpUrl url = builder.build();
                Request.Builder requestBuilder = chain.request().newBuilder();
                requestBuilder.url(url);
                return chain.proceed(requestBuilder.build());
            }
        });
        Retrofit retrofit = builder.client(httpClientBuilder.build()).build();
        return  retrofit.create(serviceClass);
    }

}
