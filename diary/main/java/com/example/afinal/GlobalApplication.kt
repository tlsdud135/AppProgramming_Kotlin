package com.example.afinal

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        // 카카오 sdk 초기화
        KakaoSdk.init(this,"85eca6e9223482467272d4d51b03f303")
    }
}