package com.iamyeong.aboutyou;

import android.app.Application;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.common.util.Utility;

public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String nativeAppKey = getString(R.string.kakao_native_app_key);
        KakaoSdk.init(this, nativeAppKey);
        System.out.println(nativeAppKey);

    }
}
