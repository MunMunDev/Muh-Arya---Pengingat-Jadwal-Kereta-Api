package com.example.muharya_pengingatjadwalkeretaapi.ui.activity.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.UserModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityAkunBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AkunActivity : Activity() {
    lateinit var binding: ActivityAkunBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var sharedPref : SharedPreferencesLogin
    lateinit var loading: LoadingAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkunBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AkunActivity)
        sharedPref = SharedPreferencesLogin(this@AkunActivity)
        loading = LoadingAlertDialog(this@AkunActivity)

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AkunActivity)

            setData()

            btnUbahData.setOnClickListener {
                dialogUbahData(sharedPref.getNama(), sharedPref.getAlamat(), sharedPref.getNomorHp(), sharedPref.getUsername(), sharedPref.getPassword(), )
            }
        }
    }

    private fun setData(){
        binding.etNama.setText(sharedPref.getNama())
        binding.etAlamat.setText(sharedPref.getAlamat())
        binding.etNomorHp.setText(sharedPref.getNomorHp())
        binding.etUsername.setText(sharedPref.getUsername())
        binding.etPassword.setText(sharedPref.getPassword())
    }

    private fun dialogUbahData(nama:String, alamat:String, nomor_hp:String, username:String, password:String,){
        val viewAlertDialog = View.inflate(this@AkunActivity, R.layout.alert_dialog_update_akun, null)

        val etEditNama = viewAlertDialog.findViewById<TextView>(R.id.etEditNama)
        val etEditAlamat = viewAlertDialog.findViewById<TextView>(R.id.etEditAlamat)
        val etEditNomorHp = viewAlertDialog.findViewById<TextView>(R.id.etEditNomorHp)
        val etEditUsername = viewAlertDialog.findViewById<TextView>(R.id.etEditUsername)
        val etEditPassword = viewAlertDialog.findViewById<TextView>(R.id.etEditPassword)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        etEditNama.text = nama
        etEditAlamat.text = alamat
        etEditNomorHp.text = nomor_hp.toString()
        etEditUsername.text = username
        etEditPassword.text = password

        val alertDialog = AlertDialog.Builder(this@AkunActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            loading.alertDialogLoading()
            postUpdateData(dialogInputan, sharedPref.getId(), etEditNama.text.toString(), etEditAlamat.text.toString(), etEditNomorHp.text.toString(), etEditUsername.text.toString(), etEditPassword.text.toString())
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    private fun postUpdateData(alert: AlertDialog, id:Int, nama:String, alamat:String, nomor_hp:String, username:String, password:String){
        ApiService.getRetrofit().postUpdateUser("", id, nama, alamat, nomor_hp, username, password)
            .enqueue(object: Callback<UserModel>{
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    Toast.makeText(this@AkunActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                    sharedPref.setLogin(id, nama, alamat, nomor_hp, username, password, "user")
                    setData()
                    alert.dismiss()
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    Toast.makeText(this@AkunActivity, "Gagal Update Akun", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
//                    sharedPref.setLogin(id, nama, alamat, nomor_hp, username, password, "user")
//                    setData()
//                    alert.dismiss()
                }

            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@AkunActivity, MainActivity::class.java))
        finish()
    }
}