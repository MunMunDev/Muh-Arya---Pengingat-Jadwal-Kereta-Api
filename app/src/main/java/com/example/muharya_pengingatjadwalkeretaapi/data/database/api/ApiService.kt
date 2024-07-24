package com.example.muharya_pengingatjadwalkeretaapi.data.database.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

//    const val BASE_URL = "https://e-tugas.my.id/jadwal-kereta-api/"
//    const val BASE_URL = "http://192.168.228.91/jadwal-kereta-api/" //Hotspot
//    const val BASE_URL = "http://192.168.56.91/jadwal-kereta-api/" //Hotspot 2
    const val BASE_URL = "http://192.168.17.7/jadwal-kereta-api/"    // Wifi
//    const val BASE_URL = "http://192.168.17.6/jadwal-kereta-api/"    // Wifi Game
//    const val BASE_URL = "http://192.168.4.155/jadwal-kereta-api/"    // Wifi UMPAR
//    const val BASE_URL = "http://192.168.1.14/jadwal-kereta-api/"    // Wifi UMPAR
//    const val BASE_URL = "http://192.168.1.141/jadwal-kereta-api/"    // Wifi Sudut Lagi
//    const val BASE_URL = "http://192.168.1.18/jadwal-kereta-api/"   // Wifi makkode

    const val STORAGE_PERMISSION_CODE = 10
    const val IMAGE_CODE = 11

    fun getRetrofit(): ApiConfig {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(ApiConfig::class.java)
    }

}