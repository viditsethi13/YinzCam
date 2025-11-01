package com.example.yinzcamassessment.presentation.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.yinzcamassessment.common.Constants
import com.example.yinzcamassessment.data.remote.dto.GameStatus
import com.example.yinzcamassessment.domain.model.GameDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(scheduleViewModel: ScheduleViewModel) {

    val state by scheduleViewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "SCHEDULE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Constants.WhiteColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Handle Menu press */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Constants.WhiteColor
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = Constants.TopBarColor,
                    titleContentColor = Constants.WhiteColor,
                    navigationIconContentColor = Constants.WhiteColor,
                    scrolledContainerColor = Constants.TopBarColor,
                    actionIconContentColor = Constants.TopBarColor,
                    subtitleContentColor = Constants.TopBarColor
                )
            )
        }
    ){
        innerPadding ->
        GameScoreBoard(games = state.games, innerPadding = innerPadding)
        if(state.error != null){
            Text(
                text = state.error.toString(),
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )
        }
        if (state.isLoading){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }

}

@Composable
fun TeamLogosRow(
    homeTeamLogoUrl: String,
    awayTeamLogoUrl: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TeamLogoDisplay(homeTeamLogoUrl)
        Text(
            text = Constants.VSText,
            fontSize = Constants.OtherTextSize,
            fontWeight = FontWeight.Bold,
            color = Constants.GreyColor
        )
        TeamLogoDisplay(awayTeamLogoUrl)
    }
}

@Composable
private fun TeamLogoDisplay(url: String) {
    AsyncImage(
        model = url,
        contentDescription = "Translated description of what the image contains",
        modifier = Modifier
            .height(Constants.TeamLogoSize)
            .width(Constants.TeamLogoSize)
    )
}

@Composable
fun GameCard(game: GameDisplay) {

    if (game.status == GameStatus.BYE){
        ByeDisplay(game)
        return
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .heightIn(min = Constants.ScoreContainerMinHeight),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ){
        //Team name row
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(
                text = game.homeTeamName,
                fontWeight = FontWeight.ExtraBold,
                fontSize = Constants.TeamNameTextSize,
                color = Constants.BlackColor,
                modifier = Modifier.weight(0.5f),
                textAlign = TextAlign.Start
            )
            Text(
                text = game.awayTeamName,
                fontWeight = FontWeight.ExtraBold,
                fontSize = Constants.TeamNameTextSize,
                color = Constants.BlackColor,
                modifier = Modifier.weight(0.5f),
                textAlign = TextAlign.End
            )
        }

        // Score and logo row
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(
                text = if (game.status == GameStatus.FINAL) game.homeScore else game.homeRecord,
                fontWeight = if (game.status == GameStatus.FINAL) FontWeight.ExtraBold else FontWeight.Normal,
                fontSize = if (game.status == GameStatus.FINAL) Constants.ScoreTextSize else 14.sp,
                color = if (game.status == GameStatus.FINAL) Constants.BlackColor else Constants.GreyColor,
                modifier = Modifier.weight(0.3f),
                textAlign = TextAlign.Start
            )

            TeamLogosRow(
                homeTeamLogoUrl = game.homeTeamLogoUrl,
                awayTeamLogoUrl = game.awayTeamLogoUrl,
                modifier = Modifier.weight(0.4f)
            )


            Text(
                text = if (game.status == GameStatus.FINAL) game.awayScore else game.awayRecord,
                fontWeight = if (game.status == GameStatus.FINAL) FontWeight.ExtraBold else FontWeight.Normal,
                fontSize = if (game.status == GameStatus.FINAL) Constants.ScoreTextSize else Constants.OtherTextSize,
                color = if (game.status == GameStatus.FINAL) Constants.BlackColor else Constants.GreyColor,
                modifier = Modifier.weight(0.3f),
                textAlign = TextAlign.End
            )
        }

        // Date and time row
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = game.date,
                fontSize = Constants.OtherTextSize,
                fontWeight = FontWeight.Bold,
                color = Constants.BlackColor,
                modifier = Modifier.weight(0.3f),
                textAlign = TextAlign.Start
            )

            Text(
                text = game.week,
                fontSize = Constants.OtherTextSize,
                fontWeight = FontWeight.Medium,
                color = Constants.GreyColor,
                modifier = Modifier.weight(0.4f),
                textAlign = TextAlign.Center
            )

            Text(
                text = if (game.status == GameStatus.FINAL) "Final" else game.time,
                fontSize = Constants.OtherTextSize,
                fontWeight = FontWeight.Bold,
                color = Constants.BlackColor,
                modifier = Modifier.weight(0.3f),
                textAlign = TextAlign.End
            )
        }

        if(game.status == GameStatus.SCHEDULED){
            // Extra detail
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween){
                Text(
                    text = game.tv,
                    fontSize = Constants.OtherTextSize,
                    fontWeight = FontWeight.Bold,
                    color = Constants.BlackColor,
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(color = Constants.GreyColor.copy(alpha = 0.5f), thickness = 1.dp)
    }

}

@Composable
fun ByeDisplay(game: GameDisplay) {
    Column(
        Modifier
            .fillMaxWidth()
            .heightIn(min = Constants.ScoreContainerMinHeight),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = game.status.name,
                fontSize = Constants.ByeTextSize,
                fontWeight = FontWeight.Bold,
                color = Constants.GreyColor
            )
        }

        HorizontalDivider(color = Constants.GreyColor.copy(alpha = 0.5f), thickness = 1.dp)
    }
}

@Composable
fun GameScoreBoard(games: List<GameDisplay>, innerPadding: PaddingValues) {
    val groupedGames = games.groupBy { it.heading }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 20.dp)
    ) {
        groupedGames.forEach { (heading, gamesInGroup) ->
            stickyHeader {
                HeadingDisplay(heading = heading)
            }
            items(gamesInGroup) { game ->
                GameCard(game = game)
            }
        }
    }
}

@Composable
fun HeadingDisplay(heading: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Constants.SeasonContainerHeight)
            .background(Constants.HeadingDisplayColor)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = heading,
            fontSize = Constants.SeasonContainerTextSize,
            fontWeight = FontWeight.SemiBold,
            color = Constants.GreyColor,
        )
    }
}