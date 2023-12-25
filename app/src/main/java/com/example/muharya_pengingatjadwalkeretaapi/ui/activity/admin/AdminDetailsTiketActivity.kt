package com.example.muharya_pengingatjadwalkeretaapi.ui.activity.admin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.muharya_pengingatjadwalkeretaapi.adapter.AdminTiketAdapter
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.StasiunModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.TiketModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityAdminDetailsTiketBinding
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityAdminTiketBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.example.muharya_pengingatjadwalkeretaapi.utils.TanggalDanWaktu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class AdminDetailsTiketActivity : Activity() {

    lateinit var binding : ActivityAdminDetailsTiketBinding
    private lateinit var sharedPref: SharedPreferencesLogin
    private lateinit var loading: LoadingAlertDialog
    private lateinit var arrayTiket : ArrayList<TiketModel>
    private lateinit var adapter: AdminTiketAdapter
    lateinit var arrayKotaKab: ArrayList<String>
    lateinit var arrayStasiun: ArrayList<StasiunModel>
    lateinit var arrayTanggalTiket: ArrayList<String>
    lateinit var tanggalTerakhir: String

    var tanggalData = tanggalSekarangZonaMakassar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDetailsTiketBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        val extras = intent.extras
        if(extras != null){
            tanggalData =  extras.getString("tanggal").toString()
            binding.titleHeader.text = tanggalData
            getData(tanggalData)
        }

        sharedPref = SharedPreferencesLogin(this@AdminDetailsTiketActivity)
        loading = LoadingAlertDialog(this@AdminDetailsTiketActivity)
        loading.alertDialogLoading()

        getDataKotaKab()

        Log.d("AdminTiketActivityTAG", "onCreate: ${ambilHari("2023-10-07")}")

        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
            btnTambah.setOnClickListener {
                dialogTambah()
            }

//            btnTambahPerminggu.setOnClickListener {
//                dialogTambahDataPerminggu()
//            }
        }

    }

    private fun getData(tanggal: String){
        ApiService.getRetrofit().getTiketAdminPertiket("", tanggal)
            .enqueue(object: Callback<ArrayList<TiketModel>> {
                override fun onResponse(
                    call: Call<ArrayList<TiketModel>>,
                    response: Response<ArrayList<TiketModel>>
                ) {
                    arrayTiket = arrayListOf()
                    arrayTanggalTiket = arrayListOf()
                    if(response.body()!!.isNotEmpty()){
                        arrayTiket = response.body()!!

                        var cekTanggal=""
                        for (value in arrayTiket){
                            tanggalTerakhir = value.tanggal
                            if(cekTanggal!=value.tanggal){
                                arrayTanggalTiket.add(value.tanggal)
                                cekTanggal = value.tanggal
                            }
                        }

                        adapter = AdminTiketAdapter(arrayTiket, object: AdminTiketAdapter.ClickListener{
                            override fun onClickItemListener(tiket: TiketModel, it: View) {
                                val popupMenu = PopupMenu(this@AdminDetailsTiketActivity, it)
                                popupMenu.inflate(R.menu.popup_menu)
                                popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                                        when (menuItem!!.itemId) {
                                            R.id.edit -> {
                                                Toast.makeText(this@AdminDetailsTiketActivity, "Edit", Toast.LENGTH_SHORT).show()
//                                                dialogUpdateTiket(tiket)
                                                dialogUpdateData(tiket)
                                                return true
                                            }

                                            R.id.hapus -> {
                                                Toast.makeText(this@AdminDetailsTiketActivity, "Hapus", Toast.LENGTH_SHORT).show()
//                                                dialogHapusTiket(tiket.id_tiket)
                                                dialogUHapusData(tiket)
                                                return true
                                            }
                                        }
                                        return true
                                    }

                                })
                                popupMenu.show()
                            }

                        })

                        binding.rvTiket.layoutManager = LinearLayoutManager(this@AdminDetailsTiketActivity)
                        binding.rvTiket.adapter = adapter
                    }
                    else{
                        Toast.makeText(this@AdminDetailsTiketActivity, "Belum Ada Tiket", Toast.LENGTH_SHORT).show()
                    }
                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<TiketModel>>, t: Throwable) {
                    Toast.makeText(this@AdminDetailsTiketActivity, "Periksa Jaringan Anda", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }

            })
    }

    fun getDataKotaKab(){
        ApiService.getRetrofit().getStasiunAdmin("")
            .enqueue(object : Callback<ArrayList<StasiunModel>> {
                override fun onResponse(
                    call: Call<ArrayList<StasiunModel>>,
                    response: Response<ArrayList<StasiunModel>>
                ) {
                    var cekKotaKab = ""
                    arrayKotaKab = arrayListOf()
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


    // Tambah
    fun dialogTambahDataPerminggu(){
        val viewAlertDialog = View.inflate(this@AdminDetailsTiketActivity, R.layout.alert_dialog_admin_tiket_perminggu, null)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        val alertDialog = AlertDialog.Builder(this@AdminDetailsTiketActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnSimpan.setOnClickListener {
            val hari = ambilHari(tanggalSekarangZonaMakassar())
            val hariTanggalTerakhir = ambilHari(tanggalTerakhir)
            Log.d("TAG", "dialogTambahDataPerminggu: $tanggalTerakhir ")
            dialogInputan.dismiss()
            loading.alertDialogLoading()
            postTambahDataTiketPerminggu()

//            if(hariTanggalTerakhir != "Minggu"){
//                dialogInputan.dismiss()
//                loading.alertDialogLoading()
//                postTambahDataTiketPerminggu()
//            }
//            else{
//                dialogInputan.dismiss()
//                Log.d("TAG", "dialogTambahDataPerminggu 2: $hariTanggalTerakhir ")
//                loading.alertDialogLoading()
//                postTambahDataTiketPerminggu()
////                if(hari == "Minggu"){
////                    dialogInputan.dismiss()
////                    loading.alertDialogLoading()
////                    postTambahDataTiketPerminggu()
////                }
////                else{
////                    Toast.makeText(this@AdminDetailsTiketActivity, "Hanya Bisa Digunakan Pada Hari Minggu", Toast.LENGTH_SHORT).show()
////                }
//            }
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postTambahDataTiketPerminggu(){
        ApiService.getRetrofit().postAdminTambahTiketPermingguStasiun("")
            .enqueue(object: Callback<TiketModel> {
                override fun onResponse(call: Call<TiketModel>, response: Response<TiketModel>) {
                    Toast.makeText(this@AdminDetailsTiketActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                    getData(tanggalData)
                }

                override fun onFailure(call: Call<TiketModel>, t: Throwable) {
                    Toast.makeText(this@AdminDetailsTiketActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                    getData(tanggalData)
                }

            })
    }


    fun dialogTambah(){
        val viewAlertDialog = View.inflate(this@AdminDetailsTiketActivity, R.layout.alert_dialog_admin_tiket,  null)

        val tvTanggal = viewAlertDialog.findViewById<TextView>(R.id.tvTanggal)
        val tvWaktu = viewAlertDialog.findViewById<TextView>(R.id.tvWaktu)
        val spDariKotaKab = viewAlertDialog.findViewById<Spinner>(R.id.spDariKotaKab)
        val spDariStasiun = viewAlertDialog.findViewById<Spinner>(R.id.spDariStasiun)
        val spSampaiKotaKab = viewAlertDialog.findViewById<Spinner>(R.id.spSampaiKotaKab)
        val spSampaiStasiun = viewAlertDialog.findViewById<Spinner>(R.id.spSampaiStasiun)
        val etHargaTiket = viewAlertDialog.findViewById<EditText>(R.id.etHargaTiket)
        val etJumlahTiket = viewAlertDialog.findViewById<EditText>(R.id.etJumlahTiket)

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

                val arrayAdapterStasiun = ArrayAdapter(this@AdminDetailsTiketActivity, android.R.layout.simple_spinner_item, arrayStasiunValue)
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

                val arrayAdapterStasiun = ArrayAdapter(this@AdminDetailsTiketActivity, android.R.layout.simple_spinner_item, arrayStasiunValue)
                arrayAdapterStasiun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spSampaiStasiun.adapter = arrayAdapterStasiun
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        val alertDialog = AlertDialog.Builder(this@AdminDetailsTiketActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tvWaktu.setOnClickListener {
//            var valueWaktu = ""
//            val hour = 12
//            val minute = 0
//            val mTimePicker: TimePickerDialog = TimePickerDialog(this@AdminDetailsTiketActivity,
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

            selectedTime(tvWaktu, waktuSekarangZonaMakassar())
        }

        tvTanggal.setOnClickListener {
            selectedDate(tvTanggal, tanggalSekarangZonaMakassar())
        }

        btnSimpan.setOnClickListener {
            val waktu = tvWaktu.text.toString()
            val tanggal = tvTanggal.text.toString()
            val dariKotaKab = spDariKotaKab.selectedItem.toString()
            val dariStasiun = spDariStasiun.selectedItem.toString()
            val sampaiKotaKab = spSampaiKotaKab.selectedItem.toString()
            val sampaiStasiun = spSampaiStasiun.selectedItem.toString()
            val hargaTiket = etHargaTiket.text.toString()
            val jumlahTiket = etJumlahTiket.text.toString()

            if(hargaTiket.isNotEmpty() && jumlahTiket.isNotEmpty() && waktu.isNotEmpty() && tanggal.isNotEmpty() ){
//                val arrayWaktu = waktu.split(":")
//                val jam = arrayWaktu[0].toInt()
//                val menit = arrayWaktu[1].toInt()
//
//                val arrayWaktuSekarang = waktuSekarangZonaMakassar().split(":")
//                val jamSekarang = arrayWaktuSekarang[0].toInt()
//                val menitSekarang = arrayWaktuSekarang[1].toInt()

//                val arrayTanggal = tanggal.split("-")
//                val tahun = arrayTanggal[0]
//                val bulan = arrayTanggal[0]
//                val tanggal = arrayTanggal[0]
//
//                val arrayTanggalSekarang = tanggalSekarangZonaMakassar().split("-")
//                val tahunSekarang = arrayTanggalSekarang[0]
//                val bulanSekarang = arrayTanggalSekarang[0]
//                val tanggalSekarang = arrayTanggalSekarang[0]

                var cekTanggal = false
                for(value in arrayTanggalTiket){
                    Log.d("AdminTiketActivityTAG", "dialogTambah: $value \ntanggal: $tanggal")
                    if(tanggal==value){
                        cekTanggal = true
                    }
                }

                if(cekTanggal){
                    // Berhasil
                    loading.alertDialogLoading()
                    postTambahData(tanggal, waktu, dariKotaKab, dariStasiun, sampaiKotaKab, sampaiStasiun, hargaTiket, jumlahTiket)
                    dialogInputan.dismiss()
                }
                else{
                    Toast.makeText(this@AdminDetailsTiketActivity, "Tanggal Harus ada pada seminggu ini", Toast.LENGTH_SHORT).show()
                }
            }
            else if(hargaTiket.isEmpty()){
                etHargaTiket.error = "Masukkan Harga Tiket"
            }
            else if(jumlahTiket.isEmpty()){
                etJumlahTiket.error = "Masukkan Jumlah Tiket"
            }
            else if(waktu.isEmpty()){
                tvWaktu.error = "Masukkan Waktu"
            }
            else if(tanggal.isEmpty()){
                tvTanggal.error = "Masukkan Tanggal"
            }

//            postTambahData()
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postTambahData(tanggal:String, waktu:String, dari_kota_kab: String, dari_stasiun: String,
                       sampai_kota_kab: String,sampai_stasiun: String,
                       harga: String,jumlah_tiket: String){

        ApiService.getRetrofit().postAdminTambahTiket("", tanggal, waktu, dari_kota_kab, dari_stasiun, sampai_kota_kab, sampai_stasiun, harga, jumlah_tiket)
            .enqueue(object: Callback<TiketModel> {
                override fun onResponse(call: Call<TiketModel>, response: Response<TiketModel>) {
                    Toast.makeText(this@AdminDetailsTiketActivity, "Berhasil Tambah", Toast.LENGTH_SHORT).show()
                    getData(tanggalData)
//                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<TiketModel>, t: Throwable) {
                    Toast.makeText(this@AdminDetailsTiketActivity, "Gagal Tambah", Toast.LENGTH_SHORT).show()
                    getData(tanggalData)
//                    loading.alertDialogCancel()
                }

            })
    }


    // Edit Data
    fun dialogUpdateData(tiketModel: TiketModel){
        val viewAlertDialog = View.inflate(this@AdminDetailsTiketActivity, R.layout.alert_dialog_admin_tiket,  null)

        val tvTanggal = viewAlertDialog.findViewById<TextView>(R.id.tvTanggal)
        val tvWaktu = viewAlertDialog.findViewById<TextView>(R.id.tvWaktu)
        val spDariKotaKab = viewAlertDialog.findViewById<Spinner>(R.id.spDariKotaKab)
        val spDariStasiun = viewAlertDialog.findViewById<Spinner>(R.id.spDariStasiun)
        val spSampaiKotaKab = viewAlertDialog.findViewById<Spinner>(R.id.spSampaiKotaKab)
        val spSampaiStasiun = viewAlertDialog.findViewById<Spinner>(R.id.spSampaiStasiun)
        val etHargaTiket = viewAlertDialog.findViewById<EditText>(R.id.etHargaTiket)
        val etJumlahTiket = viewAlertDialog.findViewById<EditText>(R.id.etJumlahTiket)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        tvTanggal.text = tiketModel.tanggal.trim()
        tvWaktu.text = tiketModel.waktu.trim()
        etHargaTiket.setText(tiketModel.harga.trim())
        etJumlahTiket.setText(tiketModel.jumlah_tiket.trim())


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

                val arrayAdapterStasiun = ArrayAdapter(this@AdminDetailsTiketActivity, android.R.layout.simple_spinner_item, arrayStasiunValue)
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

                val arrayAdapterStasiun = ArrayAdapter(this@AdminDetailsTiketActivity, android.R.layout.simple_spinner_item, arrayStasiunValue)
                arrayAdapterStasiun.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spSampaiStasiun.adapter = arrayAdapterStasiun
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

//        var cekNomor = 0
//        for(value in arrayStasiun){
//            if(value.nama_stasiun == tiketModel.dari_kota_kab){
//                spDariKotaKab.setSelection(cekNomor)
//                spDariStasiun.setSelection(cekNomor)
//            }
//            if(value.nama_stasiun == tiketModel.sampai_stasiun){
//                spSampaiKotaKab.setSelection(cekNomor)
//                spSampaiStasiun.setSelection(cekNomor)
//            }
//
//            cekNomor++
//        }

        val alertDialog = AlertDialog.Builder(this@AdminDetailsTiketActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        tvTanggal.setOnClickListener {
            selectedDate(tvTanggal, tiketModel.tanggal)
        }
        tvWaktu.setOnClickListener {
            selectedTime(tvWaktu, tiketModel.waktu)
        }

        btnSimpan.setOnClickListener {
            val tanggal = tvTanggal.text.toString()
            val waktu = tvWaktu.text.toString()
            val dariKotaKab = spDariKotaKab.selectedItem.toString()
            val dariStasiun = spDariStasiun.selectedItem.toString()
            val sampaiKotaKab = spSampaiKotaKab.selectedItem.toString()
            val sampaiStasiun = spSampaiStasiun.selectedItem.toString()
            val hargaTiket = etHargaTiket.text.toString()
            val jumlahTiket = etJumlahTiket.text.toString()


            if(hargaTiket.isNotEmpty() && jumlahTiket.isNotEmpty() && waktu.isNotEmpty() && tanggal.isNotEmpty() ){
                var cekTanggal = false
                for(value in arrayTanggalTiket){
                    Log.d("AdminTiketActivityTAG", "dialogTambah: $value \ntanggal: $tanggal")
                    if(tanggal==value){
                        cekTanggal = true
                    }
                }

                if(cekTanggal){
                    // Berhasil
                    loading.alertDialogLoading()
                    postUpdateData(tiketModel.id_tiket, tanggal, waktu, dariKotaKab, dariStasiun, sampaiKotaKab, sampaiStasiun, hargaTiket, jumlahTiket)
                    dialogInputan.dismiss()
                }
                else{
                    Toast.makeText(this@AdminDetailsTiketActivity, "Tanggal Harus ada pada seminggu ini", Toast.LENGTH_SHORT).show()
                }
            }
            else if(hargaTiket.isEmpty()){
                etHargaTiket.error = "Masukkan Harga Tiket"
            }
            else if(jumlahTiket.isEmpty()){
                etJumlahTiket.error = "Masukkan Jumlah Tiket"
            }
            else if(waktu.isEmpty()){
                tvWaktu.error = "Masukkan Waktu"
            }
            else if(tanggal.isEmpty()){
                tvTanggal.error = "Masukkan Tanggal"
            }

        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postUpdateData(id_tiket: Int,  tanggal:String, waktu:String, dari_kota_kab: String, dari_stasiun: String,
                       sampai_kota_kab: String,sampai_stasiun: String,
                       harga: String,jumlah_tiket: String){

        ApiService.getRetrofit().postAdminUpdateTiket("", id_tiket, tanggal, waktu, dari_kota_kab, dari_stasiun, sampai_kota_kab, sampai_stasiun, harga, jumlah_tiket)
            .enqueue(object: Callback<TiketModel> {
                override fun onResponse(call: Call<TiketModel>, response: Response<TiketModel>) {
                    Toast.makeText(this@AdminDetailsTiketActivity, "Berhasil Update Data", Toast.LENGTH_SHORT).show()
                    getData(tanggalData)
                }

                override fun onFailure(call: Call<TiketModel>, t: Throwable) {
                    Toast.makeText(this@AdminDetailsTiketActivity, "Berhasil Update Data", Toast.LENGTH_SHORT).show()
                    getData(tanggalData)
                }

            })
    }


    // Hapus Data
    @SuppressLint("SetTextI18n")
    fun dialogUHapusData(tiketModel: TiketModel){
        val viewAlertDialog = View.inflate(this@AdminDetailsTiketActivity, R.layout.alert_dialog_admin_hapus_tiket,  null)

        val tvTanggal = viewAlertDialog.findViewById<TextView>(R.id.tvTanggal)
        val tvWaktu = viewAlertDialog.findViewById<TextView>(R.id.tvWaktu)
        val tvDari = viewAlertDialog.findViewById<TextView>(R.id.tvDari)
        val tvSampai = viewAlertDialog.findViewById<TextView>(R.id.tvSampai)
        val tvHarga = viewAlertDialog.findViewById<TextView>(R.id.tvHarga)
        val tvJumlahTiket = viewAlertDialog.findViewById<TextView>(R.id.tvJumlahTiket)

        val btnHapus = viewAlertDialog.findViewById<Button>(R.id.btnHapus)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        tvTanggal.text = tiketModel.tanggal.trim()
        tvWaktu.text = "${tiketModel.waktu.trim()} WITA"
        tvDari.text = "${tiketModel.dari_stasiun.trim()}, ${tiketModel.dari_kota_kab.trim()}"
        tvSampai.text = "${tiketModel.sampai_stasiun.trim()}, ${tiketModel.sampai_kota_kab.trim()}"
        tvHarga.text = tiketModel.harga.trim()
        tvJumlahTiket.text = tiketModel.jumlah_tiket.trim()

        val alertDialog = AlertDialog.Builder(this@AdminDetailsTiketActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnHapus.setOnClickListener {
            dialogInputan.dismiss()
            loading.alertDialogLoading()
            postHapusData(tiketModel.id_tiket)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postHapusData(id_tiket: Int){
        ApiService.getRetrofit().postAdminHapusTiket("", id_tiket)
            .enqueue(object: Callback<TiketModel> {
                override fun onResponse(call: Call<TiketModel>, response: Response<TiketModel>) {
                    Toast.makeText(this@AdminDetailsTiketActivity, "Berhasil Hapus Data", Toast.LENGTH_SHORT).show()
                    getData(tanggalData)
                }

                override fun onFailure(call: Call<TiketModel>, t: Throwable) {
                    Toast.makeText(this@AdminDetailsTiketActivity, "Gagal Hapus", Toast.LENGTH_SHORT).show()
                    getData(tanggalData)
                }

            })
    }



    fun selectedTime(tv: TextView, waktu:String){
        var valueWaktu = ""
        var arrayWaktu = waktu.split(":")
//        val hour = 12
//        val minute = 0
        val hour = arrayWaktu[0].toInt()
        val minute = arrayWaktu[1].toInt()
        val mTimePicker: TimePickerDialog = TimePickerDialog(this@AdminDetailsTiketActivity,
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

                tv.text = valueWaktu

            },
            hour,
            minute,
            true
        )
        mTimePicker.setTitle("Pilih Waktu")
        mTimePicker.show()
    }

    fun selectedDate(tv: TextView, tanggal:String){
        var arrayTanggalSekarang = tanggal.split("-")

        val c = Calendar.getInstance()
        val year = arrayTanggalSekarang[0].toInt()
        val month = arrayTanggalSekarang[1].toInt()-1   // Kurang 1, diambil dari array
        val day = arrayTanggalSekarang[2].toInt()


        val mDatePicker = DatePickerDialog(this@AdminDetailsTiketActivity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            var tahun = year.toString()
            var bulan = (monthOfYear+1).toString()
            var tanggal = dayOfMonth.toString()
            if(bulan.length==1){
                bulan = "0$bulan"
            }
            if(tanggal.length==1){
                tanggal = "0$tanggal"
            }

            val tanggalFull = "$tahun-$bulan-$tanggal"
            tv.text = tanggalFull

        }, year, month, day)
        mDatePicker.setTitle("Pilih Tanggal")
        mDatePicker.show()
    }


    @SuppressLint("SimpleDateFormat")
    private fun tanggalSekarangZonaMakassar():String{
        var date = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val makassarZone  = ZoneId.of("Asia/Makassar")
            val makassarTanggal = LocalDate.now(makassarZone)
            val tanggal = makassarTanggal
            date = "$tanggal"
        } else {
            val makassarTimeZone = TimeZone.getTimeZone("Asia/Makassar")
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            dateFormat.timeZone = makassarTimeZone
            val currentDate = Date()
            val makassarDate = dateFormat.format(currentDate)
            date = makassarDate
        }
        return date
    }

    @SuppressLint("SimpleDateFormat")
    private fun waktuSekarangZonaMakassar():String{
        var time = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val makassarZone  = ZoneId.of("Asia/Makassar")
            val makassarTime = LocalTime.now(makassarZone)
            val waktu = makassarTime.toString().split(".")
            time = waktu[0]

        } else {
            val makassarTimeZone = TimeZone.getTimeZone("Asia/Makassar")
            val timeFormat = SimpleDateFormat("HH:mm:ss")
            timeFormat.timeZone = makassarTimeZone
            val currentTime = Date()
            val makassarTime = timeFormat.format(currentTime)
            time = makassarTime
        }
        return time
    }

    private fun ambilHari(tanggal: String): String{
        val daysArray = arrayOf("Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu")

        val dateTimeFormat = "yyyy-MM-dd"
        val formatter = SimpleDateFormat(dateTimeFormat)

        val valueTanggal = formatter.parse(tanggal)

        val calendar = Calendar.getInstance()
        calendar.time = valueTanggal

        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        val value = daysArray[dayOfWeek-1]
        return value
    }


    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}