package com.example.yinzcamassessment.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Game(
    @SerializedName("Week")
    val week: String,
    @SerializedName("TV")
    val tv: String?,
    @SerializedName("Type")
    val gameType: String,
    @SerializedName("Date")
    val date: DateTime,
    @SerializedName("Home")
    val home: Boolean,
    @SerializedName("Opponent")
    val opponent: Team,
    @SerializedName("HomeScore")
    val homeScore: String?,
    @SerializedName("AwayScore")
    val awayScore: String?
)