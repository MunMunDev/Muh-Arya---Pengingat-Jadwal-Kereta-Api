package com.example.muharya_pengingatjadwalkeretaapi.data.model

import com.google.gson.annotations.SerializedName

class PostinganModel (
    @SerializedName("id_postingan")
    val id_postingan: String? = null,

    @SerializedName("id_user")
    val id_user: String? = null,

    @SerializedName("gambar")
    val gambar: String? = null,

    @SerializedName("caption")
    val caption: String? = null,

    @SerializedName("waktu")
    val waktu: String? = null,

    @SerializedName("user")
    val user: ArrayList<UserModel> = arrayListOf(),

//    @SerializedName("postingan_komentar")
//    val postingan_komentar: ArrayList<PostinganKomentarModel> = arrayListOf(),

    @SerializedName("postingan_like")
    val postingan_like: ArrayList<PostinganLikeModel> = arrayListOf(),
)