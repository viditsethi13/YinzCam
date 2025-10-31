package com.example.yinzcamassessment.presentation.screens

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.yinzcamassessment.common.ByeTextSize
import com.example.yinzcamassessment.common.OtherTextSize
import com.example.yinzcamassessment.common.BlackColor
import com.example.yinzcamassessment.common.ScoreContainerMinHeight
import com.example.yinzcamassessment.common.ScoreTextSize
import com.example.yinzcamassessment.common.SeasonContainerHeight
import com.example.yinzcamassessment.common.SeasonContainerTextSize
import com.example.yinzcamassessment.common.GreyColor
import com.example.yinzcamassessment.common.TeamLogoSize
import com.example.yinzcamassessment.common.TeamNameTextSize
import com.example.yinzcamassessment.common.TopBarColor
import com.example.yinzcamassessment.common.VSText
import com.example.yinzcamassessment.common.WhiteColor
import com.example.yinzcamassessment.domain.GameDisplay
import com.example.yinzcamassessment.domain.GameStatus
import com.example.yinzcamassessment.presentation.viewmodel.ScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(scheduleViewModel: ScheduleViewModel) {

    val isLoading by scheduleViewModel.isLoading
    val gameDisplay by scheduleViewModel.gameDisplay

    LaunchedEffect(Unit) {
        scheduleViewModel.getSchedule()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "SCHEDULE",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = WhiteColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Handle Menu press */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = WhiteColor
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = TopBarColor,
                    titleContentColor = WhiteColor,
                    navigationIconContentColor = WhiteColor,
                    scrolledContainerColor = TopBarColor,
                    actionIconContentColor = TopBarColor,
                    subtitleContentColor = TopBarColor
                )
            )
        }
    ){
        innerPadding ->
        if (isLoading){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(text = "Loading...")
            }
        } else {
            GameScoreBoard(gameDisplay, innerPadding)
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
            text = VSText,
            fontSize = OtherTextSize,
            fontWeight = FontWeight.Bold,
            color = GreyColor
        )
        TeamLogoDisplay(awayTeamLogoUrl)
    }
}

@Composable
private fun TeamLogoDisplay(url: String) {
    AsyncImage(
        model = url,
        contentDescription = "Translated description of what the image contains",
        modifier = Modifier.height(TeamLogoSize)
            .width(TeamLogoSize)
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
            .heightIn(min = ScoreContainerMinHeight),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ){
        //Team name row
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(
                text = game.homeTeamName,
                fontWeight = FontWeight.ExtraBold,
                fontSize = TeamNameTextSize,
                color = BlackColor,
                modifier = Modifier.weight(0.5f),
                textAlign = TextAlign.Start
            )
            Text(
                text = game.awayTeamName,
                fontWeight = FontWeight.ExtraBold,
                fontSize = TeamNameTextSize,
                color = BlackColor,
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
                fontSize = if (game.status == GameStatus.FINAL) ScoreTextSize else 14.sp,
                color = BlackColor,
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
                fontSize = if (game.status == GameStatus.FINAL) ScoreTextSize else OtherTextSize,
                color = BlackColor,
                modifier = Modifier.weight(0.3f),
                textAlign = TextAlign.End
            )
        }

        // Date and time row
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = game.date,
                fontSize = OtherTextSize,
                fontWeight = FontWeight.ExtraBold,
                color = GreyColor,
                modifier = Modifier.weight(0.3f),
                textAlign = TextAlign.Start
            )

            Text(
                text = game.week,
                fontSize = OtherTextSize,
                fontWeight = FontWeight.Medium,
                color = GreyColor,
                modifier = Modifier.weight(0.4f),
                textAlign = TextAlign.Center
            )

            Text(
                text = if (game.status == GameStatus.FINAL) "Final" else game.time,
                fontSize = OtherTextSize,
                fontWeight = FontWeight.ExtraBold,
                color = if (game.status == GameStatus.FINAL) BlackColor else GreyColor,
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
                    fontSize = OtherTextSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = GreyColor,
                    modifier = Modifier.weight(0.5f),
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider(color = GreyColor.copy(alpha = 0.5f), thickness = 1.dp)
    }

}

@Composable
fun ByeDisplay(game: GameDisplay) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
            .heightIn(min = ScoreContainerMinHeight),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // take all available vertical space
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = game.status.name,
                fontSize = ByeTextSize,
                fontWeight = FontWeight.Bold,
                color = GreyColor
            )
        }

        HorizontalDivider(color = GreyColor.copy(alpha = 0.5f), thickness = 1.dp)
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
            .height(SeasonContainerHeight)
            .background(Color.LightGray)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = heading,
            fontSize = SeasonContainerTextSize,
            fontWeight = FontWeight.SemiBold,
            color = GreyColor,
        )
    }
}