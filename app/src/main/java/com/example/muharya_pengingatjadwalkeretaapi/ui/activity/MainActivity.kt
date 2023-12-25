package com.example.muharya_pengingatjadwalkeretaapi.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.adapter.PesananAdapter
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PesananModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityMainBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.AlarmNotificationReceiver
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.example.muharya_pengingatjadwalkeretaapi.utils.TanggalDanWaktu
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar

class MainActivity : Activity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var kontrolNavigationDrawer: KontrolNavigationDrawer
    lateinit var sharedPref : SharedPreferencesLogin
    lateinit var loading: LoadingAlertDialog
    lateinit var dialog: BottomSheetDialog
    lateinit var arrayPesanan: ArrayList<PesananModel>
    lateinit var adapter: PesananAdapter
    lateinit var calendar: Calendar
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    lateinit var tanggalDanWaktu: TanggalDanWaktu
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        kontrolNavigationDrawer = KontrolNavigationDrawer(this@MainActivity)
        sharedPref = SharedPreferencesLogin(this@MainActivity)
        loading = LoadingAlertDialog(this@MainActivity)
        tanggalDanWaktu = TanggalDanWaktu()
        arrayPesanan = arrayListOf()

        loading.alertDialogLoading()
        getDataPesanan(sharedPref.getId())
//        cancelAlarm()
//        createNotification()

        binding.apply {
            kontrolNavigationDrawer.cekSebagai(navView)
            kontrolNavigationDrawer.onClickItemNavigationDrawer(navView, drawerLayoutMain, ivDrawerView, this@MainActivity)

            tvNamaUser.text = "Hy, ${sharedPref.getNama()}"

            btnAlarm.setOnClickListener {
                if(tvJumlahNotif.text.toString().trim() == "0"){
                    Toast.makeText(this@MainActivity, "Maaf, Anda Tidak Memiliki Notifikasi Jadwal", Toast.LENGTH_LONG).show()
                }
                else{
                    startActivity(Intent(this@MainActivity, NotifikasiActivity::class.java))
                }
            }

            btnJadwalAnda.setOnClickListener {
                startActivity(Intent(this@MainActivity, YourAgendaActivity::class.java))
                finish()
            }
            btnSemuaJadwal.setOnClickListener {
                startActivity(Intent(this@MainActivity, SemuaJadwalActivity::class.java))
                finish()
            }

            clJadwalAnda.setOnClickListener {
                if(llTiketHariIni.visibility == View.GONE){
                    TransitionManager.beginDelayedTransition(lyJadwalHariIni, AutoTransition())
                    llTiketHariIni.visibility = View.VISIBLE
                    arrowJadwalAnda.setBackgroundResource(R.drawable.icon_arrow_up)
                }
                else if(llTiketHariIni.visibility == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(lyJadwalHariIni, AutoTransition())
                    llTiketHariIni.visibility = View.GONE
                    arrowJadwalAnda.setBackgroundResource(R.drawable.icon_arrow_down)
                }
            }

//            setAlarm.setOnClickListener {
//                setAlarm(2023, 9, 2, 12, 16, 1)
//            }
//            cancelAlarm.setOnClickListener {
//                setAlarm(2023, 9, 2, 12, 17, 2)
//
//            }
        }

    }

    private fun setAlarm(tahun: Int, bulan: Int, hari: Int, jam: Int, menit: Int, id:Int, context: Context){
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmNotificationReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_IMMUTABLE)
        calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = tahun
        calendar[Calendar.MONTH] = (bulan-1)    // bulan -1
        calendar[Calendar.DAY_OF_MONTH] = hari
        calendar[Calendar.HOUR_OF_DAY] = jam
        calendar[Calendar.MINUTE] = menit
        calendar[Calendar.SECOND] = 0

//        calendar = Calendar.getInstance()
//        calendar[Calendar.YEAR] = 2023
//        calendar[Calendar.MONTH] = 8   // bulan -1
//        calendar[Calendar.DAY_OF_MONTH] = 2
//        calendar[Calendar.HOUR_OF_DAY] = 12
//        calendar[Calendar.MINUTE] = 5
//        calendar[Calendar.SECOND] = 0

        Log.d("CekCobaAnda", "setAlarm year: ${calendar[Calendar.YEAR]}")
        Log.d("CekCobaAnda", "setAlarm month: ${calendar[Calendar.MONTH]}")
        Log.d("CekCobaAnda", "setAlarm hari: ${calendar[Calendar.DAY_OF_MONTH]}")
        Log.d("CekCobaAnda", "setAlarm jam: ${calendar[Calendar.HOUR_OF_DAY]}")
        Log.d("CekCobaAnda", "setAlarm menit: ${calendar[Calendar.MINUTE]}")
        Log.d("CekCobaAnda", "setAlarm second: ${calendar[Calendar.SECOND]}")
        Log.d("CekCobaAnda", "setAlarm milis: ${calendar.timeInMillis}")

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            0, pendingIntent
        )

//        Toast.makeText(context, "ALARM SET SUCCESSFULY", Toast.LENGTH_SHORT).show()
    }

    private fun cancelAlarm(){
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this@MainActivity, AlarmNotificationReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this@MainActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
//        Toast.makeText(this@MainActivity, "Cancel Alarm Manager", Toast.LENGTH_SHORT).show()
    }

    private fun createNotification(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name : CharSequence = "Pengingat Jadwal "
            val description = "Jadwal Ini Bro"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("Notification", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun getDataPesanan(id_user: Int){
        ApiService.getRetrofit().getPesanan("", id_user)
            .enqueue(object: retrofit2.Callback<ArrayList<PesananModel>>{
                override fun onResponse(
                    call: Call<ArrayList<PesananModel>>,
                    response: Response<ArrayList<PesananModel>>
                ) {

                    if(response.body()!!.isNotEmpty()){
                        arrayPesanan = response.body()!!

                        cancelAlarm()
                        createNotification()

                        binding.tvJumlahNotif.visibility = View.VISIBLE
//                        binding.tvJumlahNotif.text = arrayPesanan[0].jumlah_notif

                        binding.tvJumlahNotif.text = arrayPesanan[0].jumlah_notif

                        if("08".toInt() > "11".toInt()){
                            Toast.makeText(this@MainActivity, "Lebih besar", Toast.LENGTH_SHORT).show()
                        }

                        arrayPesanan = arrayListOf()
                        for(value in response.body()!!){
                            if(value.id_pesanan!="-"){
                                val tanggalSekarangZonaMakassar = tanggalDanWaktu.tanggalSekarangZonaMakassar()
                                if(value.tanggal == tanggalSekarangZonaMakassar){
                                    arrayPesanan.add(value)
                                }

//                                //Cek Jumlah Notif
//                                val arrayTanggalKeberangkatan = value.tanggal.split("-")
//                                val keberangkatanTahun = arrayTanggalKeberangkatan[0].toInt()
//                                val keberangkatanBulan = arrayTanggalKeberangkatan[1].toInt()
//                                val keberangkatanHari = arrayTanggalKeberangkatan[2].toInt()
//
//                                val arrayTanggalSekarang = (tanggalDanWaktu.tanggalSekarangZonaMakassar()).split("-")
//                                val sekarangTahun = arrayTanggalKeberangkatan[0].toInt()
//                                val sekarangBulan = arrayTanggalKeberangkatan[1].toInt()
//                                val sekarangHari = arrayTanggalKeberangkatan[2].toInt()
//
//                                if(keberangkatanTahun > sekarangTahun){
//                                    //Berhasil
//                                    jumlahNotif++
//                                    Toast.makeText(this@MainActivity, "tahun", Toast.LENGTH_SHORT).show()
//                                }
//                                else if(keberangkatanTahun == sekarangTahun){
//                                    if(keberangkatanBulan > sekarangBulan){
//                                        // Berhasil
//                                        jumlahNotif++
//                                        Toast.makeText(this@MainActivity, "bulan", Toast.LENGTH_SHORT).show()
//                                    }
//                                    else if(keberangkatanBulan == sekarangBulan){
//                                        if(keberangkatanHari > sekarangHari){
//                                            // Berhasil
//                                            jumlahNotif++
//                                            Toast.makeText(this@MainActivity, "hari1", Toast.LENGTH_SHORT).show()
//                                        }
//                                        else if(keberangkatanHari == sekarangHari){
//                                            // Cek Waktu
//
//                                            val arrayWaktuKeberangkatan = value.waktu.split(":")
//                                            val arrayWaktuSekarang = (tanggalDanWaktu.waktuSekarangZonaMakassar()).split(":")
//
//                                            if(arrayWaktuKeberangkatan[0].toInt() > arrayWaktuSekarang[0].toInt()){
//                                                //Berhasil
//                                                jumlahNotif++
//                                                Toast.makeText(this@MainActivity, "jam", Toast.LENGTH_SHORT).show()
//                                            }
//                                            else if(arrayWaktuKeberangkatan[0].toInt() == arrayWaktuSekarang[0].toInt()){
//                                                if(arrayWaktuKeberangkatan[1].toInt() > arrayWaktuSekarang[1].toInt()) {
//                                                    jumlahNotif++
//                                                }
//                                                else if(arrayWaktuKeberangkatan[1].toInt() == arrayWaktuSekarang[1].toInt()){
//                                                    jumlahNotif++
//                                                }
//                                            }
////                                            else if(arrayWaktuKeberangkatan[1].toInt() > arrayWaktuSekarang[1].toInt()){
////                                                if(arrayWaktuKeberangkatan[2].toInt() > arrayWaktuSekarang[2].toInt()){
////                                                    //Berhasil
////                                                    jumlahNotif++
////                                                }
////                                                else{
////                                                    //Gagal
////                                                }
////                                            }
//                                        }
//                                    }
//                                    else{
//                                        //Gagal
//                                    }
//                                }
//                                else{
//                                    //Gagal
//                                }
//
                                val arrayTanggal = value.tanggal.split("-")
                                val arrayWaktu = value.waktu_alarm.split(":")
                                val tahun = arrayTanggal[0].toInt()
                                val bulan = arrayTanggal[1].toInt()
                                val hari = arrayTanggal[2].toInt()
                                val jam = arrayWaktu[0].toInt()
                                val menit = arrayWaktu[1].toInt()
                                val id = value.id_pesanan.toInt()

                                // cek tanggal dan waktu
                                if(tanggalDanWaktu.tanggalSekarangZonaMakassar() == value.tanggal){
                                    if(tanggalDanWaktu.waktuSekarangZonaMakassar() <= value.waktu_alarm){
                                        if(value.status_alarm=="aktif"){
                                            setAlarm(tahun, bulan, hari, jam, menit, id, this@MainActivity)
                                        }
                                    }
                                }
                                if(tanggalDanWaktu.tanggalSekarangZonaMakassar() < value.tanggal){
                                    if(value.status_alarm=="aktif"){
                                        setAlarm(tahun, bulan, hari, jam, menit, id, this@MainActivity)
                                    }
                                }

////                                if(value.status_alarm=="aktif"){
////                                    val arrayTanggal = value.tanggal.split("-")
////                                    val arrayWaktu = value.waktu_alarm.split(":")
////                                    val tahun = arrayTanggal[0].toInt()
////                                    val bulan = arrayTanggal[1].toInt()
////                                    val hari = arrayTanggal[2].toInt()
////                                    val jam = arrayWaktu[0].toInt()
////                                    val menit = arrayWaktu[1].toInt()
////                                    val id = value.id_pesanan.toInt()
//////                                    setAlarm(2023, 9, 2, 12, 17, 2)
////                                    setAlarm(tahun, bulan, hari, jam, menit, id)
////                                }
//
////                                val arrayTanggal = value.tanggal.split("-")
////                                val arrayWaktu = value.waktu_alarm.split(":")
////                                val tahun = arrayTanggal[0].toInt()
////                                val bulan = arrayTanggal[1].toInt()
////                                val hari = arrayTanggal[2].toInt()
////                                val jam = arrayWaktu[0].toInt()
////                                val menit = arrayWaktu[1].toInt()
////                                val id = value.id_pesanan.toInt()
//////                                setAlarm(2023, 9, 2, 12, 17, 2)
////                                setAlarm(tahun, bulan, hari, jam, menit, id)

                            }
                        }

                        if(arrayPesanan.isEmpty()){
                            binding.rvPesanan.visibility = View.GONE
                            binding.tvNoPesanan.visibility = View.VISIBLE
                        }

//                        binding.tvJumlahNotif.text = jumlahNotif.toString()

//                        var valueArray = arrayPesanan
//
//                        if(arrayPesanan.size>2){
//                            arrayPesanan = arrayListOf()
//                            arrayPesanan.add(valueArray[0])
//                            arrayPesanan.add(valueArray[1])
//                        }

                        adapter = PesananAdapter(arrayPesanan, object: PesananAdapter.onClickMenuListener{
                            override fun onClick(array: PesananModel) {

                            }

                        })
                        binding.rvPesanan.layoutManager = LinearLayoutManager(this@MainActivity)
                        binding.rvPesanan.adapter = adapter
                    }
                    else{
                        binding.rvPesanan.visibility = View.GONE
                        binding.tvNoPesanan.visibility = View.VISIBLE
                    }
                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<PesananModel>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                    //onfailure
                    binding.rvPesanan.visibility = View.GONE
                    binding.tvNoPesanan.visibility = View.VISIBLE
                }
            })
    }


    @SuppressLint("PrivateResource")
    fun showBottomSheetDialog(){
        val dialogView = layoutInflater.inflate(R.layout.fragment_bottom_sheet, null)
        var btnBack : ImageView = dialogView.findViewById(R.id.btnBack)
        dialog = BottomSheetDialog(this@MainActivity, R.style.BottomShowDialogTheme)
        dialog.setContentView(dialogView)
        dialog.show()

        btnBack.setOnClickListener {
            Toast.makeText(this@MainActivity, "Button back", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }


    var tapDuaKali = false
    override fun onBackPressed() {
        if (tapDuaKali){
            super.onBackPressed()
        }
        tapDuaKali = true
        Toast.makeText(this@MainActivity, "Tekan Sekali Lagi untuk keluar", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            tapDuaKali = false
        }, 2000)

    }
}