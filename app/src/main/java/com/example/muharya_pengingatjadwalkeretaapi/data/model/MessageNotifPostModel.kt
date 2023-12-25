package com.example.muharya_pengingatjadwalkeretaapi.data.model

import com.google.gson.annotations.SerializedName

class MessageNotifPostModel (
    @SerializedName("response")
    val response: Int,

    @SerializedName("status")
    val status: String,

    @SerializedName("message_response")
    val message_response: String
)