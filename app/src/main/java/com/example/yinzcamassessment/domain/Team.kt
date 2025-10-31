package com.example.yinzcamassessment.domain

import com.google.gson.annotations.SerializedName

data class Team(
    @SerializedName("TriCode")
    val triCode: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Record")
    val record: String
)
