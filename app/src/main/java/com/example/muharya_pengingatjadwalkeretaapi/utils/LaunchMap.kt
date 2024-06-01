package com.example.muharya_pengingatjadwalkeretaapi.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri

object LaunchMap {
    @SuppressLint("QueryPermissionsNeeded")
    fun launchMap(context: Context, koordintatStasiun: String?) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$koordintatStasiun")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        context.startActivity(mapIntent)
//        mapIntent.resolveActivity(context.packageManager)?.let {
//            context.startActivity(mapIntent)
//        }
    }
}