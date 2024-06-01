package com.example.muharya_pengingatjadwalkeretaapi.data.model

import com.google.gson.annotations.SerializedName

class StasiunModel (
    @SerializedName("id_stasiun")
    val id_stasiun: Int,

    @SerializedName("nama_stasiun")
    val nama_stasiun: String,

    @SerializedName("kota_kab")
    val kota_kab: String,

//    @SerializedName("koordinat_stasiun")
//    val koordinat_stasiun: String
)