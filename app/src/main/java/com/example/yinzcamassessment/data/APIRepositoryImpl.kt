package com.example.yinzcamassessment.data

import com.example.yinzcamassessment.domain.GameDisplay
import com.example.yinzcamassessment.domain.GameStatus
import com.example.yinzcamassessment.domain.Team
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object RetrofitClient{
    const val BASE_URL = "http://files.yinzcam.com.s3.amazonaws.com/"

    val apiService: APIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }
}


class APIRepositoryImpl: APIRepository  {

    private val apiService = RetrofitClient.apiService

    override suspend fun getSchedules(): List<GameDisplay> {
        var gameList = mutableListOf<GameDisplay>()
        val response = apiService.getSchedules()
        val primaryTeam = Team(
            triCode = response.team.triCode,
            name = response.team.name,
            record = response.team.record
        )
        response.gameSection.forEach { gameSection ->
            gameSection.games.forEach { game ->
                if (GameStatus.fromType(game.gameType) != GameStatus.BYE) {
                    val homeTeam: Team
                    val awayTeam: Team
                    val homeScore: String
                    val awayScore: String

                    if (game.home) {
                        homeTeam = primaryTeam
                        awayTeam = game.opponent
                        homeScore = game.homeScore ?: ""
                        awayScore = game.awayScore ?: ""
                    } else {
                        homeTeam = game.opponent
                        awayTeam = primaryTeam
                        homeScore = game.homeScore ?: ""
                        awayScore = game.awayScore ?: ""
                    }

                    gameList.add(
                        GameDisplay(
                            homeTeamName = homeTeam.name,
                            awayTeamName = awayTeam.name,
                            homeTeamLogoUrl = "https://yc-app-resources.s3.amazonaws.com/nfl/logos/nfl_${homeTeam.triCode.lowercase()}_light.png",
                            awayTeamLogoUrl = "https://yc-app-resources.s3.amazonaws.com/nfl/logos/nfl_${awayTeam.triCode.lowercase()}_light.png",
                            homeScore = homeScore,
                            awayScore = awayScore,
                            date = formatDate(game.date.timeStamp),
                            time = formatTime(game.date.timeStamp),
                            week = game.week,
                            tv = game.tv ?: "",
                            status = GameStatus.fromType(game.gameType),
                            homeRecord = homeTeam.record,
                            awayRecord = awayTeam.record,
                            heading = gameSection.heading
                        )
                    )
                }
                else{
                    // Add a bye week
                    gameList.add(
                        GameDisplay(
                            homeTeamName = primaryTeam.name,
                            status = GameStatus.BYE,
                            week = game.week,
                            heading = gameSection.heading
                        )
                    )
                }
            }

        }
        return gameList
    }

    private fun formatTime(timestamp: String): String {
        return try {
            // Input format is explicitly UTC
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            // Output format uses the device's default timezone
            val outputFormat = SimpleDateFormat("h:mm a", Locale.US).apply {
                timeZone = TimeZone.getDefault()
            }
            val date = inputFormat.parse(timestamp)
            date?.let { outputFormat.format(it) } ?: timestamp
        } catch (e: Exception) {
            println("Error formatting time: ${e.message}")
            timestamp
        }
    }

    private fun formatDate(timestamp: String): String {
        return try {
            // Input format is explicitly UTC
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            // Output format uses the device's default timezone
            val outputFormat = SimpleDateFormat("EEE, MMM d", Locale.US).apply {
                timeZone = TimeZone.getDefault()
            }
            val date = inputFormat.parse(timestamp)
            date?.let { outputFormat.format(it) } ?: timestamp
        } catch (e: Exception) {
            println("Error formatting date: ${e.message}")
            timestamp
        }
    }

}
