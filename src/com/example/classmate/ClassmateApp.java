package com.example.classmate;

import android.app.Application;

public class ClassmateApp extends Application {
    public static String storageRootDir = "/classmate/";

    @Override
    public void onCreate() {

        super.onCreate();

        CustomException customException = CustomException.getInstance();

        customException.init(getApplicationContext());
    }
}
