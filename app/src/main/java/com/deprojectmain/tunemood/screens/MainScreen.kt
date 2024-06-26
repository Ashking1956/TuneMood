package com.deprojectmain.tunemood.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deprojectmain.tunemood.components.AlbumGridView
import com.deprojectmain.tunemood.components.ArtistGridView
import com.deprojectmain.tunemood.components.SectionHeader
import com.deprojectmain.tunemood.components.TrackGridView
import com.deprojectmain.tunemood.data.Album
import com.deprojectmain.tunemood.data.Artist
import com.deprojectmain.tunemood.data.Data

@Composable
fun MainScreen(navController: NavController, dataList: List<Data>?) {
    Column(modifier = Modifier.padding(5.dp)) {
        val albums = remember { mutableListOf<Album>() }
        val artists = remember { mutableListOf<Artist>() }
        val scrollState = rememberScrollState()

        dataList?.forEach { item ->
            if (!albums.contains(item.album)) {
                albums.add(item.album)
            }
            if (!artists.contains(item.artist)) {
                artists.add(item.artist)
            }
        }
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            // Albums
            SectionHeader(title = "Albums")
            AlbumGridView(albums = albums, navController = navController)

            // Artists
            SectionHeader(title = "Artists")
            ArtistGridView(albums = artists, onClick = {})

            // Songs
            SectionHeader(title = "Songs")
            dataList?.let {
                TrackGridView(albums = it, navController = navController)
            }
        }
    }
}

