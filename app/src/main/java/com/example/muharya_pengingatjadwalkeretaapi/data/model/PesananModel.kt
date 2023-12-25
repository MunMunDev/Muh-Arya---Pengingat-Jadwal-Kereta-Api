package com.example.muharya_pengingatjadwalkeretaapi.data.model

import com.google.gson.annotations.SerializedName

class PesananModel(
    @SerializedName("id_pesanan")
    val id_pesanan: String,

    @SerializedName("id_user")
    val id_user: String,

    @SerializedName("nama")
    val nama: String,

    @SerializedName("nomor_hp")
    val nomor_hp: String,

    @SerializedName("tanggal")
    val tanggal: String,

    @SerializedName("waktu")
    val waktu: String,

    @SerializedName("dari_kota_kab")
    val dari_kota_kab: String,

    @SerializedName("dari_stasiun")
    val dari_stasiun: String,

    @SerializedName("sampai_kota_kab")
    val sampai_kota_kab: String,

    @SerializedName("sampai_stasiun")
    val sampai_stasiun: String,

    @SerializedName("jumlah_tiket")
    val jumlah_tiket: String,

    @SerializedName("harga_satuan")
    val harga_satuan: String,

    @SerializedName("total_harga")
    val total_harga: String,

    @SerializedName("keterangan")
    val keterangan: String,

    @SerializedName("waktu_pembelian")
    val waktu_pembelian: String,

    @SerializedName("waktu_alarm")
    val waktu_alarm: String,

    @SerializedName("status_alarm")
    val status_alarm: String,

    @SerializedName("jumlah_notif")
    val jumlah_notif: String
)