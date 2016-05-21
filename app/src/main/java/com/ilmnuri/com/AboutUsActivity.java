package com.ilmnuri.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class AboutUsActivity extends AppCompatActivity {

    private TextView tvAboutUs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        tvAboutUs = (TextView)findViewById(R.id.tv_about_us);

        getList();
    }

    private void getList() {

        try {
            String aboutUs = "Assalomu alaykum hurmatli tolibi ilm!\n\n" +
                    "Mazkur ma'ruzalardan Allohning roziligi yo'lida foydalanishda hech qanday " +
                    "huquqiy chegara yo'q. Biroq darslardan tijoriy va boshqa dunyoviy maqsadlarda " +
                    "foydalanish mumkin emas.\n" +
                    "Barcha mualliflik huquqlari ma'ruzalarning voizlariga tegishlidir! " +
                    "Alloh olayotgan ilmimizning nuri ila hayot yo'limizni munavvar qilsin!\n\n" +
                    "Texnik nosozliklar haqida bizga xabar qiling.\n\n" +
                    "Telegramda: http://telegram.me/ilmnuri_apps\n" +
                    "e-mail: ilmnuri@ilmnuri.com\n\n" +
                    "--Team ilmnuri\nwww.ilmnuri.com\n\n2.8 versiya\nMay 20, 2016";
            tvAboutUs.setText(aboutUs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //required for back button
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
