package com.ilmnuri.com.utility;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.ilmnuri.com.R;

import java.io.File;


public class Utils {

    private static ProgressDialog mProgressDialog;


    public static boolean checkFileExist( String PATH){

        File file = new File(PATH );
        return file.exists();
    }
    public static boolean deleteFile(String PATH){

        File file = new File(PATH );
        if(file.exists()){
            file.delete();
            return true;
        } else {
            return false;
        }
    }

    public static void garbageCollect() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


    public static void showOKDialog(Context context, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Safar");
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public static void showToast(final Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static Dialog showProgressDialog(Context mContext, String text,
                                            boolean cancelable) {
        Dialog mDialog = new Dialog(mContext, R.style.ProgressBarTheme);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View layout = mInflater.inflate(R.layout.custom_progressbar, null);
        mDialog.setContentView(layout);

        TextView tvProgressMessage = (TextView) layout
                .findViewById(R.id.tvProgressMessage);

        if (text.equals(""))
            tvProgressMessage.setVisibility(View.GONE);
        else
            tvProgressMessage.setText(text);

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(false);
		/*
		 * mDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND
		 * );
		 */
        mDialog.show();

        return mDialog;
    }

    public static void showProgress(Context mContext) {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            return;

        mProgressDialog = new ProgressDialog(mContext, R.style.ProgressDialogTheme);
        mProgressDialog.setCancelable(false);

        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        mProgressDialog.show();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences objSharedPreferences = null;
        try {
            objSharedPreferences = context.getSharedPreferences(
                    "APP_INDECATOR", Context.MODE_PRIVATE);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return objSharedPreferences;
    }
    public static void setOnPreference(Context context, String key, String value){
        SharedPreferences preferences = getSharedPreferences(context);
        /////////save user information in preference
        SharedPreferences.Editor edit = preferences.edit();

        edit.putString(key, value);


        edit.apply();
    }

    public static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

}
