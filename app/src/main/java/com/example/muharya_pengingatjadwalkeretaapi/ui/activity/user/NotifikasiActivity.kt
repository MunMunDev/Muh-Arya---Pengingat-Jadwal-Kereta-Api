package com.example.muharya_pengingatjadwalkeretaapi.ui.activity.user

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.adapter.NotifikasiAdapter
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PesananModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityNotifikasiBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.AlarmNotificationReceiver
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.example.muharya_pengingatjadwalkeretaapi.utils.TanggalDanWaktu
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class NotifikasiActivity : Activity() {
    lateinit var binding: ActivityNotifikasiBinding
    lateinit var sharedPref : SharedPreferencesLogin
    lateinit var arrayNotifikasi : ArrayList<PesananModel>
    lateinit var adapter : NotifikasiAdapter
    lateinit var loading: LoadingAlertDialog
    lateinit var calendar: Calendar
    lateinit var alarmManager: AlarmManager
    lateinit var pendingIntent: PendingIntent
    lateinit var tanggalDanWaktu: TanggalDanWaktu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifikasiBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        sharedPref = SharedPreferencesLogin(this@NotifikasiActivity)
        loading = LoadingAlertDialog(this@NotifikasiActivity)
        loading.alertDialogLoading()
        tanggalDanWaktu = TanggalDanWaktu()

        binding.apply {
            getDataNotifikasi(sharedPref.getId())

            btnBack.setOnClickListener {
                finish()
            }

        }
    }

    private fun getDataNotifikasi(id_user: Int){
        ApiService.getRetrofit().getPesanan("", id_user)
            .enqueue(object: Callback<ArrayList<PesananModel>>{
                override fun onResponse(
                    call: Call<ArrayList<PesananModel>>,
                    response: Response<ArrayList<PesananModel>>
                ) {
                    if(response.body()!!.isNotEmpty()){
                        arrayNotifikasi = arrayListOf()

                        // Cek SetNotifikasi
                        var cekHeaderTanggal = ""
                        for(value in response.body()!!){
                            if(value.id_pesanan == "-"){
                                if(cekHeaderTanggal != "-"){
                                    arrayNotifikasi.add(value)
                                }
                                cekHeaderTanggal = value.id_pesanan
                            }
                            else if(value.id_pesanan != "-"){
                                val tanggalSekarangZonaMakassar = tanggalDanWaktu.tanggalSekarangZonaMakassar()

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
                                            setAlarm(tahun, bulan, hari, jam, menit, id)
                                        }
                                    }
                                }
                                if(tanggalDanWaktu.tanggalSekarangZonaMakassar() < value.tanggal){
                                    if(value.status_alarm=="aktif"){
                                        setAlarm(tahun, bulan, hari, jam, menit, id)
                                    }
                                }

                                arrayNotifikasi.add(value)
                                cekHeaderTanggal = value.id_pesanan


//                                //Jumlah Notifikasi
//                                val arrayTanggalKeberangkatan = value.tanggal.split("-")
//                                val arrayTanggalSekarang = (tanggalDanWaktu.tanggalSekarangZonaMakassar()).split("-")
//
//                                if(arrayTanggalKeberangkatan[0] > arrayTanggalSekarang[0]){
//                                    //Berhasil
//                                    arrayNotifikasi.add(value)
//                                    cekHeaderTanggal = value.id_pesanan
//                                }
//                                else if(arrayTanggalKeberangkatan[0].toInt() == arrayTanggalSekarang[0].toInt()){
//                                    if(arrayTanggalKeberangkatan[1].toInt() > arrayTanggalSekarang[1].toInt()){
//                                        // Berhasil
//                                        arrayNotifikasi.add(value)
//                                        cekHeaderTanggal = value.id_pesanan
//                                    }
//                                    else if(arrayTanggalKeberangkatan[1].toInt() == arrayTanggalSekarang[1].toInt()){
//                                        if(arrayTanggalKeberangkatan[2].toInt() > arrayTanggalSekarang[2].toInt()){
//                                            // Berhasil
//                                            arrayNotifikasi.add(value)
//                                            cekHeaderTanggal = value.id_pesanan
//                                        }
//                                        else if(arrayTanggalKeberangkatan[2].toInt() == arrayTanggalSekarang[2].toInt()){
//                                            // Cek Waktu
//
//                                            val arrayWaktuKeberangkatan = value.waktu.split(":")
//                                            val arrayWaktuSekarang = (tanggalDanWaktu.waktuSekarangZonaMakassar()).split(":")
//
//                                            if(arrayWaktuKeberangkatan[0].toInt() > arrayWaktuSekarang[0].toInt()){
//                                                //Berhasil
//                                                arrayNotifikasi.add(value)
//                                                cekHeaderTanggal = value.id_pesanan
//                                            }
//                                            else if(arrayWaktuKeberangkatan[1].toInt() > arrayWaktuSekarang[1].toInt()){
//                                                if(arrayWaktuKeberangkatan[2].toInt() > arrayWaktuSekarang[2].toInt()){
//                                                    //Berhasil
//                                                    arrayNotifikasi.add(value)
//                                                    cekHeaderTanggal = value.id_pesanan
//                                                }
//                                                else{
//                                                    //Gagal
//                                                }
//                                            }
//                                        }
//                                    }
//                                    else{
//                                        //Gagal
//                                    }
//                                }
//                                else{
//                                    //Gagal
//                                }
                            }
                        }

                        cancelAlarm()
                        createNotification()

                        adapter = NotifikasiAdapter(this@NotifikasiActivity , arrayNotifikasi, object: NotifikasiAdapter.OnClickItemListener{
                            override fun OnClick(arrayList: PesananModel) {
                                try {
//                                Toast.makeText(this@NotifikasiActivity, "${arrayList.jumlah_notif}", Toast.LENGTH_SHORT).show()
//                                Toast.makeText(this@NotifikasiActivity, "${arrayList.dari_kota}, ${arrayList.dari_stasiun}, ${arrayList.sampai_kota}, ${arrayList.sampai_stasiun}, ${arrayList.waktu}, ${arrayList.waktu_alarm}", Toast.LENGTH_SHORT).show()
                                    dialogRincian(arrayList)
                                }catch (ex: Exception){
                                    Toast.makeText(this@NotifikasiActivity, "${ex.message}", Toast.LENGTH_SHORT).show()
                                    Log.d("NotifikasiActivityTag", "OnClick: ${ex.message}")
                                }
                                Toast.makeText(this@NotifikasiActivity, "Klik Rincian", Toast.LENGTH_SHORT).show()
                            }

                            override fun OnSettingClick(arrayList: PesananModel, it: View) {
                                val popupMenu = PopupMenu(this@NotifikasiActivity, it)
                                popupMenu.inflate(R.menu.popup_menu_notifikasi)
                                popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                                        when (menuItem!!.itemId) {
                                            R.id.status -> {
                                                Toast.makeText(this@NotifikasiActivity, "Status", Toast.LENGTH_SHORT).show()
                                                return true
                                            }
                                            R.id.edit -> {
                                                Toast.makeText(this@NotifikasiActivity, "Edit", Toast.LENGTH_SHORT).show()
                                                dialogEditNotifikasi(arrayList.id_pesanan, arrayList.waktu, arrayList.waktu_alarm)
                                                return true
                                            }
                                            R.id.rincian -> {
                                                dialogRincian( arrayList)
                                                return true
                                            }
                                        }
                                        return true
                                    }

                                })
                                popupMenu.show()
                            }
                        })
                        binding.rvAlarm.layoutManager = LinearLayoutManager(this@NotifikasiActivity)
                        binding.rvAlarm.adapter = adapter
                    }

                    loading.alertDialogCancel()
                }

                override fun onFailure(call: Call<ArrayList<PesananModel>>, t: Throwable) {
                    Toast.makeText(this@NotifikasiActivity, "Gagal ${t.message}", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }

            })
    }

    private fun dialogRincian(arrayPesanan:PesananModel){
        val viewAlertDialog = View.inflate(this@NotifikasiActivity, R.layout.alert_dialog_rincian_notifikasi, null)

        val tvDariKota = viewAlertDialog.findViewById<TextView>(R.id.tvDariKota)
        val tvDariStasiun = viewAlertDialog.findViewById<TextView>(R.id.tvDariStasiun)
        val tvKeKota = viewAlertDialog.findViewById<TextView>(R.id.tvKeKota)
        val tvKeStasiun = viewAlertDialog.findViewById<TextView>(R.id.tvKeStasiun)
        val tvTanggalBerangkat = viewAlertDialog.findViewById<TextView>(R.id.tvTanggalBerangkat)
        val tvWaktuBerangkat = viewAlertDialog.findViewById<TextView>(R.id.tvWaktuBerangkat)
        val tvWaktuNotifikasi = viewAlertDialog.findViewById<TextView>(R.id.tvWaktuNotifikasi)

        val btnEdit = viewAlertDialog.findViewById<Button>(R.id.btnEdit)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        tvDariKota.text = arrayPesanan.dari_kota_kab
        tvDariStasiun.text = arrayPesanan.dari_stasiun
        tvKeKota.text = arrayPesanan.sampai_kota_kab
        tvKeStasiun.text = arrayPesanan.sampai_stasiun
        tvWaktuBerangkat.text = tanggalDanWaktu.konversiBulan(arrayPesanan.tanggal)
        val arrayWaktuKeberangkatan = arrayPesanan.waktu.split(":")
        tvWaktuBerangkat.text = "${arrayWaktuKeberangkatan[0]}:${arrayWaktuKeberangkatan[1]} Wita"
        val arrayWaktuNotifikasi = arrayPesanan.waktu_alarm.split(":")
        tvWaktuNotifikasi.text = "${arrayWaktuNotifikasi[0]}:${arrayWaktuNotifikasi[1]} Wita"
//        tvWaktuBerangkat.text = waktuBerangkat
//        tvWaktuNotifikasi.text = waktuNotifikasi

        val alertDialog = AlertDialog.Builder(this@NotifikasiActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogRincian = alertDialog.create()
        dialogRincian.show()

        btnEdit.setOnClickListener {
            dialogRincian.dismiss()
            dialogEditNotifikasi(arrayPesanan.id_pesanan, arrayPesanan.waktu, arrayPesanan.waktu_alarm)
        }
        btnBatal.setOnClickListener {
            dialogRincian.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    fun dialogEditNotifikasi(id_pesanan: String, waktuKeberangkatan: String, waktuNotifikasi: String){
        val viewAlertDialog = View.inflate(this@NotifikasiActivity, R.layout.alert_dialog_update_notifikasi, null)

        val tvWaktuKeberangkatan = viewAlertDialog.findViewById<TextView>(R.id.tvWaktuKeberangkatan)
        val tvWaktuNotifikasi = viewAlertDialog.findViewById<TextView>(R.id.tvWaktuNotifikasi)

        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
        val btnEdit = viewAlertDialog.findViewById<Button>(R.id.btnEdit)
        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)

        val alertDialog = AlertDialog.Builder(this@NotifikasiActivity)
        alertDialog.setView(viewAlertDialog)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        val arrayWaktuKeberangkatan = waktuKeberangkatan.split(":")
        val arrayWaktuNotifikasi = waktuNotifikasi.split(":")

        tvWaktuKeberangkatan.text = "${arrayWaktuKeberangkatan[0]}:${arrayWaktuKeberangkatan[1]}"
        tvWaktuNotifikasi.text = "${arrayWaktuNotifikasi[0]}:${arrayWaktuNotifikasi[1]}"

        btnSimpan.setOnClickListener {
            Toast.makeText(this@NotifikasiActivity, "Simpan", Toast.LENGTH_SHORT).show()

            ApiService.getRetrofit().postUpdateWaktuNotifikasi("", id_pesanan, tvWaktuNotifikasi.text.toString())
                .enqueue(object:Callback<PesananModel>{
                    override fun onResponse(
                        call: Call<PesananModel>,
                        response: Response<PesananModel>
                    ) {
                        Toast.makeText(this@NotifikasiActivity, "Berhasil Update Waktu Notifikasi", Toast.LENGTH_SHORT).show()
                        getDataNotifikasi(sharedPref.getId())
                        dialogInputan.dismiss()
                    }

                    override fun onFailure(call: Call<PesananModel>, t: Throwable) {
                        Toast.makeText(this@NotifikasiActivity, "Gagal Update Notifikasi", Toast.LENGTH_SHORT).show()
                        Log.d("NotifikasiActivityTag", "onFailure: ${t.message}")
                        getDataNotifikasi(sharedPref.getId())
                        dialogInputan.dismiss()
                    }
                })
        }
        btnEdit.setOnClickListener {
//            TimePickerDialog(this@NotifikasiActivity, this, 10, 10, true).show()
            val arrayWaktuNotifikasi = waktuNotifikasi.split(":")
            val arrayWaktuKeberangkatan = waktuKeberangkatan.split(":")
//            editNotifikasi(arrayWaktuNotifikasi[0].toInt(), arrayWaktuNotifikasi[1].toInt())

            var valueWaktu = ""
            val c = Calendar.getInstance()
//        val hour = c.get(Calendar.HOUR_OF_DAY)
//        val minute = c.get(Calendar.MINUTE)
            val hour = arrayWaktuNotifikasi[0].toInt()
            val minute = arrayWaktuNotifikasi[1].toInt()
            val mTimePicker: TimePickerDialog = TimePickerDialog(this@NotifikasiActivity,
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

                    if(arrayWaktuKeberangkatan[0].trim().toInt() < arrayWaktuEdit[0].trim().toInt()){
                        Toast.makeText(this@NotifikasiActivity, "Waktu Notifikasi Tidak Boleh Kurang dari Waktu Keberangkatan", Toast.LENGTH_LONG).show()
                    }
                    else if(arrayWaktuKeberangkatan[0].trim().toInt() == arrayWaktuEdit[0].trim().toInt()){
                        if(arrayWaktuKeberangkatan[1].trim().toInt() < arrayWaktuEdit[1].trim().toInt()){
                            Toast.makeText(this@NotifikasiActivity, "Waktu Notifikasi Tidak Boleh Kurang dari Waktu Keberangkatan", Toast.LENGTH_LONG).show()
                        }
                        else{
                            tvWaktuNotifikasi.text = valueWaktu
                            btnEdit.visibility = View.GONE
                            btnSimpan.visibility = View.VISIBLE
                        }
                    }
                    else{
                        tvWaktuNotifikasi.text = valueWaktu
                        btnEdit.visibility = View.GONE
                        btnSimpan.visibility = View.VISIBLE
                    }

                    Toast.makeText(this@NotifikasiActivity, "$valueWaktu 2", Toast.LENGTH_SHORT).show()
                },
                hour,
                minute,
                true
            )
            mTimePicker.setTitle("Select Time")
            mTimePicker.show()
        }
        btnBatal.setOnClickListener {
            dialogInputan.dismiss()
        }
    }

    fun editNotifikasi(jam:Int, menit:Int){
        var valueWaktu = ""
        val c = Calendar.getInstance()
//        val hour = c.get(Calendar.HOUR_OF_DAY)
//        val minute = c.get(Calendar.MINUTE)
        val hour = jam
        val minute = menit
        val mTimePicker: TimePickerDialog = TimePickerDialog(this@NotifikasiActivity,
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

                Toast.makeText(this@NotifikasiActivity, "$valueWaktu 2", Toast.LENGTH_SHORT).show()
            },
            hour,
            minute,
            true
        )
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()


//        Toast.makeText(this@NotifikasiActivity, "$waktu 4", Toast.LENGTH_SHORT).show()
//
//        val viewAlertDialog = View.inflate(this@NotifikasiActivity, R.layout.alert_dialog_rincian_notifikasi, null)
//
//        val tvDariKota = viewAlertDialog.findViewById<TextView>(R.id.tvDariKota)
//        val tvDariStasiun = viewAlertDialog.findViewById<TextView>(R.id.tvDariStasiun)
//
//        val btnSimpan = viewAlertDialog.findViewById<Button>(R.id.btnSimpan)
//        val btnBatal = viewAlertDialog.findViewById<Button>(R.id.btnBatal)
//
//        val alertDialog = AlertDialog.Builder(this@NotifikasiActivity)
//        alertDialog.setView(viewAlertDialog)
//        val dialogInputan = alertDialog.create()
//        dialogInputan.show()
//
//        btnSimpan.setOnClickListener {
////            postUpdateData(dialogInputan, sharedPref.getId(), tvDariKota.text.toString(), tvDariStasiun.text.toString(), tvKeKota.text.toString(), tvKeStasiun.text.toString())
//        }
//        btnBatal.setOnClickListener {
//            dialogInputan.dismiss()
//        }
    }

    private fun cancelAlarm(){
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this@NotifikasiActivity, AlarmNotificationReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this@NotifikasiActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        alarmManager.cancel(pendingIntent)
//        Toast.makeText(this@MainActivity, "Cancel Alarm Manager", Toast.LENGTH_SHORT).show()
    }

    private fun createNotification(){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name : CharSequence = "Pengingat Jadwal "
            val description = "Jadwal Ini"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("Notification", name, importance)
            channel.description = description
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun setAlarm(tahun: Int, bulan: Int, hari: Int, jam: Int, menit: Int, id:Int){
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this@NotifikasiActivity, AlarmNotificationReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this@NotifikasiActivity, id, intent, PendingIntent.FLAG_IMMUTABLE)
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

        Toast.makeText(this@NotifikasiActivity, "ALARM SET SUCCESSFULY", Toast.LENGTH_SHORT).show()
    }
}