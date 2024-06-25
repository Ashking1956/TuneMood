package com.deprojectmain.tunemood.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.deprojectmain.tunemood.data.Data
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {

    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(APIinterface::class.java)

    private val artistsList = listOf(
        "Olivia Rodrigo", "Lil Nas X", "Coldplay", "Jason Aldean", "Luke Combs", "Miley Cyrus",
        "Billie Eilish", "Doja Cat", "Ed Sheeran", "Ariana Grande", "Travis Scott",
        "Taylor Swift", "Justin Bieber", "Dua Lipa", "BTS", "The Weeknd", "Drake",
        "Harry Styles", "Post Malone", "Bad Bunny", "Megan Thee Stallion", "Kanye West",
        "Shawn Mendes", "Cardi B", "Lizzo", "SZA", "Bruno Mars", "Adele", "Chris Stapleton"
    )

    suspend fun collectDataFromAPI(): List<Data> {
        val dataList = mutableListOf<Data>()
        try {
            withContext(Dispatchers.IO) {
                artistsList.forEach { artist ->
                    val response = retrofitBuilder.getData(artist).execute()
                    if (response.isSuccessful) {
                        val responseData = response.body()?.data
                        responseData?.let {
                            dataList.addAll(it)
                        }
                        Log.d("API Working", "onResponse: Data loaded successfully")
                    } else {
                        Log.d("API Error", "onResponse: ${response.errorBody()}")
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("API Error", "onFailure: ${e.message}")
        }
        return dataList
    }
}
