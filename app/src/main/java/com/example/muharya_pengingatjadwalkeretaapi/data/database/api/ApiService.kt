package com.example.muharya_pengingatjadwalkeretaapi.data.database.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    private const val BASE_URL = "https://aplikasitugas12345.000webhostapp.com/jadwal-kereta-api/"
//    private const val BASE_URL = "http://192.168.228.91/jadwal-kereta-api/" //Hotspot
//    private const val BASE_URL = "http://192.168.56.91/jadwal-kereta-api/" //Hotspot 2
//    private const val BASE_URL = "http://192.168.1.7/jadwal-kereta-api/"    // Wifi
//    private const val BASE_URL = "http://192.168.1.4/jadwal-kereta-api/"    // Wifi Game
//    private const val BASE_URL = "http://192.168.4.155/jadwal-kereta-api/"    // Wifi UMPAR
//    private const val BASE_URL = "http://192.168.1.14/jadwal-kereta-api/"    // Wifi UMPAR
//    private const val BASE_URL = "http://192.168.1.141/jadwal-kereta-api/"    // Wifi Sudut Lagi
//    private const val BASE_URL = "http://192.168.1.18/jadwal-kereta-api/"   // Wifi makkode

    fun getRetrofit(): ApiConfig {
        val gson = GsonBuilder().setLenient().create()
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(ApiConfig::class.java)
    }

}