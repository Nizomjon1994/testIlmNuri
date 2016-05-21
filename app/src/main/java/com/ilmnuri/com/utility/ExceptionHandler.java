package com.ilmnuri.com.Utility;

import android.app.Activity;
import android.content.Intent;

import com.ilmnuri.com.ExceptionViewActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler implements
        Thread.UncaughtExceptionHandler {
    private final Activity myContext;

    public ExceptionHandler(Activity context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));

        Intent intent = new Intent(myContext, ExceptionViewActivity.class);
        intent.putExtra("error", "Kutilmagan xato yuzaga keldi. ilmnuri@ilmnuri.com ga email yozing.\n\n");
        myContext.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

}