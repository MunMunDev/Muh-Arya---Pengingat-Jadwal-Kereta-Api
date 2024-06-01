package com.example.muharya_pengingatjadwalkeretaapi.ui.activity.user

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.adapter.PesananAdapter
import com.example.muharya_pengingatjadwalkeretaapi.adapter.PostinganAdapter
import com.example.muharya_pengingatjadwalkeretaapi.adapter.PostinganKomentarAdapter
import com.example.muharya_pengingatjadwalkeretaapi.data.database.api.ApiService
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PesananModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PostinganKomentarModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.PostinganModel
import com.example.muharya_pengingatjadwalkeretaapi.data.model.ResponseModel
import com.example.muharya_pengingatjadwalkeretaapi.databinding.ActivityMainBinding
import com.example.muharya_pengingatjadwalkeretaapi.databinding.AlertDialogKonfirmasiBinding
import com.example.muharya_pengingatjadwalkeretaapi.databinding.BottomSheetDialogKomentarBinding
import com.example.muharya_pengingatjadwalkeretaapi.utils.AlarmNotificationReceiver
import com.example.muharya_pengingatjadwalkeretaapi.utils.KontrolNavigationDrawer
import com.example.muharya_pengingatjadwalkeretaapi.utils.LoadingAlertDialog
import com.example.muharya_pengingatjadwalkeretaapi.utils.SharedPreferencesLogin
import com.example.muharya_pengingatjadwalkeretaapi.utils.TanggalDanWaktu
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
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
    lateinit var adapterPostingan: PostinganAdapter

    // RecyclerView Bottom Sheet Dialog
    private lateinit var rvKomentar: RecyclerView

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
        getPostingan()
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

            btnLihatPostingan.setOnClickListener {
                startActivity(Intent(this@MainActivity, PostinganActivity::class.java))
                finish()
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

                        adapter = PesananAdapter(arrayPesanan, object:PesananAdapter.onClickMenuListener{
                            override fun onClick(listPesanan: PesananModel, it:View) {
                                val popupMenu = PopupMenu(this@MainActivity, it)
                                popupMenu.inflate(R.menu.popup_menu_location)
                                popupMenu.setOnMenuItemClickListener(object :
                                    PopupMenu.OnMenuItemClickListener {
                                    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                                        when (menuItem!!.itemId) {
                                            R.id.showLocation -> {
//                                                setToLocation(listPesanan)
                                                return true
                                            }
                                        }
                                        return true
                                    }

                                })
                                popupMenu.show()
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

//    private fun setToLocation(arrayPesanan: PesananModel) {
//        Log.d("YourAgendaActivityTAG", "setToLocation: ${arrayPesanan.koordinat_stasiun_awal}")
//        if (arrayPesanan.koordinat_stasiun_awal.isNotEmpty()) {
//            Log.d("YourAgendaActivityTAG", "setToLocation 2: ${arrayPesanan.koordinat_stasiun_awal}")
//            LaunchMap.launchMap(this@MainActivity, arrayPesanan.koordinat_stasiun_awal)
//        } else {
//            Toast.makeText(
//                this@MainActivity,
//                "Alamat maps belum ada",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }

    private fun getPostingan(){
        ApiService.getRetrofit().getPostingan("")
            .enqueue(object : Callback<ArrayList<PostinganModel>>{
                override fun onResponse(
                    call: Call<ArrayList<PostinganModel>>,
                    response: Response<ArrayList<PostinganModel>>
                ) {
                    if(response.body()!!.isNotEmpty()){
                        val arrayListPostingan = response.body()!!

                        adapterPostingan = PostinganAdapter(arrayListPostingan, object : PostinganAdapter.onClickPostingan{

//                            override fun onClickLike(
//                                id_user: String,
//                                id_postingan: String,
//                                sudahLike: Boolean,
//                                holder: PostinganAdapter.ViewHolder,
//                                it: View
//                            ) {
//                                postPostinganLike(
//                                    id_postingan,
//                                    sharedPref.getId().toString(),
//                                    sudahLike,
//                                    holder.binding.ivLike
//                                )
//                            }

                            override fun onClickKomentar(
                                id_user: String,
                                id_postingan: String,
                                position:Int,
                                it: View
                            ) {
                                setShowBottomSheetDialog(id_postingan)
//                                showBottomSheetDialog(id_postingan)
                            }

                        }, sharedPref.getId().toString(), 0)

                        binding.apply {
                            rvPostingan.layoutManager = LinearLayoutManager(
                                this@MainActivity, LinearLayoutManager.VERTICAL, false
                            )
                            rvPostingan.adapter = adapterPostingan
                        }


                    } else{
                        Toast.makeText(this@MainActivity, "Tidak Ada Postingan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArrayList<PostinganModel>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun postPostinganLike(idPostingan: String, idUserLike: String, sudahLike: Boolean, ivLike: ImageView){
        loading.alertDialogLoading()
        ApiService.getRetrofit().postPostinganLike("", idPostingan, idUserLike)
            .enqueue(object : Callback<ResponseModel>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    loading.alertDialogCancel()
                    if(sudahLike){
                        ivLike.setImageResource(R.drawable.icon_hati)
                        adapterPostingan.notifyDataSetChanged()
                    } else{
                        ivLike.setImageResource(R.drawable.icon_hati_aktif)
                        adapterPostingan.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    loading.alertDialogCancel()
                    Toast.makeText(this@MainActivity, "Gagal Like: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }


    private fun setShowBottomSheetDialog(idPostingan: String){

        val view = BottomSheetDialogKomentarBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(this@MainActivity)

        dialog.setContentView(view.root)
        dialog.setCancelable(true)
        dialog.show()

        rvKomentar = view.rvKomentar

        view.apply {
            // Full Height
            tvTtile.setOnClickListener {
                val bottomSheetDialog = dialog
                val parentLayout =
                    bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                parentLayout?.let { it ->
                    val behaviour = BottomSheetBehavior.from(it)

                    val layoutParams = it.layoutParams
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
                    it.layoutParams = layoutParams

                    behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                }

                etTulisKomentar.apply {
                    requestFocus()
                    dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),SystemClock.uptimeMillis(),MotionEvent.ACTION_UP,0f,0f,0))
                }
            }
            //Close Komentar
            btnBack.setOnClickListener {
                dialog.dismiss()
            }

            getPostinganKomentar(idPostingan)

            btnSendKomentar.setOnClickListener {
                var cek = true

                if(etTulisKomentar.text.toString().trim().isEmpty()){
                    cek = false
                }

                if(cek){
                    val komentar = etTulisKomentar.text.toString().trim()
                    postPostinganKomentar(
                        idPostingan,
                        "0",    // Jika 0 maka komentar baru
                        sharedPref.getId().toString(),
                        komentar,
                        etTulisKomentar,
                        view.root
                    )
                }
            }
        }
    }

    private fun getPostinganKomentar(idPostingan: String){
        ApiService.getRetrofit().getPostinganKomentar("", idPostingan)
            .enqueue(object : Callback<ArrayList<PostinganKomentarModel>>{
                override fun onResponse(
                    call: Call<ArrayList<PostinganKomentarModel>>,
                    response: Response<ArrayList<PostinganKomentarModel>>
                ) {
                    var arrayListPostingan: ArrayList<PostinganKomentarModel> = arrayListOf()
                    if(response.body()!!.isNotEmpty()){
                        arrayListPostingan = response.body()!!

                        val adapter = PostinganKomentarAdapter(
                            arrayListPostingan, sharedPref.getId().toString(), object : PostinganKomentarAdapter.onClickPostinganKomentar{
                                override fun onClickBalasKomentar(
                                    id_user: String,
                                    id_postingan_komentar: String,
                                    it: View
                                ) {

                                }

                                override fun onClickHapusKomentar(
                                    id_user: String,
                                    id_postingan_komentar: String,
                                    it: View
                                ) {
                                    dialogHapusKomentar(id_postingan_komentar, idPostingan)
                                }
                            })
                        rvKomentar.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
                        rvKomentar.adapter = adapter

                    } else{
                        Toast.makeText(this@MainActivity, "Tidak Ada Komentar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ArrayList<PostinganKomentarModel>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Gagal: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun postPostinganKomentar(
        idPostingan: String, idPostinganKomentarUser: String, idUser: String, komentar: String, etTulisKomentar: EditText, view: View
    ) {
        loading.alertDialogLoading()
        ApiService.getRetrofit().postPostinganKomentarTambah("", idPostingan, idPostinganKomentarUser, idUser, komentar)
            .enqueue(object : Callback<ResponseModel>{
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    loading.alertDialogCancel()
                    getPostinganKomentar(idPostingan)
                    etTulisKomentar.setText("")
                    hideKeyboard(view)
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    loading.alertDialogCancel()
                    Toast.makeText(this@MainActivity, "Gagal : ${t.message}", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun dialogHapusKomentar(idPostinganKomentar: String, idPostingan: String) {
        val view = AlertDialogKonfirmasiBinding.inflate(layoutInflater)
        val alertDialog = AlertDialog.Builder(this@MainActivity)
        alertDialog.setView(view.root)
        val dialogInputan = alertDialog.create()
        dialogInputan.show()

        view.apply {
            btnHapus.setOnClickListener {
                postHapusKomentar(idPostinganKomentar, idPostingan)
                dialogInputan.dismiss()
            }
            btnBatal.setOnClickListener {
                dialogInputan.dismiss()
            }
        }
    }

    private fun postHapusKomentar(id_postingan_komentar: String, idPostingan: String){
        loading.alertDialogLoading()
        ApiService.getRetrofit().postPostinganKomentarHapus("", id_postingan_komentar)
            .enqueue(object: Callback<ResponseModel>{
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    loading.alertDialogCancel()
                    getPostinganKomentar(idPostingan)
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Gagal : ${t.message}", Toast.LENGTH_SHORT).show()
                    loading.alertDialogCancel()
                }

            })
    }


    @SuppressLint("PrivateResource")
    fun showBottomSheetDialog(idPostingan: String){
        val dialogView = layoutInflater.inflate(R.layout.fragment_bottom_sheet, null)
        var btnBack : ImageView = dialogView.findViewById(R.id.btnBack)
        dialog = BottomSheetDialog(this@MainActivity, R.style.BottomShowDialogTheme)
        dialog.setContentView(dialogView)
        dialog.show()

        getPostinganKomentar(idPostingan)
        rvKomentar = dialogView.findViewById(R.id.rvAlarm)

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