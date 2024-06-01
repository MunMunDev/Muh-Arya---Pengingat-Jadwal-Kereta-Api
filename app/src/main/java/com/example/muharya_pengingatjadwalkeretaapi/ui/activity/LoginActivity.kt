package com.example.muharya_pengingatjadwalkeretaapi.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.UserModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityLoginBinding
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin.AdminHomeActivity
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.user.MainActivity
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : Activity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var loading: LoadingAlertDialog
    lateinit var sharedPref : SharedPreferencesLogin
    val TAG = "LoginActivityLog"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(view)

        sharedPref = SharedPreferencesLogin(this@LoginActivity)
        loading = LoadingAlertDialog(this@LoginActivity)

        binding.apply {
            btnLogin.setOnClickListener {
                loading.alertDialogLoading()
                if(etUsername.text.isNotEmpty() && etPassword.text.isNotEmpty()){
                    cariDataLogin(etUsername.text.toString().trim(), etPassword.text.toString().trim())
                }
                else if(etUsername.text.isEmpty()){
                    etUsername.error = "Tidak Boleh Kosong !"
                }
                else if(etPassword.text.isEmpty()){
                    etPassword.error = "Tidak Boleh Kosong !"
                }
            }

            tvDaftar.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegistrasiActivity::class.java))

            }
        }

    }

    fun cariDataLogin(username: String, password: String){
        ApiService.getRetrofit().getUser("", username, password)
            .enqueue(object: Callback<ArrayList<UserModel>> {
                override fun onResponse(
                    call: Call<ArrayList<UserModel>>,
                    response: Response<ArrayList<UserModel>>
                ) {
                    if(response.body()!!.size>0){
                        val id = response.body()!![0].id_user
                        val nama = response.body()!![0].nama
                        val alamat = response.body()!![0].alamat
                        val nomorHp = response.body()!![0].nomor_hp
                        val valueUsername = response.body()!![0].username
                        val valuePassword = response.body()!![0].password
                        val sebagai = response.body()!![0].sebagai

                        sharedPref.setLogin(id, nama, alamat, nomorHp, username, password, sebagai)

                        Toast.makeText(this@LoginActivity, "Selamat Datang $nama", Toast.LENGTH_LONG).show()
                        Log.d(TAG, "onResponse id: $id")
                        Log.d(TAG, "onResponse nama: $nama")
                        Log.d(TAG, "onResponse alamat: $alamat")
                        Log.d(TAG, "onResponse nomorHp: $nomorHp")
                        Log.d(TAG, "onResponse username: $username")
                        Log.d(TAG, "onResponse password: $password")
                        Log.d(TAG, "onResponse sebagai: $sebagai")

                        if(sebagai == "user"){
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        }
                        else if(sebagai == "admin"){
                            startActivity(Intent(this@LoginActivity, AdminHomeActivity::class.java))
                            Toast.makeText(this@LoginActivity, "Hy Admin", Toast.LENGTH_SHORT).show()
                        }
                        finish()
                        loading.alertDialogCancel()
                    }
                    else{
                        Toast.makeText(this@LoginActivity, "Data tidak ditemukan..", Toast.LENGTH_SHORT).show()
                        loading.alertDialogCancel()
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserModel>>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, "Data tidak ditemukan ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onFailure: ${t.message}")
//                    Toast.makeText(this@LoginActivity, " $username - $password", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }

            })
    }

    var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@LoginActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)

    }
}