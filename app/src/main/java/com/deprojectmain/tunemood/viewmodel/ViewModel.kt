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

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> get() = _isSearching

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _tracks = MutableStateFlow<List<Data>>(emptyList())
    val tracks: StateFlow<List<Data>> get() = _tracks

    private val _searchData = MutableStateFlow<List<Data>?>(null)
    val searchData: StateFlow<List<Data>?> get() = _searchData

    private val _searchAlbums = MutableStateFlow<List<Album>?>(null)
    val searchAlbums: StateFlow<List<Album>?> get() = _searchAlbums

    private val _searchArtists = MutableStateFlow<List<Artist>?>(null)
    val searchArtists: StateFlow<List<Artist>?> get() = _searchArtists

    private val artistsList = listOf(
        "Lata Mangeshkar",
        "Kishore Kumar",
        "Asha Bhosle",
        "Mohammad Rafi",
        "Mukesh",
        "Jagjit Singh",
        "Hemant Kumar",
        "Manna Dey",
        "Pankaj Udhas",
        "Kumar Sanu",
        "Udit Narayan",
        "Alka Yagnik",
        "Sonu Nigam",
        "Shreya Ghoshal",
        "Arijit Singh",
        "Kailash Kher",
        "Rahat Fateh Ali Khan",
        "Sunidhi Chauhan",
        "Shankar Mahadevan",
        "Vishal Dadlani",
        "Neha Kakkar",
        "Abhijeet Bhattacharya",
        "Kavita Krishnamurthy",
        "S. P. Balasubrahmanyam",
        "Hariharan",
        "K. S. Chithra",
        "Anuradha Paudwal",
        "Sukhwinder Singh",
        "Armaan Malik",
        "Shaan",
        "Benny Dayal",
        "Monali Thakur",
        "Palak Muchhal",
        "Ankit Tiwari",
        "Atif Aslam",
        "Mohit Chauhan",
        "Harshdeep Kaur",
        "Vishal Mishra",
        "Kanika Kapoor",
        "Jubin Nautiyal",
        "Badshah",
        "Neeti Mohan",
        "Prateek Kuhad",
        "Arjun Kanungo",
        "Sanam Puri",
        "Darshan Raval",
        "Akhil Sachdeva",
        "Jonita Gandhi",
        "A. R. Rahman",
        "Papon",
        "Lucky Ali",
        "Shabbir Kumar"
    )

    fun fetchData() {
        viewModelScope.launch {
            try {
                val fetchedData = mutableListOf<Data>()
                val fetchedAlbums = mutableListOf<Album>()
                val fetchedArtists = mutableListOf<Artist>()
                artistsList.forEach { artist ->
                    val result = repository.fetchData(artist)
                    result?.data?.let { dataList ->
                        fetchedData.addAll(dataList)
                        dataList.forEach { data ->
                            when (data.type) {
                                "album" -> if (!fetchedAlbums.contains(data.album)) fetchedAlbums.add(
                                    data.album
                                )

                                "artist" -> if (!fetchedArtists.contains(data.artist)) fetchedArtists.add(
                                    data.artist
                                )
                            }
                        }
                    } ?: Log.e("ViewModel", "Error fetching data for artist: $artist")
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

    fun fetchTracksOfAlbums(artistName: String): List<Data> {
        return data.value?.filter { track -> track.album.title == artistName } ?: emptyList()
    }

    fun fetchTracksOfArtist(artistName: String): List<Data> {
        return data.value?.filter { track -> track.artist.name == artistName } ?: emptyList()
    }

    fun fetchSearchResults(query: String) {
        viewModelScope.launch {
            try {
                _isSearching.value = true
                val response = repository.fetchData(query)
                if (response != null) {
                    val fetchedData = mutableListOf<Data>()
                    val fetchedAlbums = mutableListOf<Album>()
                    val fetchedArtists = mutableListOf<Artist>()
                    response.data.forEach { item ->
                        if (!fetchedAlbums.contains(item.album)) {
                            fetchedAlbums.add(item.album)
                        }
                        if (!fetchedArtists.contains(item.artist)) {
                            fetchedArtists.add(item.artist)
                        }
                        if (!tracks.value.contains(item) && item.type == "track") {
                            fetchedData.add(item)
                        }
                    }

                    _searchData.value = fetchedData
                    _searchAlbums.value = fetchedAlbums
                    _searchArtists.value = fetchedArtists
                    Log.d("Search API", "onResponse: Data loaded successfully")
                    _isSearching.value = false
                } else {
                    Log.d("Search API ", "onResponse: No data received")
                }
            } catch (e: Exception) {
                Log.d("Search API", "onFailure: ${e.message}")
            }
        }
    }
}
