package com.example.yinzcamassessment.domain

import com.google.gson.annotations.SerializedName

data class TeamSchedule(
    @SerializedName("Team")
    val team: Team,
    @SerializedName("GameSection")
    val gameSection: List<GameSection>
)