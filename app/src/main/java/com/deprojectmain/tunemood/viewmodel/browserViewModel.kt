package com.deprojectmain.tunemood.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deprojectmain.tunemood.data.Album
import com.deprojectmain.tunemood.data.Artist
import com.deprojectmain.tunemood.data.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BrowseViewModel : ViewModel() {
    private val albums = mutableListOf<Album>()
    private val artists = mutableListOf<Artist>()
    private val tracks = mutableListOf<Data>()
    val _albums = albums
    val _artist = artists
    val _track = tracks
    fun fetchSearchResults(retrofitBuilder: APIinterface, query: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    retrofitBuilder.getData(query).execute()
                }
                if (response.isSuccessful) {
                    response.body()?.data?.let { dataList ->
                        for (item in dataList) {
                            if (!albums.contains(item.album)) {
                                albums.add(item.album)
                            }
                            if (!artists.contains(item.artist)) {
                                artists.add(item.artist)
                            }
                            if (!tracks.contains(item) && item.type == "track") {
                                tracks.add(item)
                            }
                        }
                    }
                    Log.d("API Working", "onResponse: Data loaded successfully")
                } else {
                    Log.d("API Error", "onResponse: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.d("API Error", "onFailure: ${e.message}")
            }
        }
    }
}