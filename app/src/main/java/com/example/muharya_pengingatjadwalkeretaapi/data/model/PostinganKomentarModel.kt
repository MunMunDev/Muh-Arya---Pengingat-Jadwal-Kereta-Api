package com.example.muharya_pengingatjadwalkeretaapi.data.model

import com.example.muharya_pengingatjadwalkeretaapi.adapter.PostinganKomentarAdapter
import com.google.gson.annotations.SerializedName

class PostinganKomentarModel (
    @SerializedName("id_postingan_komentar")
    val id_postingan_komentar: String,

    @SerializedName("id_postingan")
    val id_postingan: String,

    @SerializedName("id_postingan_komentar_user")
    val id_postingan_komentar_user: String,

    @SerializedName("id_user_komentar")
    val id_user_komentar: String,

    @SerializedName("komentar")
    val komentar: String,

    @SerializedName("waktu")
    val waktu: String,

    @SerializedName("user")
    val user: ArrayList<UserModel> = arrayListOf(),

    @SerializedName("postingan_komentar")
    val postingan_komentar: ArrayList<PostinganKomentarModel> = arrayListOf()
)