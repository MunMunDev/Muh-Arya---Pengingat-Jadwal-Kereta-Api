package com.example.muharya_pengingatjadwalkeretaapi.data.model

import com.google.gson.annotations.SerializedName

class PostinganLikeModel (
    @SerializedName("id_postingan_like")
    val id_postingan_like: String,

    @SerializedName("id_postingan")
    val id_postingan: String,

    @SerializedName("id_user_like")
    val id_user_like: String,

    @SerializedName("waktu")
    val waktu: String
)