package com.deprojectmain.tunemood.screens

import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.deprojectmain.tunemood.R
import kotlinx.coroutines.delay
import java.io.IOException

@Composable
fun TrackPlayerScreen(
    id: Long,
    trackArtistName: String,
    trackAlbumCover: String,
    trackLink: String,
    trackTitle: String,
    onForwardClick: () -> Unit,
    onPreviousClick: () -> Unit,
) {
    val mediaPlayer = remember { MediaPlayer() }
    var isPlaying by remember { mutableStateOf(false) }
    val playPauseIcon = remember { mutableIntStateOf(R.drawable.baseline_play_arrow_24) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var trackDuration by remember { mutableIntStateOf(0) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }

    LaunchedEffect(trackLink) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(trackLink)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                trackDuration = mediaPlayer.duration
                mediaPlayer.start()
                isPlaying = true
                playPauseIcon.intValue = R.drawable.baseline_pause_circle_24
            }
            mediaPlayer.setOnCompletionListener {
                isPlaying = false
                playPauseIcon.intValue = R.drawable.baseline_play_arrow_24
            }
        } catch (e: IOException) {
            Log.e("TrackPlayerScreen", "Error setting data source", e)
        }
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (mediaPlayer.isPlaying) {
                sliderPosition = mediaPlayer.currentPosition.toFloat() / trackDuration
                delay(1000)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = trackTitle,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = trackArtistName,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Image(
            painter = rememberAsyncImagePainter(trackAlbumCover),
            contentDescription = "Album cover",
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Slider(
            value = sliderPosition,
            onValueChange = { newValue ->
                sliderPosition = newValue
                mediaPlayer.seekTo((sliderPosition * trackDuration).toInt())
            },
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPreviousClick) {
                Icon(
                    painter = painterResource(R.drawable.baseline_skip_previous_24),
                    contentDescription = "Previous"
                )
            }
            Spacer(modifier = Modifier.width(32.dp))
            IconButton(onClick = {
                if (isPlaying) {
                    mediaPlayer.pause()
                    playPauseIcon.intValue = R.drawable.baseline_play_arrow_24
                } else {
                    mediaPlayer.start()
                    playPauseIcon.intValue = R.drawable.baseline_pause_circle_24
                }
                isPlaying = !isPlaying
            }) {
                Icon(
                    painter = painterResource(playPauseIcon.intValue),
                    contentDescription = "Play/Pause",
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.width(32.dp))
            IconButton(onClick = onForwardClick) {
                Icon(
                    painter = painterResource(R.drawable.baseline_skip_next_24),
                    contentDescription = "Next"
                )
            }
        }
    }
}
