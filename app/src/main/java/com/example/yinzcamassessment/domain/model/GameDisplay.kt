package com.example.yinzcamassessment.domain.model

import com.example.yinzcamassessment.data.remote.dto.GameStatus

data class GameDisplay(
    val homeTeamName: String = "",
    val awayTeamName: String = "",
    val homeTeamLogoUrl: String = "",
    val awayTeamLogoUrl: String = "",
    val homeScore: String = "",
    val awayScore: String = "",
    val homeRecord: String = "",
    val awayRecord: String = "",
    val date: String = "",
    val time: String = "",
    val week: String = "",
    val tv: String = "",
    val heading: String,
    val status: GameStatus
)