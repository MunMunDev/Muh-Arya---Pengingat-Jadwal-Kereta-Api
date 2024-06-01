package com.example.muharya_pengingatjadwalkeretaapi.utils

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.muharya_pengingatjadwalkeretaapi.R
import com.example.muharya_pengingatjadwalkeretaapi.ui.activity.user.YourAgendaActivity

class AlarmNotificationReceiver() : BroadcastReceiver() {
    lateinit var tanggalDanWaktu: TanggalDanWaktu
    override fun onReceive(context: Context?, intent: Intent?) {
        val repeatingIntent = Intent(context, YourAgendaActivity::class.java)
//        repeatingIntent.putExtra("klikNotifikasi", "klikNotifikasi")
        repeatingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        tanggalDanWaktu = TanggalDanWaktu()

        val tanggal = tanggalDanWaktu.konversiBulanSingkatan(tanggalDanWaktu.tanggalSekarangZonaMakassar())
        val arrayWaktu = (tanggalDanWaktu.waktuSekarangZonaMakassar()).split(":")
//        val waktu = tanggalDanWaktu.waktuSekarangZonaMakassar()
        val waktu = "${arrayWaktu[0]}:${arrayWaktu[1]}"

        val pendingIntent = PendingIntent.getActivity(context, 0, repeatingIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context!!, "Notification")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Notifikasi: Klik Untuk Melihat jadwal")
            .setContentText("Tanggal: $tanggal \nWaktu: $waktu")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        val notificationManagerCompact = NotificationManagerCompat.from(context)

        if (checkSelfPermission(
                context,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManagerCompact.notify(200, builder.build())

            return
        }
//        notificationManagerCompact.notify(200, builder.build())


//        val i = Intent(context, YourAgendaActivity::class.java)
//        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        val pendingIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_MUTABLE)
//
//        val builder = NotificationCompat.Builder(context!!, "Pengingat Jadwal Kereta Api")
//            .setSmallIcon(R.drawable.logo_kereta2)
//            .setContentTitle("Ini Pengingat Jadwal Kereta Api")
//            .setContentText("Klik Untuk Melihat jadwal")
//            .setAutoCancel(true)
//            .setDefaults(NotificationCompat.DEFAULT_ALL)
//            .setPriority(NotificationCompat.PRIORITY_MAX)
//            .setContentIntent(pendingIntent)
//
//        val notificationManager = NotificationManagerCompat.from(context)
//
//        if (checkSelfPermission(
//                context,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            notificationManager.notify(123, builder.build())
//
//            return
//        }
//        notificationManager.notify(123, builder.build())


    }


}