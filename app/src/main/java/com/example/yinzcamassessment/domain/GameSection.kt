package com.example.yinzcamassessment.domain

import com.google.gson.annotations.SerializedName

data class GameSection(
    @SerializedName("Heading")
    val heading: String,
    @SerializedName("Game")
    val games: List<Game>
)
