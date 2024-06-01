package com.example.muharya_pengingatjadwalkeretaapi.data.model

import com.google.gson.annotations.SerializedName

class RuteModel (
    @SerializedName("id_rute")
    val id_rute: Int,

    @SerializedName("dari_kota_kab")
    val dari_kota_kab: String,

    @SerializedName("dari_stasiun")
    val dari_stasiun: String,

    @SerializedName("sampai_kota_kab")
    val sampai_kota_kab: String,

    @SerializedName("sampai_stasiun")
    val sampai_stasiun: String,

    @SerializedName("harga")
    val harga: String,

    @SerializedName("jumlah_tiket")
    val jumlah_tiket: String,

    @SerializedName("waktu")
    val waktu: String,

    @SerializedName("waktu_sampai")
    val waktu_sampai: String,

    @SerializedName("koordinat_stasiun_awal")
    val koordinat_stasiun_awal: String,

    @SerializedName("koordinat_stasiun_tujuan")
    val koordinat_stasiun_tujuan: String
)