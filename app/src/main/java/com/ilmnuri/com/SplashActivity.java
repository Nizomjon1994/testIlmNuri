package com.ilmnuri.com;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ilmnuri.com.api.IlmApi;
import com.ilmnuri.com.model.AlbumModel;
import com.ilmnuri.com.model.Global;
import com.ilmnuri.com.model.ListAlbumResult;
import com.ilmnuri.com.utility.CacheUtils;
import com.ilmnuri.com.utility.Utils;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SplashActivity extends BaseActivity {

    private final String TAG_REQUEST = "MY_TAG";
    //    private RequestQueue mVolleyQueue;
    private ArrayList<AlbumModel> albumModels;

    @Inject
    IlmApi mApi;

    @Inject
    CacheUtils mCacheUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        getList();


    }


    private void getList() {
        albumModels = new ArrayList<>();
        mApi.getAlbums(new Callback<ListAlbumResult>() {
            @Override
            public void success(ListAlbumResult listAlbumResult, Response response) {
                if (listAlbumResult != null) {
                    albumModels.addAll(listAlbumResult.getAlbumModels());
                    Global.getInstance().setArrayList(albumModels);
                    mCacheUtils.saveToCache(albumModels);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            SplashActivity.this.finish();
                        }
                    }, 2000);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    Utils.showToast(SplashActivity.this, "Tarmoqda biron bir xatolik bo'ldi! Kesh quruq ekan, internet kerak.");
                    Global.getInstance().setArrayList(mCacheUtils.getAlbums());
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                            SplashActivity.this.finish();
                        }
                    }, 2000);
                }
//                else if (error.getResponse().getStatus())
            }

        });

    }
}
