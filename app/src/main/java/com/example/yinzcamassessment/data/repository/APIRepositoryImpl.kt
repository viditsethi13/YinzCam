package com.example.yinzcamassessment.data.repository

import com.example.yinzcamassessment.common.Resource
import com.example.yinzcamassessment.data.remote.APIService
import com.example.yinzcamassessment.domain.model.GameDisplay
import com.example.yinzcamassessment.data.remote.dto.GameStatus
import com.example.yinzcamassessment.data.remote.dto.Team
import com.example.yinzcamassessment.domain.repository.APIRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object RetrofitClient{
    const val BASE_URL = "http://files.yinzcam.com.s3.amazonaws.com/"
    const val LOGO_URL = "https://yc-app-resources.s3.amazonaws.com/"
    val apiService: APIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)
    }
}


class APIRepositoryImpl: APIRepository {

    private val apiService = RetrofitClient.apiService

    override suspend fun getSchedules(): Flow<Resource<List<GameDisplay>>> = flow {

        emit(Resource.Loading())
        try{
            val gameList = mutableListOf<GameDisplay>()
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
                                homeTeamLogoUrl = "${RetrofitClient.LOGO_URL}nfl/logos/nfl_${homeTeam.triCode.lowercase()}_light.png",
                                awayTeamLogoUrl = "${RetrofitClient.LOGO_URL}nfl/logos/nfl_${awayTeam.triCode.lowercase()}_light.png",
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
            emit(Resource.Success(gameList))
        }
        catch (e: HttpException) {
            emit(Resource.Error("Oops, something went wrong: ${e.message()}"))
        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach the server. ${e.message}"))
        } catch (e: Exception) {
            emit(Resource.Error("Something went wrong. ${e.message}"))
        }
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
