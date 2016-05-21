package com.ilmnuri.com.module;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ilmnuri.com.AboutUsActivity;
import com.ilmnuri.com.AlbumActivity;
import com.ilmnuri.com.BaseActivity;
import com.ilmnuri.com.ExceptionViewActivity;
import com.ilmnuri.com.MainActivity;
import com.ilmnuri.com.PlayActivity;
import com.ilmnuri.com.SplashActivity;
import com.ilmnuri.com.api.IlmApi;
import com.ilmnuri.com.application.IlmApplication;
import com.ilmnuri.com.utility.CacheUtils;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by User on 18.05.2016.
 */

@Module(
        library = true,
        injects = {
                IlmApplication.class,
                MainActivity.class,
                AlbumActivity.class,
                AboutUsActivity.class,
                ExceptionViewActivity.class,
                PlayActivity.class,
                SplashActivity.class,
                BaseActivity.class
        }
)

public class MainModule {

    private Application application;

    public static final String ENDPOINT = "http://api.ilmnuri.net/api/v2.0/";


    public MainModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .create();
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(Gson gson) {

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.MINUTES);
        client.setReadTimeout(1, TimeUnit.MINUTES);
        OkClient okClient = new OkClient(client);

        return new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setClient(okClient)
                .setConverter(new GsonConverter(gson))
                .build();
    }

    @Provides
    @Singleton
    IlmApi provideApi(RestAdapter restAdapter) {
        return restAdapter.create(IlmApi.class);
    }

    @Provides
    @Singleton
    CacheUtils provideCacheUtils(Gson gson) {
        CacheUtils cacheUtils = new CacheUtils(application, gson);
//        EventBus.getDefault().re
        return cacheUtils;
    }


}
