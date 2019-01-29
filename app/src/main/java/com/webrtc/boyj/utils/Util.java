package com.webrtc.boyj.utils;

import com.webrtc.boyj.api.data.stun.TurnServer;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Util {
    private static Util instance;
    private static final String URL = "https://global.xirsys.net";

    public static Util getInstance() {
        if(instance == null)
            instance = new Util();
        return instance;
    }
    private Retrofit retrofit;

    public TurnServer getRetrofit() {
        if(retrofit == null) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(TurnServer.class);
    }

}
