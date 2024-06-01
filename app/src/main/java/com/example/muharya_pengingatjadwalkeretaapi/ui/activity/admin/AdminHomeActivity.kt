package com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin;

import android.app.Activity
import android.content.Intent

import android.os.Bundle;
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityAdminHomeBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer

class AdminHomeActivity: Activity() {
    lateinit var binding: ActivityAdminHomeBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminHomeActivity)

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminHomeActivity)

            btnPostingan.setOnClickListener {
                startActivity(Intent(this@AdminHomeActivity, AdminPostinganActivity::class.java))
                finish()
            }
            btnKotaKab.setOnClickListener {
                startActivity(Intent(this@AdminHomeActivity, AdminKotaKabTerdaftar::class.java))
                finish()
            }
            btnStasiun.setOnClickListener {
                startActivity(Intent(this@AdminHomeActivity, AdminStasiunActivity::class.java))
                finish()
            }
            btnRute.setOnClickListener {
                startActivity(Intent(this@AdminHomeActivity, AdminRuteActivity::class.java))
                finish()
            }
            btnTiket.setOnClickListener {
                startActivity(Intent(this@AdminHomeActivity, AdminTiketActivity::class.java))
                finish()
            }
            btnPesanan.setOnClickListener {
                startActivity(Intent(this@AdminHomeActivity, AdminPesananActivity::class.java))
                finish()
            }
            btnSemuaAkun.setOnClickListener {
                startActivity(Intent(this@AdminHomeActivity, AdminSemuaAkunActivity::class.java))
                finish()
            }

        }

    }

    var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@AdminHomeActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)

    }
}