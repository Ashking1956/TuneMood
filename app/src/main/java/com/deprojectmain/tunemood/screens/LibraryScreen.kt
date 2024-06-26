package com.deprojectmain.tunemood.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.deprojectmain.tunemood.components.CardView
import com.deprojectmain.tunemood.viewmodel.APIRepository
import com.deprojectmain.tunemood.viewmodel.APInterface
import com.deprojectmain.tunemood.viewmodel.MainViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun LibraryScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    LaunchedEffect(key1 = viewModel) {
        viewModel.fetchData()
    }

    val dataList = viewModel.data.collectAsState().value ?: emptyList()
    val artistList = viewModel.artists.collectAsState().value ?: emptyList()
    val moodList = listOf(
        "Happy", "Sad", "Angry", "Calm", "Anxious",
        "Excited", "Bored", "Love", "Confused", "Surprised",
        "Confident", "Lonely", "Hopeful", "Fearful", "Disgusted"
    )
    val musicGenresList = listOf(
        "Pop", "Rock", "Hip-Hop", "R&B", "Jazz", "Classical",
        "Electronic", "Country", "Reggae", "Blues", "Metal",
        "Folk", "Punk", "Soul", "Funk", "Disco", "Reggaeton",
        "Latin", "Alternative", "K-Pop", "Gospel", "Ska",
        "Bluegrass", "Ambient", "Dance"
    )

    var currentStep by remember { mutableIntStateOf(1) }
    var selectedMood by remember { mutableStateOf<String?>(null) }
    var selectedGenre by remember { mutableStateOf<String?>(null) }
    var selectedArtist by remember { mutableStateOf<String?>(null) }
    var selectedData by remember { mutableStateOf<Long?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .height(585.dp)
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (currentStep) {
                1 -> {
                    Text("Select a Mood", style = MaterialTheme.typography.titleLarge)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(moodList) { mood ->
                            CardView(
                                cover = "", // No cover for mood, provide a default image if needed
                                title = mood,
                                selected = selectedMood == mood,
                                onClick = {
                                    selectedMood = mood
                                }
                            )
                        }
                    }
                }

                2 -> {
                    Text("Select a Music Genre", style = MaterialTheme.typography.titleLarge)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(musicGenresList) { genre ->
                            CardView(
                                cover = "", // No cover for genre, provide a default image if needed
                                title = genre,
                                selected = selectedGenre == genre,
                                onClick = {
                                    selectedGenre = genre
                                }
                            )
                        }
                    }
                }

                3 -> {
                    Text("Select an Artist", style = MaterialTheme.typography.titleLarge)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(artistList) { artist ->
                            CardView(
                                cover = artist.picture, // Assuming Artist has a cover property
                                title = artist.name, // Assuming Artist has a name property
                                selected = selectedArtist == artist.name,
                                onClick = {
                                    selectedArtist = artist.name
                                }
                            )
                        }
                    }
                }

                4 -> {
                    Text("Select a Data Item", style = MaterialTheme.typography.titleLarge)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(dataList) { data ->
                            CardView(
                                cover = data.album.cover, // Assuming Data has a cover property
                                title = data.title, // Assuming Data has a title property
                                selected = selectedData == data.id,
                                onClick = {
                                    selectedData = data.id
                                }
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = {
                if (currentStep < 5) {
                    currentStep++
                }
                if (currentStep == 5) {
                    // TODO: Implement the next step or action when currentStep is 5
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .padding(horizontal = 16.dp)
                .wrapContentHeight(), // Ensures button takes only the height it needs
            shape = RoundedCornerShape(25.dp),
        ) {
            Text(
                text = "Next",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LibraryScreenPreview() {
    val navController = rememberNavController()
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APInterface::class.java)
    }

    // ViewModel initialization
    val model = remember {
        MainViewModel(repository = APIRepository(retrofit))
    }
    LibraryScreen(navController = navController, viewModel = model)
}
