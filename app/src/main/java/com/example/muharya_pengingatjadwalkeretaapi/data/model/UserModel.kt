package com.example.muharya_pengingatjadwalkeretaapi.data.model

import com.google.gson.annotations.SerializedName

class UserModel (
    @SerializedName("id_user")
    val id_user: Int,

    @SerializedName("nama")
    val nama: String,

    @SerializedName("alamat")
    val alamat: String,

    @SerializedName("nomor_hp")
    val nomor_hp: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("sebagai")
    val sebagai: String,

    @SerializedName("tanggal")
    val tanggal: String,

    @SerializedName("message")
    val message: String
)