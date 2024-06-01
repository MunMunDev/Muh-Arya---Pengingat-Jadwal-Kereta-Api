package com.example.muharya_pengingatjadwalkeretaapi.data.model

import com.google.gson.annotations.SerializedName

class ResponseModel(
    @SerializedName("response")
    var response: String? = null,

    @SerializedName("status")
    var status: String? = null,

    @SerializedName("message_response")
    var message_response: String? = null
)