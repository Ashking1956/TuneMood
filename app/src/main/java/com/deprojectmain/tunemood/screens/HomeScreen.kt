package com.deprojectmain.tunemood.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.deprojectmain.tunemood.components.TrackItem
import com.deprojectmain.tunemood.data.Data
import com.deprojectmain.tunemood.navigation.TrackPlayerScreenClass
import com.deprojectmain.tunemood.viewmodel.APIinterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Composable
fun HomeScreen(navController: NavController) {
    val text = remember {
        mutableStateOf("")
    }
    val tempList = remember {
        mutableStateListOf<Data>()
    }
    val retrofitBuilder = Retrofit.Builder().baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create()).build().create(APIinterface::class.java)

    LaunchedEffect(Unit) {
        try {
            val response = withContext(Dispatchers.IO) {
                retrofitBuilder.getData("eminem").execute()
            }
            if (response.isSuccessful) {
                response.body()?.data?.let {
                    tempList.addAll(it)
                }
                Log.d("API Working", "onResponse: Data loaded successfully")
            } else {
                Log.d("API Error", "onResponse: ${response.errorBody()}")
                text.value = "Error: ${response.errorBody()}"
            }
        } catch (e: Exception) {
            Log.d("API Error", "onFailure: ${e.message}")
            text.value = "Working Not Fine!"
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(tempList.size) { index ->
            TrackItem(tempList[index], onClick = {
                val track = tempList[index]
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