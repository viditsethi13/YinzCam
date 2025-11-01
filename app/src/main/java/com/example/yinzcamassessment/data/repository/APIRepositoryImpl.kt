package com.example.yinzcamassessment.data.repository

import com.example.yinzcamassessment.common.Constants
import com.example.yinzcamassessment.common.Resource
import com.example.yinzcamassessment.data.remote.APIService
import com.example.yinzcamassessment.data.remote.dto.GameStatus
import com.example.yinzcamassessment.data.remote.dto.Team
import com.example.yinzcamassessment.domain.model.GameDisplay
import com.example.yinzcamassessment.domain.repository.APIRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone


class APIRepositoryImpl @Inject constructor(
    private val apiService: APIService,
    private val logoUrl: String,
    private val logoUrlSuffix: String
): APIRepository{

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
            val year = fetchYear(response.defaultGameId)
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
                                homeTeamLogoUrl = "$logoUrl${homeTeam.triCode.lowercase()}$logoUrlSuffix",
                                awayTeamLogoUrl = "$logoUrl${awayTeam.triCode.lowercase()}$logoUrlSuffix",
                                homeScore = homeScore,
                                awayScore = awayScore,
                                date = formatDate(game.date.timeStamp),
                                time = formatTime(game.date.timeStamp),
                                week = game.week,
                                tv = game.tv ?: "",
                                status = GameStatus.fromType(game.gameType),
                                homeRecord = homeTeam.record,
                                awayRecord = awayTeam.record,
                                heading = "$year ${gameSection.heading}"
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
                                heading = "$year ${gameSection.heading}"
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

    private fun fetchYear(defaultGameId: String): String{
        return when {
            defaultGameId.length < 4 -> ""
            else -> defaultGameId.take( 4)
        }
    }

}
