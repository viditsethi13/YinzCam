package com.example.yinzcamassessment.domain

import com.google.gson.annotations.SerializedName

data class DateTime(
    @SerializedName("Timestamp")
    val timeStamp: String,
)
