package com.example.week2_prj;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {
    private static GlobalApplication instance; // 인스턴스 설정

    public static final GlobalApplication getGlobalApplicationContext(){
        if(instance==null){
            throw new IllegalStateException("GlobalApplication does not inherit com.kakao.GlobalApplication");
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // kakao SDK 초기화
        KakaoSdk.init(this, "372e30fe9996d31aee0bf2b23bb09984");
    }
}
