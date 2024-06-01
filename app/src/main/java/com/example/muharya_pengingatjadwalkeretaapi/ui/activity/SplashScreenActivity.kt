package com.example.muharya_pengingatjadwalkeretaapi.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin.AdminHomeActivity
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.user.MainActivity
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash_screen)

        val sharedPref = SharedPreferencesLogin(this@SplashScreenActivity)

        Handler(Looper.getMainLooper()).postDelayed({
            if(sharedPref.getSebagai() == "user"){
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }
            else if(sharedPref.getSebagai() == "admin"){
                startActivity(Intent(this@SplashScreenActivity, AdminHomeActivity::class.java))
                finish()
            }
            else{
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
            }
        }, 3000) // 3 detik
    }
}