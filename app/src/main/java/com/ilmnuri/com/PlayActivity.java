package com.ilmnuri.com;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ilmnuri.com.model.Api;
import com.ilmnuri.com.model.Category;
import com.ilmnuri.com.utility.Utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;

public class PlayActivity extends AppCompatActivity  {

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;

    private ImageView imageView;
    private int currentCategory;
    private String url, trackPath;
    private String fileName;

    boolean readExternalStoragePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        initVariables();
        chechReadStoragePermission();
        initUI();

        File direct = new File(Api.localPath);

        boolean isDirectoryCreated=direct.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated= direct.mkdirs();
        }
        if(isDirectoryCreated) {
            // do something
            Log.d("mkdirs option", "Directory already exists.");
        }

        if (Utils.checkFileExist(Api.localPath + "/" + fileName)) {
            if (readExternalStoragePermission) {
                initMediaPlayer();
            }

        } else {
            if (isNetworkAvailable()) {
                new DownloadFileAsync().execute(url);
            } else {
                Utils.showToast(PlayActivity.this, "INTERNET YO'Q! Yuklay olmaysiz!");
                finish();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void chechReadStoragePermission() {
        int permissinCheck = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            permissinCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissinCheck !=  PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                try {
                    readExternalStoragePermission = true;
                } catch (Exception e) {
                    Utils.showToast(PlayActivity.this, "Diskdan joy berilmaganga o'hshaydi!");
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            200);
                }
            }

        } else {
            readExternalStoragePermission = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 200:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    readExternalStoragePermission = true;
                } else {
                    Utils.showToast(this, "Diskga yozishga ruxsat bermabsiz!");
                    finish();
                }
        }
    }

    private void initVariables() {

        readExternalStoragePermission = false;

        trackPath = getIntent().getStringExtra("url");
        url = Api.BaseUrl + trackPath;
        fileName = url.substring(url.lastIndexOf('/') + 1);
        String catetory = getIntent().getStringExtra("category");

        if (catetory.equals(Category.category1)) {
            currentCategory = 0;
        } else if (catetory.equals(Category.category2)) {
            currentCategory = 1;
        } else {
            currentCategory = 2;
        }

    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        TextView tvTitle = null;
        if (toolbar != null) {
            tvTitle = (TextView) toolbar.findViewById(R.id.tv_play_title);
        }
        if (tvTitle != null) {
            tvTitle.setText(trackPath.replace(".mp3", "").replace("_", " "));
        }

        imageView = (ImageView)findViewById(R.id.iv_play);

        loadImage();
    }

    private void loadImage() {

        switch (currentCategory) {
            case 0:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ilm));
                break;
            case 1:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ilm2));
                break;
            case 2:
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ilm3));
                break;

        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(getString(R.string.Loading));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }
    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aural) {
            int count;

            try {

                URL url = new URL(aural[0]);
                URLConnection connexion = url.openConnection();
                connexion.connect();

                int lenghtOfFile = connexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(Api.localPath + "/" + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Utils.showToast(PlayActivity.this, "Yuklashda xatolik bo'ldi?");
            }
            return null;

        }
        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
            Utils.showToast(PlayActivity.this, "Darslik yuklandi, endi ijro etilmoqda");
            if (readExternalStoragePermission) {
                initMediaPlayer();
            }
        }
    }

    //play music===============start
    private MediaPlayer mediaPlayer;
    public TextView  duration;
    private double timeElapsed = 0, finalTime = 0;
    private Handler durationHandler = new Handler();
    private SeekBar seekbar;

    public void initMediaPlayer(){
        mediaPlayer = MediaPlayer.create(this, Uri.parse(Api.localPath + "/" + fileName));
        finalTime = mediaPlayer.getDuration();
        duration = (TextView) findViewById(R.id.songDuration);
        seekbar = (SeekBar) findViewById(R.id.seekBar);

        if (seekbar != null) {
            seekbar.setMax((int) finalTime);
        }
        assert seekbar != null;
        seekbar.setClickable(true);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekBar.setProgress(progress);
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ImageButton btnStart = (ImageButton) findViewById(R.id.media_play);

        if (btnStart != null) {
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play();
                }
            });
        }
        if (mediaPlayer != null) {
            play();
        }

    }

    // play mp3 song
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            timeElapsed = mediaPlayer.getCurrentPosition();
            seekbar.setProgress((int) timeElapsed);
            durationHandler.postDelayed(updateSeekBarTime, 100);
        }

    }

    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            //get current position
            timeElapsed = mediaPlayer.getCurrentPosition();

            //set seekbar progress
            seekbar.setProgress((int) timeElapsed);
            //set time remaing
            double timeRemaining = finalTime - timeElapsed;
            duration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));

            //repeat yourself that again in 100 miliseconds
            durationHandler.postDelayed(this, 100);
        }
    };

    // pause mp3 song
    public void pause(View view) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }

    }

    // go forward at forwardTime seconds
    public void forward(View view) {
        if (mediaPlayer != null) {
            //check if we can go forward at forwardTime seconds before song endes
            int forwardTime = 7000;
            if ((timeElapsed + forwardTime) <= finalTime) {
                timeElapsed += forwardTime;

                //seek to the exact second of the track
                mediaPlayer.seekTo((int) timeElapsed);
            }
        }

    }

    // go backwards at backwardTime seconds
    public void rewind(View view) {
        if (mediaPlayer != null) {
            //check if we can go back at backwardTime seconds after song starts
            int backwardTime = 7000;
            if ((timeElapsed - backwardTime) > 0) {
                timeElapsed = timeElapsed - backwardTime;

                //seek to the exact second of the track
                mediaPlayer.seekTo((int) timeElapsed);
            }
        }

    }

    /////////////////end


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (readExternalStoragePermission) {
            mediaPlayer.stop();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_play, menu);
        return true;
    }
}