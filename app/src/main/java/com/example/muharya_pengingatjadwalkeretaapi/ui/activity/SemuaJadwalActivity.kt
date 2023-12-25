package com.example.muharya_pengingatjadwalkeretaapi.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupMenu.OnMenuItemClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.adapter.TiketAdapter
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.MessageNotifPostModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PesananModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.TiketModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivitySemuaJadwalBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.example.muharya_pengingatjadwalkeretaapi.utils.TanggalDanWaktu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.TimeZone

class SemuaJadwalActivity : Activity() {
    lateinit var binding: ActivitySemuaJadwalBinding
    lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var sharedPref : SharedPreferencesLogin
    lateinit var adapter: TiketAdapter
    lateinit var arrayTiket: ArrayList<TiketModel>
    lateinit var loading: LoadingAlertDialog
    lateinit var tanggalDanWaktu: TanggalDanWaktu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySemuaJadwalBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        loading = LoadingAlertDialog(this@SemuaJadwalActivity)
        kontrolNavigationDrawer = KontrolNavigationDrawer(this@SemuaJadwalActivity)
        sharedPref = SharedPreferencesLogin(this@SemuaJadwalActivity)
        tanggalDanWaktu = TanggalDanWaktu()

        getDataTiket()
        loading.alertDialogLoading()

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@SemuaJadwalActivity)

            btnSemua.setOnClickListener {
                getTiket(btnSemua)
            }
            btnSenin.setOnClickListener {
                getTiket(btnSenin)
            }
            btnSelasa.setOnClickListener {
                getTiket(btnSelasa)
            }
            btnRabu.setOnClickListener {
                getTiket(btnRabu)
            }
            btnKamis.setOnClickListener {
                getTiket(btnKamis)
            }
            btnJumat.setOnClickListener {
                getTiket(btnJumat)
            }
            btnSabtu.setOnClickListener {
                getTiket(btnSabtu)
            }
            btnMinggu.setOnClickListener {
                getTiket(btnMinggu)
            }
        }
    }

    private fun getDataTiket(){
        ApiService.getRetrofit().getTiket("get_tiket")
            .enqueue(object: Callback<ArrayList<TiketModel>>{
                override fun onResponse(
                    call: Call<ArrayList<TiketModel>>,
                    response: Response<ArrayList<TiketModel>>
                ) {
                    arrayTiket = arrayListOf()
                    if(response.body()!!.isNotEmpty()){
                        arrayTiket = response.body()!!

                        adapter = TiketAdapter(arrayTiket, object:TiketAdapter.onClickMenuListener{
                            override fun onClick(array: TiketModel) {
                                dialogPesanTiket(array)
                            }

                        })

                        binding.rvTiket.layoutManager = LinearLayoutManager(this@SemuaJadwalActivity)
                        binding.rvTiket.adapter = adapter
                    }
                    else{
                        Toast.makeText(this@SemuaJadwalActivity, "Belum Ada Jadwal", Toast.LENGTH_SHORT).show()
                    }
                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<TiketModel>>, t: Throwable) {
                    Toast.makeText(this@SemuaJadwalActivity, "Gagal Memuat: ${t.message}", Toast.LENGTH_SHORT).show()
                    Log.d("SemuaJadwalActivityTAG", "onFailure: ${t.message}")
                    loading.alertDialogCancel()
                }

            })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun getTiket(btnTiket: TextView){
        if(arrayTiket.isNotEmpty()){

            binding.btnSemua.background = resources.getDrawable(R.drawable.bg_card_tiket)
            binding.btnSenin.background = resources.getDrawable(R.drawable.bg_card_tiket)
            binding.btnSelasa.background = resources.getDrawable(R.drawable.bg_card_tiket)
            binding.btnRabu.background = resources.getDrawable(R.drawable.bg_card_tiket)
            binding.btnKamis.background = resources.getDrawable(R.drawable.bg_card_tiket)
            binding.btnJumat.background = resources.getDrawable(R.drawable.bg_card_tiket)
            binding.btnSabtu.background = resources.getDrawable(R.drawable.bg_card_tiket)
            binding.btnMinggu.background = resources.getDrawable(R.drawable.bg_card_tiket)

            if(btnTiket.id == R.id.btnSemua){
                val value = getArraySeleksiHari("Semua")
                binding.btnSemua.background = resources.getDrawable(R.drawable.bg_card_tiket_pilih)

                adapter = TiketAdapter(value, object:TiketAdapter.onClickMenuListener{
                    override fun onClick(array: TiketModel) {
                        dialogPesanTiket(array)
                    }
                })

                binding.rvTiket.layoutManager = LinearLayoutManager(this@SemuaJadwalActivity)
                binding.rvTiket.adapter = adapter
                loading.alertDialogCancel()

            }
            else if(btnTiket.id == R.id.btnSenin){
                val value = getArraySeleksiHari("Senin")
                binding.btnSenin.background = resources.getDrawable(R.drawable.bg_card_tiket_pilih)

                adapter = TiketAdapter(value, object:TiketAdapter.onClickMenuListener{
                    override fun onClick(array: TiketModel) {
                        dialogPesanTiket(array)
                    }
                })

                binding.rvTiket.layoutManager = LinearLayoutManager(this@SemuaJadwalActivity)
                binding.rvTiket.adapter = adapter
                loading.alertDialogCancel()
            }
            else if(btnTiket.id == R.id.btnSelasa){
                val value = getArraySeleksiHari("Selasa")
                binding.btnSelasa.background = resources.getDrawable(R.drawable.bg_card_tiket_pilih)

                adapter = TiketAdapter(value, object:TiketAdapter.onClickMenuListener{
                    override fun onClick(array: TiketModel) {
                        dialogPesanTiket(array)
                    }

                })

                binding.rvTiket.layoutManager = LinearLayoutManager(this@SemuaJadwalActivity)
                binding.rvTiket.adapter = adapter
                loading.alertDialogCancel()
            }
            else if(btnTiket.id == R.id.btnRabu){
                val value = getArraySeleksiHari("Rabu")
                binding.btnRabu.background = resources.getDrawable(R.drawable.bg_card_tiket_pilih)

                adapter = TiketAdapter(value, object:TiketAdapter.onClickMenuListener{
                    override fun onClick(array: TiketModel) {
                        dialogPesanTiket(array)
                    }

                })

                binding.rvTiket.layoutManager = LinearLayoutManager(this@SemuaJadwalActivity)
                binding.rvTiket.adapter = adapter
                loading.alertDialogCancel()
            }
            else if(btnTiket.id == R.id.btnKamis){
                val value = getArraySeleksiHari("Kamis")
                binding.btnKamis.background = resources.getDrawable(R.drawable.bg_card_tiket_pilih)

                adapter = TiketAdapter(value, object:TiketAdapter.onClickMenuListener{
                    override fun onClick(array: TiketModel) {
                        dialogPesanTiket(array)
                    }

                })

                binding.rvTiket.layoutManager = LinearLayoutManager(this@SemuaJadwalActivity)
                binding.rvTiket.adapter = adapter
                loading.alertDialogCancel()
            }
            else if(btnTiket.id == R.id.btnJumat){
                val value = getArraySeleksiHari("Jumat")
                binding.btnJumat.background = resources.getDrawable(R.drawable.bg_card_tiket_pilih)

                adapter = TiketAdapter(value, object:TiketAdapter.onClickMenuListener{
                    override fun onClick(array: TiketModel) {
                        dialogPesanTiket(array)
                    }

                })

                binding.rvTiket.layoutManager = LinearLayoutManager(this@SemuaJadwalActivity)
                binding.rvTiket.adapter = adapter
                loading.alertDialogCancel()
            }
            else if(btnTiket.id == R.id.btnSabtu){
                val value = getArraySeleksiHari("Sabtu")
                binding.btnSabtu.background = resources.getDrawable(R.drawable.bg_card_tiket_pilih)

                adapter = TiketAdapter(value, object:TiketAdapter.onClickMenuListener{
                    override fun onClick(array: TiketModel) {
                        dialogPesanTiket(array)
                    }

                })

                binding.rvTiket.layoutManager = LinearLayoutManager(this@SemuaJadwalActivity)
                binding.rvTiket.adapter = adapter
                loading.alertDialogCancel()
            }
            else if(btnTiket.id == R.id.btnMinggu){
                val value = getArraySeleksiHari("Minggu")
                binding.btnMinggu.background = resources.getDrawable(R.drawable.bg_card_tiket_pilih)

                adapter = TiketAdapter(value, object:TiketAdapter.onClickMenuListener{
                    override fun onClick(array: TiketModel) {
                        dialogPesanTiket(array)
                    }

                })

                binding.rvTiket.layoutManager = LinearLayoutManager(this@SemuaJadwalActivity)
                binding.rvTiket.adapter = adapter
                loading.alertDialogCancel()
            }

        }
    }

    fun getArraySeleksiHari(hari: String): ArrayList<TiketModel>{

        var arrayTiketValue = arrayListOf<TiketModel>()
        if(hari=="Semua"){
            arrayTiketValue = arrayTiket
        }
        else{
            for (value in arrayTiket){
                if(value.hari == hari){
                    arrayTiketValue.add(value)
                }
            }
        }

        return arrayTiketValue
    }

    @SuppressLint("SetTextI18n")
    fun dialogPesanTiket(tiketModel: TiketModel){
        val viewAlertDialog = View.inflate(this@SemuaJadwalActivity, R.layout.alert_dialog_pesan_tiket, null)

        val dariKotaKab = tiketModel.dari_kota_kab
        val dariStasiun = tiketModel.dari_stasiun
        val sampaiKotaKab = tiketModel.sampai_kota_kab
        val sampaiStasiun = tiketModel.sampai_stasiun
        val tanggal = tiketModel.tanggal
        val hari = tiketModel.hari
        val waktu = tiketModel.waktu
        val harga = tiketModel.harga

        val etNama = viewAlertDialog.findViewById<TextView>(R.id.etNama)
        val etNomorHp = viewAlertDialog.findViewById<TextView>(R.id.etNomorHp)
        val etBerangkatDari = viewAlertDialog.findViewById<TextView>(R.id.etBerangkatDari)
        val etSampaiKe = viewAlertDialog.findViewById<TextView>(R.id.etSampaiKe)
        val etJumlahTiket = viewAlertDialog.findViewById<TextView>(R.id.etJumlahTiket)
        val etWaktu = viewAlertDialog.findViewById<TextView>(R.id.etWaktu)

        val btnPesan = viewAlertDialog.findViewById<Button>(R.id.btnPesan)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        etNama.text = sharedPref.getNama()
        etNomorHp.text = sharedPref.getNomorHp()
        etBerangkatDari.text = "St: $dariStasiun, Kab: $dariKotaKab"
        etSampaiKe.text = "St: $sampaiStasiun, Kab: $sampaiKotaKab"
        val valueTanggal = tanggalDanWaktu.konversiBulanSingkatan(tanggal)
        etWaktu.text = "$hari, $valueTanggal : $waktu Wita";

        val alertDialog = AlertDialog.Builder(this@SemuaJadwalActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        btnPesan.setOnClickListener {
            loading.alertDialogLoading()
            dialogInputan.dismiss()
            postPesanTiket(sharedPref.getId(), tiketModel.id_tiket, sharedPref.getNama(), sharedPref.getAlamat(),
                            sharedPref.getNomorHp(), dariKotaKab, dariStasiun, sampaiKotaKab,
                            sampaiStasiun, tanggal, waktu, etJumlahTiket.text.toString(), harga)
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun postPesanTiket(id_user:Int, id_tiket:Int, nama: String, alamat: String, nomorHp: String,
                       dariKotaKab: String, dariStasiun: String, sampaiKotaKab: String,
                       sampaiStasiun: String, tanggal: String, waktu:String, jumlahTiket: String, harga: String){

        ApiService.getRetrofit().postPesanTiket("pesan_tiket", id_user, id_tiket,
                                                dariKotaKab, dariStasiun, sampaiKotaKab, sampaiStasiun,
                                                tanggal, waktu, jumlahTiket, harga)
            .enqueue(object:Callback<TiketModel>{
                override fun onResponse(
                    call: Call<TiketModel>,
                    response: Response<TiketModel>
                ) {
                    Toast.makeText(this@SemuaJadwalActivity, "Berhasil", Toast.LENGTH_SHORT).show()
                    getDataTiket()
                }

                override fun onFailure(call: Call<TiketModel>, t: Throwable) {
                    Toast.makeText(this@SemuaJadwalActivity, "Gagal Pesan Tiket", Toast.LENGTH_SHORT).show()
                    Log.d("SemuaJadwalActivityTag", "id_user: $id_user \n" +
                            "id_tiket: $id_tiket \n" +
                            "dariKotaKab: $dariKotaKab \n" +
                            "dariStasiun: $dariStasiun \n" +
                            "sampaiKotaKab: $sampaiKotaKab \n" +
                            "sampaiStasiun: $sampaiStasiun \n" +
                            "waktu: $waktu \n" +
                            "jumlahTiket: $jumlahTiket \n" +
                            "harga: $harga \n" +
                            "")
                    getDataTiket()
                }

            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@SemuaJadwalActivity, MainActivity::class.java))
        finish()
    }

}