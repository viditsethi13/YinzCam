package com.example.yinzcamassessment.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DateTime(
    @SerializedName("Timestamp")
    val timeStamp: String,
)