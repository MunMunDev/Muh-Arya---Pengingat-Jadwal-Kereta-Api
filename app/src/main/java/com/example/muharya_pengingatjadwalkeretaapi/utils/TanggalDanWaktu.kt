package com.example.muharya_pengingatjadwalkeretaapi.utils

import android.annotation.SuppressLint
import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date
import java.util.TimeZone

@SuppressLint("SimpleDateFormat")
class TanggalDanWaktu {
    fun konversiBulan(bulan: String): String{
        val arrayBulan = arrayOf(
            "Januari",
            "Februari",
            "Maret",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Agustus",
            "September",
            "Oktober",
            "November",
            "Desember"
        )

        val splitBulan = bulan.split("-")
        val valueBulan = "${splitBulan[2]} ${arrayBulan[(splitBulan[1].toInt()-1)]} ${splitBulan[0]}"

        return valueBulan

    }
    fun konversiBulanSingkatan(bulan: String): String{
        val arrayBulan = arrayOf(
            "Jan",
            "Feb",
            "Maret",
            "April",
            "Mei",
            "Juni",
            "Juli",
            "Agust",
            "Sep",
            "Okt",
            "Nov",
            "Des"
        )

        val splitBulan = bulan.split("-")
        val valueBulan = "${splitBulan[2]} ${arrayBulan[(splitBulan[1].toInt()-1)]} ${splitBulan[0]}"

        return valueBulan
    }

    fun tanggalSekarangZonaMakassar():String{
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

    fun waktuSekarangZonaMakassar():String{
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

}