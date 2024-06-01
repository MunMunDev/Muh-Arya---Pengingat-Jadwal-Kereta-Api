package com.example.muharya_pengingatjadwalkeretaapi.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.user.YourAgendaActivity
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.*
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin.AdminHomeActivity
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin.AdminKotaKabTerdaftar
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin.AdminPesananActivity
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin.AdminPostinganActivity
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin.AdminRuteActivity
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin.AdminSemuaAkunActivity
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin.AdminStasiunActivity
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin.AdminTiketActivity
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.user.AkunActivity
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.user.MainActivity
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.user.PostinganActivity
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.user.SemuaJadwalActivity

class KontrolNavigationDrawer(var context: Context) {
    var sharedPreferences = SharedPreferencesLogin(context)
    fun cekSebagai(navigation: com.google.android.material.navigation.NavigationView){
        if(sharedPreferences.getSebagai() == "user"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_user)
        }
        else if(sharedPreferences.getSebagai() == "admin"){
            navigation.menu.clear()
            navigation.inflateMenu(R.menu.nav_menu_admin)
        }
    }
    fun onClickItemNavigationDrawer(navigation: com.google.android.material.navigation.NavigationView, navigationLayout: DrawerLayout, igNavigation:ImageView, activity: Activity){
        navigation.setNavigationItemSelectedListener {
            if(sharedPreferences.getSebagai() == "user"){
                when(it.itemId){
                    R.id.userNavDrawerHome ->{
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerPostingan ->{
                        val intent = Intent(context, PostinganActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerYourAgenda ->{
                        val intent = Intent(context, YourAgendaActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerAllJadwal ->{
                        val intent = Intent(context, SemuaJadwalActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userNavDrawerAkun ->{
                        val intent = Intent(context, AkunActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.userBtnKeluar ->{
                        logout(activity)
                    }
                }
            }
            else if(sharedPreferences.getSebagai() == "admin"){
                when(it.itemId){
                    R.id.adminNavDrawerHome ->{
                        val intent = Intent(context, AdminHomeActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerPostingan ->{
                        val intent = Intent(context, AdminPostinganActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerKotaKabTerdaftar ->{
                        val intent = Intent(context, AdminKotaKabTerdaftar::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerStasiun ->{
                        val intent = Intent(context, AdminStasiunActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerRute ->{
                        val intent = Intent(Intent(context, AdminRuteActivity::class.java))
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerTiket ->{
                        val intent = Intent(context, AdminTiketActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerPesanan ->{
                        val intent = Intent(context, AdminPesananActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.adminNavDrawerSemuaAkun ->{
                        val intent = Intent(context, AdminSemuaAkunActivity::class.java)
                        context.startActivity(intent)
                        activity.finish()
                    }
                    R.id.btnAdminKeluar ->{
                        logout(activity)
                    }
                }

            }
            navigationLayout.closeDrawer(GravityCompat.START)
            true
        }
        // garis 3 navigasi
        igNavigation.setOnClickListener {
            navigationLayout.openDrawer(GravityCompat.START)
        }
    }

    fun logout(activity: Activity){
        val viewAlertDialog = View.inflate(context, R.layout.alert_dialog_logout, null)
        val btnLogout = viewAlertDialog.findViewById<Button>(R.id.btnLogout)
        val btnBatalLogout = viewAlertDialog.findViewById<Button>(R.id.btnBatalLogout)

        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setView(viewAlertDialog)
        val dialog = alertDialog.create()
        dialog.show()
        btnLogout.setOnClickListener {
            sharedPreferences.setLogin(0, "","", "", "", "", "")
            context.startActivity(Intent(context, LoginActivity::class.java))
            activity.finish()
        }
        btnBatalLogout.setOnClickListener {
            dialog.dismiss()
        }
    }
}