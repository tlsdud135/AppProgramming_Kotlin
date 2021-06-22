package com.example.afinal

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){
    var TAG="kakaologin"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 로그인 조합 예제

        // 로그인 공통 callback 구성
        btn_kakao_login.setOnClickListener {
                val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                    if (error != null) {
                        Log.e(TAG, "로그인 실패", error)
                    } else if (token != null) {
                        Log.i(TAG, "로그인 성공 ${token.accessToken}")
                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Log.e(TAG, "사용자 정보 요청 실패", error)
                            } else if (user != null) {
                                var scopes = mutableListOf<String>()
                                if (user.kakaoAccount?.email != null) {
                                    Log.i(
                                        "KAKAO_API",
                                        "email: ${user.kakaoAccount?.email.toString()}"
                                    )
                                } else if (user.kakaoAccount?.emailNeedsAgreement == true) {
                                    //동의 요청 후 이메일 획득 가능
                                    scopes.add("account_email")
                                    UserApiClient.instance.loginWithNewScopes(
                                        applicationContext,
                                        scopes
                                    ) { token, error ->
                                        if (error != null) {
                                            Log.e(TAG, "사용자 추가 동의 실패", error)
                                        } else {
                                            UserApiClient.instance.me { user, error ->
                                                if (error != null) {
                                                    Log.e(TAG, "사용자 정보 요청 실패", error)
                                                } else if (user != null) {
                                                    Log.i(
                                                        TAG,
                                                        "사용자 정보 요청 성공 : 이메일: ${user.kakaoAccount?.email}"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    // 이메일 획득 불가
                                    Toast.makeText(
                                        applicationContext,
                                        "이메일 획득 불가",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                var intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.putExtra("email", user.kakaoAccount?.email.toString())
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }

                // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(applicationContext)) {
                    UserApiClient.instance.loginWithKakaoTalk(
                        applicationContext,
                        callback = callback
                    )
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(
                        applicationContext,
                        callback = callback
                    )
                }
        }
    }
}