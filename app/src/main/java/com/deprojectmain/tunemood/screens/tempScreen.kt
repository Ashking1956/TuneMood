package com.deprojectmain.tunemood.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.deprojectmain.tunemood.R
import com.deprojectmain.tunemood.components.AlbumGridView
import com.deprojectmain.tunemood.components.ArtistGridView
import com.deprojectmain.tunemood.components.SectionHeader
import com.deprojectmain.tunemood.components.TrackGridView
import com.deprojectmain.tunemood.viewmodel.APIRepository
import com.deprojectmain.tunemood.viewmodel.APInterface
import com.deprojectmain.tunemood.viewmodel.MainViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempBrowserScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val searchHistory = remember { mutableStateListOf<String>() }
    val scrollState = rememberScrollState()
    val retrofit = remember {
        Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APInterface::class.java)
    }
    // ViewModel initialization
    val viewModel = remember {
        MainViewModel(repository = APIRepository(retrofit))
    }

    val searchedAlbums = viewModel.searchAlbums.collectAsState()
    val searchedArtists = viewModel.searchArtists.collectAsState()
    val searchedTracks = viewModel.searchData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp)
        ) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .wrapContentHeight(),
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                    if (query.isNotEmpty()) {
                        searchHistory.add(query)
                        active = false
                        viewModel.fetchSearchResults(query)
                    }
                },
                active = active,
                onActiveChange = { active = it },
                placeholder = {
                    Text(
                        text = "Search",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Normal
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .clickable {
                                if (query.isNotEmpty()) {
                                    query = ""
                                } else {
                                    active = false
                                }
                            }
                            .padding(8.dp)
                    )
                }
            ) {
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(searchHistory.size) { index ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    query = searchHistory[index]
                                    active = false
                                    viewModel.fetchSearchResults(query)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_history_24),
                                contentDescription = "History",
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = searchHistory[index],
                                modifier = Modifier.weight(1f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.isLoading.collectAsState().value) {
                    Column(
                        modifier = Modifier.wrapContentSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color(0xFF68B7E8)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading...",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        SectionHeader("Albums")
                        searchedAlbums.value?.let {
                            AlbumGridView(
                                navController = navController,
                                albums = it
                            )
                        }
                        SectionHeader("Artists")
                        searchedArtists.value?.let {
                            ArtistGridView(
                                navController = navController,
                                albums = it
                            )
                        }
                        SectionHeader("Songs")
                        searchedTracks.value?.let {
                            TrackGridView(
                                albums = it,
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TempBrowserScreenPreview() {
    val navController = rememberNavController()
    TempBrowserScreen(navController)
}
