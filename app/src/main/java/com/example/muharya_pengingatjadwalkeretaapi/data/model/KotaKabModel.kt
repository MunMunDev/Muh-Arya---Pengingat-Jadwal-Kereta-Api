package com.example.muharya_pengingatjadwalkeretaapi.data.model

import com.google.gson.annotations.SerializedName

class KotaKabModel (
    @SerializedName("id_kota_kab")
    val id_kota_kab: Int,

    @SerializedName("kota_kab")
    val kota_kab: String
)