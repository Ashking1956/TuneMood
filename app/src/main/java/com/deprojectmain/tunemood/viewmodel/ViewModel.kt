package com.deprojectmain.tunemood.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deprojectmain.tunemood.data.Album
import com.deprojectmain.tunemood.data.Artist
import com.deprojectmain.tunemood.data.Data
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: APIRepository) : ViewModel() {
    private val _data = MutableStateFlow<List<Data>?>(null)
    val data: StateFlow<List<Data>?> get() = _data

    private val _albums = MutableStateFlow<List<Album>?>(null)
    val albums: StateFlow<List<Album>?> get() = _albums

    private val _artists = MutableStateFlow<List<Artist>?>(null)
    val artists: StateFlow<List<Artist>?> get() = _artists

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading


    private val artistsList = listOf(
        "Olivia Rodrigo", "Lil Nas X", "Coldplay", "Jason Aldean", "Luke Combs", "Miley Cyrus",
        "Billie Eilish", "Doja Cat", "Ed Sheeran", "Ariana Grande", "Travis Scott",
        "Taylor Swift", "Justin Bieber", "Dua Lipa", "BTS", "The Weeknd", "Drake",
        "Harry Styles", "Post Malone", "Bad Bunny", "Megan Thee Stallion", "Kanye West",
        "Shawn Mendes", "Cardi B", "Lizzo", "SZA", "Bruno Mars", "Adele", "Chris Stapleton"
    )

    fun fetchData() {
        viewModelScope.launch {
            try {
                val fetchedData = mutableListOf<Data>()
                val fetchedAlbums = mutableListOf<Album>()
                val fetchedArtists = mutableListOf<Artist>()
                artistsList.forEach { artist ->
                    val result = repository.fetchData(artist)
                    result.let {
                        if (it?.data?.isEmpty() == false) {
                            it.data.let { dataList ->
                                fetchedData.addAll(dataList)
                                dataList.forEach { data ->
                                    when (data.type) {
                                        "album" -> fetchedAlbums.add(data.album)
                                        "artist" -> fetchedArtists.add(data.artist)
                                    }
                                }
                            }
                        } else {
                            Log.e("ViewModel", "Error fetching data for artist: $artist")
                        }
                    }
                }
                _data.value = fetchedData
                _albums.value = fetchedAlbums
                _artists.value = fetchedArtists
                Log.d("ViewModel", "fetchData: Successfully fetched data \n $fetchedData")
                _isLoading.value = false
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ViewModel", "fetchData: Unsuccessfully fetched data", e)
            }
        }
    }



}
