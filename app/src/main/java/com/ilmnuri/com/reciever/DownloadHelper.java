package com.ilmnuri.com.reciever;

import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ilmnuri.com.event.AudioEvent;
import com.ilmnuri.com.model.Audio;
import com.ilmnuri.com.model.Global;
import com.ilmnuri.com.utility.Utils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;

import de.greenrobot.event.EventBus;

/**
 * Created by User on 18.05.2016.
 */
public class DownloadHelper extends Service {

    DownloadManager downloadManager;
    File dir;
    private Audio mAudio;
    private HashMap<Long, Audio> specialFeedItem;
    Gson mGson;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            mGson = new Gson();
            String filePath = intent.getStringExtra("file_path");
            String fileName = intent.getStringExtra("file_name");
            String audio = intent.getStringExtra("audio");
            specialFeedItem = new HashMap<>();
            Type type = new TypeToken<Audio>() {
            }.getType();
            mAudio = mGson.fromJson(audio, type);
            dir = new File(getExternalFilesDir(null), "audio");
            if (!dir.exists()) {
                dir.mkdir();
            }

            downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri download_uri = Uri.parse(filePath);
            DownloadManager.Request request = new DownloadManager.Request(download_uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setAllowedOverRoaming(false);
            request.setDescription("Test");
            request.setTitle("Test");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setDestinationInExternalFilesDir(this, dir.getName(), fileName);
            final long enqueue = downloadManager.enqueue(request);

            specialFeedItem.put(enqueue, mAudio);
            Global.getInstance().setAudio(mAudio);
            EventBus.getDefault().post(AudioEvent.download(mAudio));

            new Thread(new Runnable() {

                @Override
                public void run() {

                    boolean downloading = true;

                    while (downloading) {

                        DownloadManager.Query q = new DownloadManager.Query();

                        q.setFilterById(enqueue);

                        Cursor cursor = downloadManager.query(q);
                        cursor.moveToFirst();
                        int bytes_downloaded = cursor.getInt(cursor
                                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        final int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                            downloading = false;
//                        mProgressDialog.dismiss();
//                        if (readExternalStoragePermission) {
//                            initMediaPlayer();
//                        }
//                        Utils.showToast(getApplicationContext(), "Darslik yuklandi, endi ijro etilmoqda");
                            EventBus.getDefault().post(AudioEvent.stop(mAudio));
                            Global.getInstance().setAudio(null);
                        } else {
                            if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_FAILED) {
                                Utils.showToast(getApplicationContext(), "Yuklashda xatolik bo'ldi?");

                            }
                        }


                        final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);


                        Global.getInstance().setCurrent_position(dl_progress);

                        cursor.close();
                    }

                }
            }).start();

        }


        return START_STICKY;
    }
}
