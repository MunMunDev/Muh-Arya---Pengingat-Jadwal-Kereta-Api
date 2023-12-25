package com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin;

import android.annotation.SuppressLint
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent

import android.os.Bundle;
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
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
import com.example.muharya_pengingatjadwalkeretaapi.adapter.AdminRuteAdapter
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.RuteModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.StasiunModel

import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityAdminRuteBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class AdminRuteActivity : Activity() {
    val TAG = "AdminRuteActivityTag"
    lateinit var binding: ActivityAdminRuteBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var loading: LoadingAlertDialog
    lateinit var arrayRute: ArrayList<RuteModel>
    lateinit var arrayStasiun: ArrayList<StasiunModel>
    lateinit var arrayKotaKab: ArrayList<String>
    lateinit var adapter : AdminRuteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminRuteBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@AdminRuteActivity)
        loading = LoadingAlertDialog(this@AdminRuteActivity)

        loading.alertDialogLoading()

        getData()
        getDataKotaKabStasiun()

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@AdminRuteActivity)

            btnTambah.setOnClickListener {
                dialogTambahData()
            }

        }

    }

    private fun getData(){
        ApiService.getRetrofit().getRuteAdmin("get_rute")
            .enqueue(object: Callback<ArrayList<RuteModel>>{
                override fun onResponse(
                    call: Call<ArrayList<RuteModel>>,
                    response: Response<ArrayList<RuteModel>>
                ) {
                    if(response.body()!!.isNotEmpty()){
                        arrayRute = response.body()!!

                        adapter = AdminRuteAdapter(arrayRute, object:AdminRuteAdapter.ClickListener{
                            override fun clickItemListener(rute: RuteModel, it: View) {
                                val popupMenu = PopupMenu(this@AdminRuteActivity, it)
                                popupMenu.inflate(R.menu.popup_menu)
                                popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                                        when (menuItem!!.itemId) {
                                            R.id.edit -> {
                                                dialogUpdateData(rute)
                                                return true
                                            }

                                            R.id.hapus -> {
                                                dialogUHapusData(rute)
                                                return true
                                            }
                                        }
                                        return true
                                    }

                                })
                                popupMenu.show()
                            }

                        })

                        binding.rvRute.layoutManager = LinearLayoutManager(this@AdminRuteActivity)
                        binding.rvRute.adapter = adapter

                    }

                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<RuteModel>>, t: Throwable) {
                    Toast.makeText(this@AdminRuteActivity, "Periksa Jaringan Anda", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }
            })
    }

    private fun getDataKotaKabStasiun(){
        ApiService.getRetrofit().getStasiunAdmin("")
            .enqueue(object: Callback<ArrayList<StasiunModel>>{
                override fun onResponse(
                    call: Call<ArrayList<StasiunModel>>,
                    response: Response<ArrayList<StasiunModel>>
                ) {
                    arrayKotaKab = arrayListOf()
                    var cekKotaKab=""
                    if(response.body()!!.isNotEmpty()){
                        arrayStasiun = response.body()!!
                        val data = response.body()!!
                        for(value in data){
                            if(cekKotaKab!=value.kota_kab){
                                arrayKotaKab.add(value.kota_kab)

                                cekKotaKab = value.kota_kab
                            }

                        }

                    }

                }

                override fun onFailure(call: Call<ArrayList<StasiunModel>>, t: Throwable) {

                }

            })
    }

    fun dialogTambahData(){
        val viewAlertDialog = View.inflate(this@AdminRuteActivity, R.layout.alert_dialog_admin_rute,  null)

        val spDariKotaKab = viewAlertDialog.findViewById<Spinner>(R.id.spDariKotaKab)
        val spDariStasiun = viewAlertDialog.findViewById<Spinner>(R.id.spDariStasiun)
        val spSampaiKotaKab = viewAlertDialog.findViewById<Spinner>(R.id.spSampaiKotaKab)
        val spSampaiStasiun = viewAlertDialog.findViewById<Spinner>(R.id.spSampaiStasiun)
        val etHargaTiket = viewAlertDialog.findViewById<EditText>(R.id.etHargaTiket)
        val etJumlahTiket = viewAlertDialog.findViewById<EditText>(R.id.etJumlahTiket)
        val tvWaktu = viewAlertDialog.findViewById<TextView>(R.id.tvWaktu)
        val tvWaktuSampai = viewAlertDialog.findViewById<TextView>(R.id.tvWaktuSampai)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        val arrayadapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayKotaKab)
        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spDariKotaKab.adapter = arrayadapter
        spSampaiKotaKab.adapter = arrayadapter

        spDariKotaKab.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val arrayStasiunValue = arrayListOf<String>()
                for (value in arrayStasiun){
                    if(spDariKotaKab.selectedItem.toString().trim().lowercase() == value.kota_kab.trim().lowercase()){
                        arrayStasiunValue.add(value.nama_stasiun)
                    }
                }

                val arrayAdapterStasiun = ArrayAdapter(this@AdminRuteActivity, android.R.layout.simple_spinner_item, arrayStasiunValue)
                arrayAdapterStasiun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spDariStasiun.adapter = arrayAdapterStasiun
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        spSampaiKotaKab.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val arrayStasiunValue = arrayListOf<String>()
                for (value in arrayStasiun){
                    if(spSampaiKotaKab.selectedItem.toString().trim().lowercase() == value.kota_kab.trim().lowercase()){
                        arrayStasiunValue.add(value.nama_stasiun)
                    }
                }

                val arrayAdapterStasiun = ArrayAdapter(this@AdminRuteActivity, android.R.layout.simple_spinner_item, arrayStasiunValue)
                arrayAdapterStasiun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spSampaiStasiun.adapter = arrayAdapterStasiun
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        val alertDialog = AlertDialog.Builder(this@AdminRuteActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tvWaktu.setOnClickListener {
//            var valueWaktu = ""
//            val hour = 12
//            val minute = 0
//            val mTimePicker: TimePickerDialog = TimePickerDialog(this@AdminRuteActivity,
//                { timePicker, selectedHour, selectedMinute ->
//                    var menit = selectedMinute.toString()
//                    var jam = selectedHour.toString()
//                    if(jam.length==1){
//                        jam = "0$selectedHour"
//                    }
//                    if(menit.length==1){
//                        menit = "0$selectedMinute"
//                    }
//                    valueWaktu = "$jam:$menit:00"
//
////                    tvWaktuNotifikasi.text = valueWaktu
////                    btnEdit.visibility = View.GONE
////                    btnSimpan.visibility = View.VISIBLE
//
//                    val arrayWaktuEdit = valueWaktu.split(":")
//
//                    tvWaktu.text = valueWaktu
//
//                },
//                hour,
//                minute,
//                true
//            )
//            mTimePicker.setTitle("Select Time")
//            mTimePicker.show()

            selectedTime(tvWaktu)
        }

        tvWaktuSampai.setOnClickListener {
            selectedTime(tvWaktuSampai)
        }

        btnSimpan.setOnClickListener {
            val dariKotaKab = spDariKotaKab.selectedItem.toString()
            val dariStasiun = spDariStasiun.selectedItem.toString()
            val sampaiKotaKab = spSampaiKotaKab.selectedItem.toString()
            val sampaiStasiun = spSampaiStasiun.selectedItem.toString()
            val hargaTiket = etHargaTiket.text.toString()
            val jumlahTiket = etJumlahTiket.text.toString()
            val waktuKeberangkatan = tvWaktu.text.toString()
            val waktuSampai = tvWaktuSampai.text.toString()

            if(hargaTiket.isNotEmpty() && jumlahTiket.isNotEmpty() && waktuKeberangkatan.isNotEmpty() && waktuSampai.isNotEmpty() ){
                val arrayWaktuBerangkat = waktuKeberangkatan.split(":")
                val jamBerangkat = arrayWaktuBerangkat[0].toInt()
                val menitBerangkat = arrayWaktuBerangkat[1].toInt()

                val arrayWaktuSampai = waktuSampai.split(":")
                val jamSampai = arrayWaktuSampai[0].toInt()
                val menitSampai = arrayWaktuSampai[1].toInt()

                // cek tanggal dan waktu
                if(jamSampai > jamBerangkat){
                    // Berhasil
                    postTambahData(dariKotaKab, dariStasiun, sampaiKotaKab, sampaiStasiun, hargaTiket, jumlahTiket, waktuKeberangkatan, waktuSampai)
                    dialogInputan.dismiss()
                }
                else if(jamSampai == jamBerangkat){
                    if(menitSampai > menitBerangkat){
                        // Berhasil
                        postTambahData(dariKotaKab, dariStasiun, sampaiKotaKab, sampaiStasiun, hargaTiket, jumlahTiket, waktuKeberangkatan, waktuSampai)
                        dialogInputan.dismiss()
                    }
                    else{
                        Toast.makeText(this@AdminRuteActivity, "Waktu Sampai Tidak Boleh Lebih Kecil Dari Waktu Berangkat", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this@AdminRuteActivity, "Waktu Sampai Tidak Boleh Lebih Kecil Dari Waktu Berangkat", Toast.LENGTH_SHORT).show()
                }
            }
            else if(hargaTiket.isEmpty()){
                etHargaTiket.error = "Masukkan Harga Tiket"
            }
            else if(jumlahTiket.isEmpty()){
                etJumlahTiket.error = "Masukkan Jumlah Tiket"
            }
            else if(waktuKeberangkatan.isEmpty()){
                tvWaktu.error = "Masukkan Waktu"
            }
            else if(waktuSampai.isEmpty()){
                tvWaktuSampai.error = "Masukkan Waktu"
            }

//            postTambahData()
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postTambahData(dari_kota_kab: String, dari_stasiun: String,
                        sampai_kota_kab: String,sampai_stasiun: String,
                        harga: String,jumlah_tiket: String, waktu:String, waktu_sampai:String){

        ApiService.getRetrofit().postAdminTambahRute("", dari_kota_kab, dari_stasiun, sampai_kota_kab, sampai_stasiun, harga, jumlah_tiket, waktu, waktu_sampai)
            .enqueue(object: Callback<RuteModel>{
                override fun onResponse(call: Call<RuteModel>, response: Response<RuteModel>) {
                    Toast.makeText(this@AdminRuteActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                    getData()
//                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<RuteModel>, t: Throwable) {
                    Toast.makeText(this@AdminRuteActivity, "Gagal Tambah", Toast.LENGTH_SHORT).show()
                    getData()
//                    loading.alertDialogCancel()
                }

            })
    }


    // Edit Data
    fun dialogUpdateData(ruteModel: RuteModel){
        val viewAlertDialog = View.inflate(this@AdminRuteActivity, R.layout.alert_dialog_admin_rute,  null)

        val spDariKotaKab = viewAlertDialog.findViewById<Spinner>(R.id.spDariKotaKab)
        val spDariStasiun = viewAlertDialog.findViewById<Spinner>(R.id.spDariStasiun)
        val spSampaiKotaKab = viewAlertDialog.findViewById<Spinner>(R.id.spSampaiKotaKab)
        val spSampaiStasiun = viewAlertDialog.findViewById<Spinner>(R.id.spSampaiStasiun)
        val etHargaTiket = viewAlertDialog.findViewById<EditText>(R.id.etHargaTiket)
        val etJumlahTiket = viewAlertDialog.findViewById<EditText>(R.id.etJumlahTiket)
        val tvWaktu = viewAlertDialog.findViewById<TextView>(R.id.tvWaktu)
        val tvWaktuSampai = viewAlertDialog.findViewById<TextView>(R.id.tvWaktuSampai)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        etHargaTiket.setText(ruteModel.harga.trim())
        etJumlahTiket.setText(ruteModel.jumlah_tiket.trim())
        tvWaktu.text = ruteModel.waktu.trim()
        tvWaktuSampai.text = ruteModel.waktu_sampai.trim()

        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayKotaKab)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spDariKotaKab.adapter = arrayAdapter
        spSampaiKotaKab.adapter = arrayAdapter

        spDariKotaKab.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val arrayStasiunValue = arrayListOf<String>()
                for (value in arrayStasiun){
                    if(spDariKotaKab.selectedItem.toString().trim().lowercase() == value.kota_kab.trim().lowercase()){
                        arrayStasiunValue.add(value.nama_stasiun)
                    }
                }

                val arrayAdapterStasiun = ArrayAdapter(this@AdminRuteActivity, android.R.layout.simple_spinner_item, arrayStasiunValue)
                arrayAdapterStasiun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spDariStasiun.adapter = arrayAdapterStasiun
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        spSampaiKotaKab.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val arrayStasiunValue = arrayListOf<String>()
                for (value in arrayStasiun){
                    if(spSampaiKotaKab.selectedItem.toString().trim().lowercase() == value.kota_kab.trim().lowercase()){
                        arrayStasiunValue.add(value.nama_stasiun)
                    }
                }

                val arrayAdapterStasiun = ArrayAdapter(this@AdminRuteActivity, android.R.layout.simple_spinner_item, arrayStasiunValue)
                arrayAdapterStasiun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spSampaiStasiun.adapter = arrayAdapterStasiun
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        val alertDialog = AlertDialog.Builder(this@AdminRuteActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tvWaktu.setOnClickListener {
            selectedTime(tvWaktu)
        }

        tvWaktuSampai.setOnClickListener {
            selectedTime(tvWaktuSampai)
        }

        btnSimpan.setOnClickListener {
            val dariKotaKab = spDariKotaKab.selectedItem.toString()
            val dariStasiun = spDariStasiun.selectedItem.toString()
            val sampaiKotaKab = spSampaiKotaKab.selectedItem.toString()
            val sampaiStasiun = spSampaiStasiun.selectedItem.toString()
            val hargaTiket = etHargaTiket.text.toString()
            val jumlahTiket = etJumlahTiket.text.toString()
            val waktuKeberangkatan = tvWaktu.text.toString()
            val waktuSampai = tvWaktuSampai.text.toString()

            if(hargaTiket.isNotEmpty() && jumlahTiket.isNotEmpty() && waktuKeberangkatan.isNotEmpty() && waktuSampai.isNotEmpty() ){
                val arrayWaktuBerangkat = waktuKeberangkatan.split(":")
                val jamBerangkat = arrayWaktuBerangkat[0].toInt()
                val menitBerangkat = arrayWaktuBerangkat[1].toInt()

                val arrayWaktuSampai = waktuSampai.split(":")
                val jamSampai = arrayWaktuSampai[0].toInt()
                val menitSampai = arrayWaktuSampai[1].toInt()

                // cek tanggal dan waktu
                if(jamSampai > jamBerangkat){
                    // Berhasil
                    postUpdateData(ruteModel.id_rute, dariKotaKab, dariStasiun, sampaiKotaKab, sampaiStasiun, hargaTiket, jumlahTiket, waktuKeberangkatan, waktuSampai)
                    dialogInputan.dismiss()
                }
                else if(jamSampai == jamBerangkat){
                    if(menitSampai > menitBerangkat){
                        // Berhasil
                        postUpdateData(ruteModel.id_rute, dariKotaKab, dariStasiun, sampaiKotaKab, sampaiStasiun, hargaTiket, jumlahTiket, waktuKeberangkatan, waktuSampai)
                        dialogInputan.dismiss()
                    }
                    else{
                        Toast.makeText(this@AdminRuteActivity, "Waktu Sampai Tidak Boleh Lebih Kecil Dari Waktu Berangkat", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(this@AdminRuteActivity, "Waktu Sampai Tidak Boleh Lebih Kecil Dari Waktu Berangkat", Toast.LENGTH_SHORT).show()
                }
            }
            else if(hargaTiket.isEmpty()){
                etHargaTiket.error = "Masukkan Harga Tiket"
            }
            else if(jumlahTiket.isEmpty()){
                etJumlahTiket.error = "Masukkan Jumlah Tiket"
            }
            else if(waktuKeberangkatan.isEmpty()){
                tvWaktu.error = "Masukkan Waktu"
            }
            else if(waktuSampai.isEmpty()){
                tvWaktuSampai.error = "Masukkan Waktu"
            }

        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postUpdateData(id_rute: Int, dari_kota_kab: String, dari_stasiun: String,
                       sampai_kota_kab: String,sampai_stasiun: String,
                       harga: String,jumlah_tiket: String, waktu:String, waktu_sampai:String){

        ApiService.getRetrofit().postAdminUpdateRute("", id_rute, dari_kota_kab, dari_stasiun, sampai_kota_kab, sampai_stasiun, harga, jumlah_tiket, waktu, waktu_sampai)
            .enqueue(object: Callback<RuteModel>{
                override fun onResponse(call: Call<RuteModel>, response: Response<RuteModel>) {
                    Toast.makeText(this@AdminRuteActivity, "Berhasil Update Data", Toast.LENGTH_SHORT).show()
                    getData()
                }

                override fun onFailure(call: Call<RuteModel>, t: Throwable) {
                    Toast.makeText(this@AdminRuteActivity, "Gagal Tambah", Toast.LENGTH_SHORT).show()
                    getData()
                }

            })
    }


    // Hapus Data
    @SuppressLint("SetTextI18n")
    fun dialogUHapusData(ruteModel: RuteModel){
        val viewAlertDialog = View.inflate(this@AdminRuteActivity, R.layout.alert_dialog_admin_hapus_rute,  null)

        val tvWaktuBerangkat = viewAlertDialog.findViewById<TextView>(R.id.tvWaktuBerangkat)
        val tvWaktuSampai = viewAlertDialog.findViewById<TextView>(R.id.tvWaktuSampai)
        val tvDari = viewAlertDialog.findViewById<TextView>(R.id.tvDari)
        val tvSampai = viewAlertDialog.findViewById<TextView>(R.id.tvSampai)
        val tvHarga = viewAlertDialog.findViewById<TextView>(R.id.tvHarga)
        val tvJumlahTiket = viewAlertDialog.findViewById<TextView>(R.id.tvJumlahTiket)

        val btnHapus = viewAlertDialog.findViewById<Button>(R.id.btnHapus)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        tvWaktuBerangkat.text = ruteModel.waktu.trim()
        tvWaktuSampai.text = ruteModel.waktu_sampai.trim()
        tvDari.text = "${ruteModel.dari_stasiun.trim()}, ${ruteModel.dari_kota_kab.trim()}"
        tvSampai.text = "${ruteModel.sampai_stasiun.trim()}, ${ruteModel.sampai_kota_kab.trim()}"
        tvHarga.text = ruteModel.harga.trim()
        tvJumlahTiket.text = ruteModel.jumlah_tiket.trim()

        val alertDialog = AlertDialog.Builder(this@AdminRuteActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnHapus.setOnClickListener {
            dialogInputan.dismiss()
            postHapusData(ruteModel.id_rute)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postHapusData(id_rute: Int){

        ApiService.getRetrofit().postAdminHapusRute("", id_rute)
            .enqueue(object: Callback<RuteModel>{
                override fun onResponse(call: Call<RuteModel>, response: Response<RuteModel>) {
                    Toast.makeText(this@AdminRuteActivity, "Berhasil Update Data", Toast.LENGTH_SHORT).show()
                    getData()
                }

                override fun onFailure(call: Call<RuteModel>, t: Throwable) {
                    Toast.makeText(this@AdminRuteActivity, "Gagal Tambah", Toast.LENGTH_SHORT).show()
                    getData()
                }

            })
    }


    fun selectedTime(tv: TextView){
        var valueWaktu = ""
        val hour = 12
        val minute = 0
        val mTimePicker: TimePickerDialog = TimePickerDialog(this@AdminRuteActivity,
            { timePicker, selectedHour, selectedMinute ->
                var menit = selectedMinute.toString()
                var jam = selectedHour.toString()
                if(jam.length==1){
                    jam = "0$selectedHour"
                }
                if(menit.length==1){
                    menit = "0$selectedMinute"
                }
                valueWaktu = "$jam:$menit:00"

//                    tvWaktuNotifikasi.text = valueWaktu
//                    btnEdit.visibility = View.GONE
//                    btnSimpan.visibility = View.VISIBLE

                val arrayWaktuEdit = valueWaktu.split(":")

                tv.text = valueWaktu

            },
            hour,
            minute,
            true
        )
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    override fun onBackPressed() {
        startActivity(Intent(this@AdminRuteActivity, AdminHomeActivity::class.java))
        finish()
        super.onBackPressed()
    }

}