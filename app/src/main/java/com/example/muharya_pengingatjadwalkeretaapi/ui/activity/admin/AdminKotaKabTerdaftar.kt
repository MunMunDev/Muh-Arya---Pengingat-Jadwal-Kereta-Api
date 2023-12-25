package com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.example.muharya_pengingatjadwalkeretaapi.adapter.AdminKotaKabTerdaftarAdapter
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.KotaKabModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityAdminKotaKabTerdaftarBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminKotaKabTerdaftar : Activity() {
    lateinit var binding: ActivityAdminKotaKabTerdaftarBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var loading: LoadingAlertDialog

    lateinit var arrayKotaKab: ArrayList<KotaKabModel>
    lateinit var adapter: AdminKotaKabTerdaftarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminKotaKabTerdaftarBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminKotaKabTerdaftar)
        loading = LoadingAlertDialog(this@AdminKotaKabTerdaftar)
        loading.alertDialogLoading()

        getData()

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(
                navView,
                drawerLayoutMain,
                ivDrawerView,
                this@AdminKotaKabTerdaftar
            )

            btnTambah.setOnClickListener {
                dialogTambahData()
            }
        }

    }

    private fun getData() {
        ApiService.getRetrofit().getKotaKab("")
            .enqueue(object : Callback<ArrayList<KotaKabModel>> {
                override fun onResponse(
                    call: Call<ArrayList<KotaKabModel>>,
                    response: Response<ArrayList<KotaKabModel>>
                ) {
                    arrayKotaKab = arrayListOf()
                    if (response.body()!!.isNotEmpty()) {
                        arrayKotaKab = response.body()!!

                        adapter = AdminKotaKabTerdaftarAdapter(
                            arrayKotaKab,
                            object : AdminKotaKabTerdaftarAdapter.ClickListener{
                                override fun onClickItem(
                                    kotaKab: KotaKabModel,
                                    position: Int,
                                    it: View
                                ) {
                                    val popupMenu = PopupMenu(this@AdminKotaKabTerdaftar, it)
                                    popupMenu.inflate(R.menu.popup_menu)
                                    popupMenu.setOnMenuItemClickListener(object :
                                        PopupMenu.OnMenuItemClickListener {
                                        override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                                            when (menuItem!!.itemId) {
                                                R.id.edit -> {
                                                    dialogUpdateData(kotaKab)
                                                    return true
                                                }

                                                R.id.hapus -> {
                                                    dialogHapusData(kotaKab)
                                                    return true
                                                }
                                            }
                                            return true
                                        }

                                    })
                                    popupMenu.show()
                                }
                            }
                        )

                        binding.rvKotaKabTerdaftar.layoutManager =
                            LinearLayoutManager(this@AdminKotaKabTerdaftar)
                        binding.rvKotaKabTerdaftar.adapter = adapter

                    }
                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<KotaKabModel>>, t: Throwable) {
                    Toast.makeText(
                        this@AdminKotaKabTerdaftar,
                        "Gagal. Periksa Jaringan Anda",
                        Toast.LENGTH_SHORT
                    ).show()
                    loading.alertDialogCancel()
                }

            })
    }

    // Tambah
    fun dialogTambahData() {
        val viewAlertDialog =
            View.inflate(this@AdminKotaKabTerdaftar, R.layout.alert_dialog_admin_kota_kab, null)

        val etKotaKab = viewAlertDialog.findViewById<EditText>(R.id.etKotaKab)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        val alertDialog = AlertDialog.Builder(this@AdminKotaKabTerdaftar)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            dialogInputan.dismiss()
            val kotaKab = etKotaKab.text.toString()

            postTambahData(kotaKab)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postTambahData(kotaKab: String) {

        ApiService.getRetrofit().postAdminTambahKotaKab("", kotaKab)
            .enqueue(object : Callback<KotaKabModel> {
                override fun onResponse(
                    call: Call<KotaKabModel>,
                    response: Response<KotaKabModel>
                ) {
                    Toast.makeText(this@AdminKotaKabTerdaftar, "Berhasil Tambah", Toast.LENGTH_SHORT)
                        .show()
                    getData()
                }

                override fun onFailure(call: Call<KotaKabModel>, t: Throwable) {
                    Toast.makeText(this@AdminKotaKabTerdaftar, "Gagal Tambah", Toast.LENGTH_SHORT)
                        .show()
                    getData()
                }

            })
    }

    // Update
    fun dialogUpdateData(kota_kab: KotaKabModel) {
        val viewAlertDialog =
            View.inflate(this@AdminKotaKabTerdaftar, R.layout.alert_dialog_admin_kota_kab, null)

        val etKotaKab = viewAlertDialog.findViewById<EditText>(R.id.etKotaKab)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        val alertDialog = AlertDialog.Builder(this@AdminKotaKabTerdaftar)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        etKotaKab.setText(kota_kab.kota_kab)

        btnSimpan.setOnClickListener {
            dialogInputan.dismiss()
            val kotaKab = etKotaKab.text.toString()

            loading.alertDialogLoading()
            postUpdateData(kota_kab.id_kota_kab, kotaKab)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postUpdateData(id_kota_kab: Int, kotaKab: String) {
        ApiService.getRetrofit().postAdminUpdateKotaKab("", id_kota_kab, kotaKab)
            .enqueue(object : Callback<KotaKabModel> {
                override fun onResponse(
                    call: Call<KotaKabModel>,
                    response: Response<KotaKabModel>
                ) {
                    Toast.makeText(
                        this@AdminKotaKabTerdaftar,
                        "Berhasil Update Data",
                        Toast.LENGTH_SHORT
                    ).show()
                    getData()
                }

                override fun onFailure(call: Call<KotaKabModel>, t: Throwable) {
                    Toast.makeText(
                        this@AdminKotaKabTerdaftar,
                        "Gagal Update Data",
                        Toast.LENGTH_SHORT
                    ).show()
                    getData()
                }

            })
    }


    // Update
    @SuppressLint("SetTextI18n")
    fun dialogHapusData(kota_kab: KotaKabModel) {
        val viewAlertDialog =
            View.inflate(this@AdminKotaKabTerdaftar, R.layout.alert_dialog_admin_hapus, null)

        val tvHapus = viewAlertDialog.findViewById<TextView>(R.id.tvHapus)

        val btnHapus = viewAlertDialog.findViewById<Button>(R.id.btnHapus)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        val alertDialog = AlertDialog.Builder(this@AdminKotaKabTerdaftar)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tvHapus.text = "Hapus \nKota/Kab: ${kota_kab.kota_kab}"

        btnHapus.setOnClickListener {
            dialogInputan.dismiss()
            postHapusData(kota_kab.id_kota_kab)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postHapusData(id_kota_kab: Int) {
        ApiService.getRetrofit().postAdminHapusKotaKab("", id_kota_kab)
            .enqueue(object : Callback<KotaKabModel> {
                override fun onResponse(
                    call: Call<KotaKabModel>,
                    response: Response<KotaKabModel>
                ) {
                    Toast.makeText(
                        this@AdminKotaKabTerdaftar,
                        "Berhasil Update Daata",
                        Toast.LENGTH_SHORT
                    ).show()
                    getData()
                }

                override fun onFailure(call: Call<KotaKabModel>, t: Throwable) {
                    Toast.makeText(
                        this@AdminKotaKabTerdaftar,
                        "Gagal Update Daata",
                        Toast.LENGTH_SHORT
                    ).show()
                    getData()
                }

            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@AdminKotaKabTerdaftar, AdminHomeActivity::class.java))
        finish()
        super.onBackPressed()
    }
}