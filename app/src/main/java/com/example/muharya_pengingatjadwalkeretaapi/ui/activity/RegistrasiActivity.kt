package com.example.muharya_pengingatjadwalkeretaapi.ui.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.MessageNotifPostModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityRegistrasiBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrasiActivity : Activity() {
    lateinit var binding: ActivityRegistrasiBinding
    lateinit var loading: LoadingAlertDialog
    private var TAG = "RegistrasiActivityTag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        val view = binding.root
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(view)

        loading = LoadingAlertDialog(this@RegistrasiActivity)
        binding.apply {
            btnRegistrasi.setOnClickListener {
                if (etNama.text.isNotEmpty()  && etAlamat.text.isNotEmpty()  && etNomorHp.text.isNotEmpty()
                    && etUsername.text.isNotEmpty() && etPassword.text.isNotEmpty()) {

                    loading.alertDialogLoading()
                    postRegistrasi(
                        etNama.text.toString(),
                        etAlamat.text.toString(),
                        etNomorHp.text.toString().trim(),
                        etUsername.text.toString(),
                        etPassword.text.toString()
                    )
                } else if (etNama.text.isEmpty()) {
                    etNama.error = "Tidak Boleh Kosong"
                }else if (etAlamat.text.isEmpty()) {
                    etAlamat.error = "Tidak Boleh Kosong"
                }else if (etNomorHp.text.isEmpty()) {
                    etNomorHp.error = "Tidak Boleh Kosong"
                } else if (etUsername.text.isEmpty()) {
                    etUsername.error = "Tidak Boleh Kosong"
                } else if (etPassword.text.isEmpty()) {
                    etPassword.error = "Tidak Boleh Kosong"
                }
            }

            tvLogin.setOnClickListener {
                finish()
            }
        }
    }

    fun postRegistrasi(nama:String, alamat: String, nomor_hp: String, username: String, password: String) {
        ApiService.getRetrofit().postDaftarUser("", nama, alamat, nomor_hp, username, password, "user")
            .enqueue(object : Callback<ArrayList<MessageNotifPostModel>> {
                override fun onResponse(call: Call<ArrayList<MessageNotifPostModel>>, response: Response<ArrayList<MessageNotifPostModel>>) {
                    Toast.makeText(
                        this@RegistrasiActivity,
                        "Berhasil: ${response.body()!![0].message_response.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<MessageNotifPostModel>>, t: Throwable) {
                    Toast.makeText(this@RegistrasiActivity, "Berhasil ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onFailure: ${t.message}")
                    loading.alertDialogCancel()
                }

            })
    }
}