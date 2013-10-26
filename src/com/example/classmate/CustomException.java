package com.example.classmate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class CustomException implements UncaughtExceptionHandler {
    // 获取application 对象；
    private Context mContext;

    private Thread.UncaughtExceptionHandler defaultExceptionHandler;
    // 单例声明CustomException;
    private static CustomException customException;

    private CustomException() {
    }

    public static CustomException getInstance() {
        if (customException == null) {
            customException = new CustomException();
        }
        return customException;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {

            // Log.e("tag",
            // "exception >>>>>>>"+exception.getLocalizedMessage());
            // 将异常抛出，则应用会弹出异常对话框.这里先注释掉
        String path = Environment.getExternalStorageDirectory() + ClassmateApp.storageRootDir + "error.log";
        File f = new File(path);
        try {
            PrintWriter pw = new PrintWriter(f);
            exception.printStackTrace(pw);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        defaultExceptionHandler.uncaughtException(thread, exception);
    }

    public void init(Context context) {
        mContext = context;
        defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

}