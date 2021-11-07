package com.iamyeong.aboutyou;

import android.app.Activity;
import android.app.Application;

public class AboutYouApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("AboutYouApplication onCreate");
        setCrashHandler();
    }

    private void setCrashHandler() {
        Thread.UncaughtExceptionHandler handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new AboutYouUncaughtExceptionHandler(this, handler));
    }
}
