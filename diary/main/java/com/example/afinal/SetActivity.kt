package com.example.afinal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.bottom_navigation
import kotlinx.android.synthetic.main.activity_set.*

class SetActivity : AppCompatActivity() {
    var TAG="kakaologin"
    var em=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set)

        em= intent.getStringExtra("email").toString()
        Log.e("main", em)
        tv_email.text="사용자: "+em

        btn_out.setOnClickListener {
            Toast.makeText(applicationContext,"정상적으로 로그아웃 되었습니다.",Toast.LENGTH_SHORT).show()
            // 로그아웃
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e(TAG, "로그아웃 실패. SDK에서 토큰 삭제됨", error)
                }
                else {
                    Log.i(TAG, "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
            var intent= Intent(this@SetActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }



        bottom_navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.tab1 -> {

                    var intent= Intent(this@SetActivity,CalActivity::class.java)
                    intent.putExtra("email", em)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab2 -> {

                    var intent= Intent(this@SetActivity,MainActivity::class.java)
                    intent.putExtra("email", em)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.tab3 -> {


                    return@setOnNavigationItemSelectedListener true
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

}