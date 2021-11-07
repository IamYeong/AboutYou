package com.iamyeong.aboutyou;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;

public class AboutYouUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Activity lastActivity;
    private int activityCount = 0;

    public AboutYouUncaughtExceptionHandler(Application application, Thread.UncaughtExceptionHandler exceptionHandler) {

        application.registerActivityLifecycleCallbacks(new SimpleLifecycleCallback() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                //super.onActivityCreated(activity, savedInstanceState);

                lastActivity = activity;
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                //super.onActivityStarted(activity);

                lastActivity = activity;
                activityCount++;
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                //super.onActivityResumed(activity);
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                //super.onActivityPaused(activity);
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                //super.onActivityStopped(activity);

                activityCount--;

                if (activityCount < 0) {
                    lastActivity = null;
                }
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                //super.onActivitySaveInstanceState(activity, outState);
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                //super.onActivityDestroyed(activity);
            }
        });

    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

        /*
        crashlyticsExceptionHandler.uncaughtException(thread, throwable)
        Process.killProcess(Process.myPid())
        System.exit(-1)
         */

        System.out.println("*****" + t.getName());
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));

        Intent intent = new Intent(lastActivity, ErrorActivity.class);
        intent.putExtra("ERROR_MESSAGE", t.getName() + ", " + writer.toString());
        intent.putExtra("LAST_ACTIVITY_INTENT", intent);
        lastActivity.startActivity(intent);
        lastActivity.finish();

    }
}
