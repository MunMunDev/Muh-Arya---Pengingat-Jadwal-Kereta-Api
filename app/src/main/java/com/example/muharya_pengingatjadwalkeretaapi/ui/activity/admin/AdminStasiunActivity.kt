package com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin;

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent

import android.os.Bundle;
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.adapter.AdminStasiunAdapter
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.KotaKabModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.StasiunModel

import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityAdminStasiunBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class AdminStasiunActivity : Activity() {
    lateinit var binding: ActivityAdminStasiunBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var loading: LoadingAlertDialog

    lateinit var arrayStasiun: ArrayList<StasiunModel>
    lateinit var arrayKotaKab: ArrayList<String>
    lateinit var adapter: AdminStasiunAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminStasiunBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminStasiunActivity)
        loading = LoadingAlertDialog(this@AdminStasiunActivity)
        loading.alertDialogLoading()

        getData()
        getDataKotaKab()

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminStasiunActivity)

            btnTambah.setOnClickListener {
                dialogTambahData()
            }
        }

    }

    private fun getData(){
        ApiService.getRetrofit().getStasiunAdmin("")
            .enqueue(object: Callback<ArrayList<StasiunModel>> {
                override fun onResponse(
                    call: Call<ArrayList<StasiunModel>>,
                    response: Response<ArrayList<StasiunModel>>
                ) {
                    arrayStasiun = arrayListOf()
                    if(response.body()!!.isNotEmpty()){
                        arrayStasiun = response.body()!!

                        adapter = AdminStasiunAdapter(arrayStasiun, object: AdminStasiunAdapter.ClickListener{
                            override fun onClick(stasiun: StasiunModel, position: Int, it: View) {
                                val popupMenu = PopupMenu(this@AdminStasiunActivity, it)
                                popupMenu.inflate(R.menu.popup_menu)
                                popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                                        when (menuItem!!.itemId) {
                                            R.id.edit -> {
                                                dialogUpdateData(stasiun)
                                                return true
                                            }

                                            R.id.hapus -> {
                                                dialogHapusData(stasiun)
                                                return true
                                            }
                                        }
                                        return true
                                    }

                                })
                                popupMenu.show()
                            }

                        })

                        binding.rvStasiun.layoutManager = LinearLayoutManager(this@AdminStasiunActivity)
                        binding.rvStasiun.adapter = adapter

                    }
                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<StasiunModel>>, t: Throwable) {
                    Toast.makeText(this@AdminStasiunActivity, "Gagal. Periksa Jaringan Anda", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }

            })
    }

    fun getDataKotaKab(){
        ApiService.getRetrofit().getKotaKab("")
            .enqueue(object : Callback<ArrayList<KotaKabModel>>{
                override fun onResponse(
                    call: Call<ArrayList<KotaKabModel>>,
                    response: Response<ArrayList<KotaKabModel>>
                ) {
                    arrayKotaKab = arrayListOf()
                    if(response.body()!!.isNotEmpty()){
                        for (value in response.body()!!){
                            arrayKotaKab.add(value.kota_kab)
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<KotaKabModel>>, t: Throwable) {

                }

            })
    }


    // Tambah
    fun dialogTambahData(){
        val viewAlertDialog = View.inflate(this@AdminStasiunActivity, R.layout.alert_dialog_admin_stasiun, null)

        val etNamaStasiun = viewAlertDialog.findViewById<EditText>(R.id.etNamaStasiun)
        val spKotaKab = viewAlertDialog.findViewById<Spinner>(R.id.spKotaKab)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        val alertDialog = AlertDialog.Builder(this@AdminStasiunActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        val arrayadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayKotaKab)
        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spKotaKab.adapter = arrayadapter

        btnSimpan.setOnClickListener {
            dialogInputan.dismiss()
            val namaStasiun = etNamaStasiun.text.toString()
            val kotaKab = spKotaKab.selectedItem.toString()

            loading.alertDialogLoading()
            postTambahData(namaStasiun, kotaKab)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postTambahData(namaStasiun: String, kotaKab: String){

        ApiService.getRetrofit().postAdminTambahStasiun("", namaStasiun, kotaKab)
            .enqueue(object: Callback<StasiunModel>{
                override fun onResponse(call: Call<StasiunModel>, response: Response<StasiunModel>) {
                    Toast.makeText(this@AdminStasiunActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                    getData()
                }

                override fun onFailure(call: Call<StasiunModel>, t: Throwable) {
                    Toast.makeText(this@AdminStasiunActivity, "Gagal Tambah", Toast.LENGTH_SHORT).show()
                    getData()
                }

            })
    }

    // Update
    fun dialogUpdateData(stasiun: StasiunModel){
        val viewAlertDialog = View.inflate(this@AdminStasiunActivity, R.layout.alert_dialog_admin_stasiun, null)

        val etNamaStasiun = viewAlertDialog.findViewById<EditText>(R.id.etNamaStasiun)
        val spKotaKab = viewAlertDialog.findViewById<Spinner>(R.id.spKotaKab)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        val alertDialog = AlertDialog.Builder(this@AdminStasiunActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        etNamaStasiun.setText(stasiun.nama_stasiun)

        val arrayadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayKotaKab)
        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spKotaKab.adapter = arrayadapter

        var cekNomor = 0
        for(value in arrayKotaKab){
            if(value.trim() == stasiun.kota_kab.trim()){
                spKotaKab.setSelection(cekNomor)
            }

            cekNomor++
        }

        btnSimpan.setOnClickListener {
            dialogInputan.dismiss()
            val namaStasiun = etNamaStasiun.text.toString()
            val kotaKab = spKotaKab.selectedItem.toString()

            loading.alertDialogLoading()
            postUpdateData(stasiun.id_stasiun, namaStasiun, kotaKab)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postUpdateData(id_stasiun: Int, namaStasiun: String, kotaKab: String){
        ApiService.getRetrofit().postAdminUpdateStasiun("", id_stasiun, namaStasiun, kotaKab)
            .enqueue(object: Callback<StasiunModel>{
                override fun onResponse(call: Call<StasiunModel>, response: Response<StasiunModel>) {
                    Toast.makeText(this@AdminStasiunActivity, "Berhasil Update Data", Toast.LENGTH_SHORT).show()
                    getData()
                }

                override fun onFailure(call: Call<StasiunModel>, t: Throwable) {
                    Toast.makeText(this@AdminStasiunActivity, "Gagal Update Data", Toast.LENGTH_SHORT).show()
                    getData()
                }

            })
    }


    // Update
    @SuppressLint("SetTextI18n")
    fun dialogHapusData(stasiun: StasiunModel){
        val viewAlertDialog = View.inflate(this@AdminStasiunActivity, R.layout.alert_dialog_admin_hapus, null)

        val tvHapus = viewAlertDialog.findViewById<TextView>(R.id.tvHapus)

        val btnHapus = viewAlertDialog.findViewById<Button>(R.id.btnHapus)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        val alertDialog = AlertDialog.Builder(this@AdminStasiunActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tvHapus.text = "Hapus \nStasiun: ${stasiun.nama_stasiun}. \nKota/Kab: ${stasiun.kota_kab}"

        btnHapus.setOnClickListener {
            dialogInputan.dismiss()
            loading.alertDialogLoading()
            postHapusData(stasiun.id_stasiun)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postHapusData(id_stasiun: Int){

        ApiService.getRetrofit().postAdminHapusStasiun("", id_stasiun)
            .enqueue(object: Callback<StasiunModel>{
                override fun onResponse(call: Call<StasiunModel>, response: Response<StasiunModel>) {
                    Toast.makeText(this@AdminStasiunActivity, "Berhasil Hapus Daata", Toast.LENGTH_SHORT).show()
                    getData()
                }

                override fun onFailure(call: Call<StasiunModel>, t: Throwable) {
                    Toast.makeText(this@AdminStasiunActivity, "Gagal Hapus Daata", Toast.LENGTH_SHORT).show()
                    getData()
                }

            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@AdminStasiunActivity, AdminHomeActivity::class.java))
        finish()
        super.onBackPressed()
    }
}