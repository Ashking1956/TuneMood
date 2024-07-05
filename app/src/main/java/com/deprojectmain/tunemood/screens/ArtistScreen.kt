package com.deprojectmain.tunemood.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.deprojectmain.tunemood.components.TrackItem
import com.deprojectmain.tunemood.navigation.TrackPlayerScreenClass
import com.deprojectmain.tunemood.viewmodel.MainViewModel

@Composable
fun ArtistScreen(
    navController: NavController,
    id: Int,
    title: String,
    cover: String,
    viewModel: MainViewModel
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(cover),
            contentDescription = "Album cover",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Track List",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(viewModel.fetchTracksOfArtist(title)) { track ->
                TrackItem(track, onClick = {
                    navController.navigate(
                        TrackPlayerScreenClass(
                            trackTitle = track.title,
                            trackArtistName = track.artist.name,
                            trackAlbumCover = track.album.cover,
                            trackLink = track.preview,
                            id = track.id,
                        )
                    )
                })
            }
        }
    }
}