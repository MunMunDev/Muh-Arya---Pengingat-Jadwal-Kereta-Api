package com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin;

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent

import android.os.Bundle;
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.muharya_pengingatjadwalkeretaapi.R;
import com.example.muharya_pengingatjadwalkeretaapi.adapter.AdminPesananAdapter
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PesananModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityAdminPesananBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer
import com.example.muharya_pengingatjadwalkeretaapi.utils.KonversiRupiah
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.example.muharya_pengingatjadwalkeretaapi.utils.TanggalDanWaktu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminPesananActivity : Activity() {
    lateinit var binding: ActivityAdminPesananBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var sharedPref: SharedPreferencesLogin
    lateinit var loading: LoadingAlertDialog
    lateinit var arrayPesanan: ArrayList<PesananModel>
    lateinit var adapter: AdminPesananAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminPesananBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminPesananActivity)
        sharedPref = SharedPreferencesLogin(this@AdminPesananActivity)
        loading = LoadingAlertDialog(this@AdminPesananActivity)
        loading.alertDialogLoading()

        getPesanan()
        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminPesananActivity)

            btnTambah.setOnClickListener {
                Toast.makeText(this@AdminPesananActivity, "tambah", Toast.LENGTH_SHORT).show()

            }

        }

    }

    fun getPesanan(){
        ApiService.getRetrofit().getPesananAdmin("get_pesanan_admin")
            .enqueue(object: Callback<ArrayList<PesananModel>>{
                override fun onResponse(
                    call: Call<ArrayList<PesananModel>>,
                    response: Response<ArrayList<PesananModel>>
                ) {
                    if(response.body()!!.isNotEmpty()){
                        arrayPesanan = response.body()!!

                        adapter = AdminPesananAdapter(arrayPesanan, object : AdminPesananAdapter.OnItemClick{
                            override fun clickItemListener(pesanan: PesananModel, it:View) {
                                val popupMenu = PopupMenu(this@AdminPesananActivity, it)
                                popupMenu.inflate(R.menu.popup_menu_rincian)
                                popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                                        when (menuItem!!.itemId) {
//                                            R.id.edit -> {
//                                                Toast.makeText(this@AdminPesananActivity, "Edit", Toast.LENGTH_SHORT).show()
//                                                dialogUpdatePesanan(pesanan)
//                                                return true
//                                            }

                                            R.id.rincian -> {
                                                Toast.makeText(this@AdminPesananActivity, "Edit", Toast.LENGTH_SHORT).show()
                                                dialogRincianPesanan(pesanan)
                                                return true
                                            }
                                        }
                                        return true
                                    }

                                })
                                popupMenu.show()
                            }

                        })

                        binding.rvPesanan.layoutManager = LinearLayoutManager(this@AdminPesananActivity)
                        binding.rvPesanan.adapter = adapter
                    }

                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<PesananModel>>, t: Throwable) {
                    Toast.makeText(this@AdminPesananActivity, "Periksa Jaringan Anda", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }

            })
    }

    @SuppressLint("SetTextI18n")
    fun dialogRincianPesanan(pesanan: PesananModel){
        val viewAlertDialog = View.inflate(this@AdminPesananActivity, R.layout.alert_dialog_admin_rincian_pesanan,  null)

        val tvWaktuPembelian = viewAlertDialog.findViewById<TextView>(R.id.tvWaktuPembelian)
        val tvTanggalDanWaktuBerangkat = viewAlertDialog.findViewById<TextView>(R.id.tvTanggalDanWaktuBerangkat)
        val tvNama = viewAlertDialog.findViewById<TextView>(R.id.tvNama)
        val tvNomorHp = viewAlertDialog.findViewById<TextView>(R.id.tvNomorHp)
        val tvDari = viewAlertDialog.findViewById<TextView>(R.id.tvDari)
        val tvSampai = viewAlertDialog.findViewById<TextView>(R.id.tvSampai)
        val tvJumlahTiket = viewAlertDialog.findViewById<TextView>(R.id.tvJumlahTiket)
        val tvHargaSatuan = viewAlertDialog.findViewById<TextView>(R.id.tvHargaSatuan)
        val tvTotalHarga = viewAlertDialog.findViewById<TextView>(R.id.tvTotalHarga)

        val btnClose = viewAlertDialog.findViewById<Button>(R.id.btnClose)

        val arrayPembelian = pesanan.waktu_pembelian.split(" ")
        val tanggalPembelian = TanggalDanWaktu().konversiBulanSingkatan(arrayPembelian[0])
        val arrayWaktuPembelian = arrayPembelian[1].split(":")
        val tanggalDanWaktuPembelian = "$tanggalPembelian ${arrayWaktuPembelian[0]}:${ arrayWaktuPembelian[1]} WITA"

        val arrayWaktuBerangkat = pesanan.waktu.split(":")
        val tanggalBerangkat = TanggalDanWaktu().konversiBulanSingkatan(pesanan.tanggal)
        val waktuBerangkat = "${arrayWaktuBerangkat[0]}:${arrayWaktuBerangkat[1]} WITA"
        val tanggalDanWaktuBerangkat = "$tanggalBerangkat $waktuBerangkat"

        tvWaktuPembelian.text = tanggalDanWaktuPembelian
        tvTanggalDanWaktuBerangkat.text = tanggalDanWaktuBerangkat
        tvNama.text = pesanan.nama
        tvNomorHp.text = pesanan.nomor_hp
        tvDari.text = "${pesanan.dari_stasiun}, ${pesanan.dari_kota_kab}"
        tvSampai.text = "${pesanan.sampai_stasiun}, ${pesanan.sampai_kota_kab}"
        tvJumlahTiket.text = pesanan.jumlah_tiket
        tvHargaSatuan.text = KonversiRupiah().rupiah(pesanan.harga_satuan.toLong())
        tvTotalHarga.text = KonversiRupiah().rupiah(pesanan.total_harga.toLong())

        val alertDialog = AlertDialog.Builder(this@AdminPesananActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnClose.setOnClickListener {
            dialogInputan.dismiss()
        }
    }


//    fun dialogUpdatePesanan(pesanan: PesananModel){
//        val viewAlertDialog = View.inflate(this@AdminPesananActivity, R.layout.alert_dialog_admin_pesanan,  null)
//
//        val etEditNama = viewAlertDialog.findViewById<TextView>(R.id.etEditNama)
//        val etEditAlamat = viewAlertDialog.findViewById<TextView>(R.id.etEditAlamat)
//        val etEditNomorHp = viewAlertDialog.findViewById<TextView>(R.id.etEditNomorHp)
//        val etEditUsername = viewAlertDialog.findViewById<TextView>(R.id.etEditUsername)
//        val etEditPassword = viewAlertDialog.findViewById<TextView>(R.id.etEditPassword)
//
//        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
//        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)
//
//        etEditNama.text = nama
//        etEditAlamat.text = alamat
//        etEditNomorHp.text = nomor_hp.toString()
//        etEditUsername.text = username
//        etEditPassword.text = password
//
//        val alertDialog = AlertDialog.Builder(this@AdminPesananActivity)
//        alertDialog.setView(viewAlertDialog)
//        val dialogInputan = alertDialog.create()
//        dialogInputan.show()
//
//        btnSimpan.setOnClickListener {
//            loading.alertDialogLoading()
//            postUpdateData(dialogInputan, sharedPref.getId(), etEditNama.text.toString(), etEditAlamat.text.toString(), etEditNomorHp.text.toString(), etEditUsername.text.toString(), etEditPassword.text.toString())
//        }
//        btnBatal.setOnClickListener {
//            dialogInputan.dismiss()
//        }
//    }

    fun postUpdatePesanan(dialogInputan: AlertDialog){

    }


    override fun onBackPressed() {
        startActivity(Intent(this@AdminPesananActivity, AdminHomeActivity::class.java))
        finish()
        super.onBackPressed()
    }
}