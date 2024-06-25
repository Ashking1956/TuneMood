package com.deprojectmain.tunemood.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.deprojectmain.tunemood.R
import com.deprojectmain.tunemood.components.AlbumGridView
import com.deprojectmain.tunemood.components.ArtistGridView
import com.deprojectmain.tunemood.components.SectionHeader
import com.deprojectmain.tunemood.components.TrackGridView
import com.deprojectmain.tunemood.viewmodel.APIinterface
import com.deprojectmain.tunemood.viewmodel.BrowseViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    navController: NavController
) {
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val searchHistory = remember { mutableStateListOf<String>() }
    val scrollState = rememberScrollState()
    val viewModel = viewModel<BrowseViewModel>()
    val retrofitBuilder = remember {
        Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIinterface::class.java)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp), // Added consistent padding
        topBar = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp) // Added vertical padding
                    .wrapContentHeight(),
                query = query,
                onQueryChange = { query = it },
                onSearch = {
                    if (query.isNotEmpty()) {
                        searchHistory.add(query)
                        active = false
                        viewModel.fetchSearchResults(retrofitBuilder, query)
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
                            .padding(8.dp) // Increased touchable area
                    )
                }
            ) {
                LazyColumn(modifier = Modifier.padding(8.dp)) {
                    items(searchHistory.size) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp) // Added vertical padding
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_history_24),
                                contentDescription = "History",
                                modifier = Modifier.padding(end = 8.dp) // Spacing between icon and text
                            )
                            Text(
                                text = searchHistory[it],
                                modifier = Modifier.clickable {
                                    query = searchHistory[it]
                                    active = false
                                    viewModel.fetchSearchResults(retrofitBuilder, query)
                                }
                            )
                        }
                    }
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            SectionHeader("Albums")
            AlbumGridView(navController ,albums = viewModel._albums)

            SectionHeader("Artists")
            ArtistGridView(viewModel._artist)

            SectionHeader("Songs")
            TrackGridView(viewModel._track)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BrowseScreenPreview() {
    val navController = rememberNavController()
    BrowseScreen(navController = navController)
}
