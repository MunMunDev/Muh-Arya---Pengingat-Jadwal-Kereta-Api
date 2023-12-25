package com.example.muharya_pengingatjadwalkeretaapi.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.adapter.PesananAdapter
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PesananModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityYourAgendaBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YourAgendaActivity : Activity() {
    lateinit var binding: ActivityYourAgendaBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var sharedPref : SharedPreferencesLogin
    lateinit var arrayPesanan: ArrayList<PesananModel>
    lateinit var adapter: PesananAdapter
    lateinit var loading:LoadingAlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYourAgendaBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        arrayPesanan = arrayListOf()
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@YourAgendaActivity)
        sharedPref = SharedPreferencesLogin(this@YourAgendaActivity)
        loading = LoadingAlertDialog(this@YourAgendaActivity)

        loading.alertDialogLoading()
        getPesanan(sharedPref.getId())
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@YourAgendaActivity)

        }
    }

    fun getPesanan(id_user: Int){
        ApiService.getRetrofit().getPesanan("", id_user)
            .enqueue(object: Callback<ArrayList<PesananModel>>{
                override fun onResponse(
                    call: Call<ArrayList<PesananModel>>,
                    response: Response<ArrayList<PesananModel>>
                ) {
                    arrayPesanan = response.body()!!
                    if(arrayPesanan.isNotEmpty()){
                        arrayPesanan = arrayListOf()
                        for(value in response.body()!!){
                            if(value.id_pesanan!="-"){
                                arrayPesanan.add(value)
                            }
                        }
                        adapter = PesananAdapter(arrayPesanan, object:PesananAdapter.onClickMenuListener{
                            override fun onClick(array: PesananModel) {

                            }

                        })
                        binding.rvJadwal.layoutManager = LinearLayoutManager(this@YourAgendaActivity)
                        binding.rvJadwal.adapter = adapter
                    }
                    else{
                        Toast.makeText(this@YourAgendaActivity, "Tidak Ada Jadwal Anda", Toast.LENGTH_SHORT).show()
                    }
                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<PesananModel>>, t: Throwable) {
                    loading.alertDialogCancel()
                }

            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@YourAgendaActivity, MainActivity::class.java))
        finish()
    }
}