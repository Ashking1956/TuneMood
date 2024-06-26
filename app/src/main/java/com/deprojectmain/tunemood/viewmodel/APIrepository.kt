package com.deprojectmain.tunemood.viewmodel

import android.util.Log
import com.deprojectmain.tunemood.data.Album
import com.deprojectmain.tunemood.data.Artist
import com.deprojectmain.tunemood.data.Data
import com.deprojectmain.tunemood.data.MyData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class APIRepository(private val apiInterface: APInterface) {

    suspend fun fetchData(query: String): MyData? {
        return try {
            val response = withContext(Dispatchers.IO) {
                apiInterface.getData(query).execute()
            }
            if (response.isSuccessful) {
                Log.d("fetchData", "fetchData: successful")
                response.body()
            } else {
                Log.d("fetchData", "fetchData: unsuccessful")
                null
            }
        } catch (e: Exception) {
            Log.e("fetchData", "fetchData: failed", e)
            null
        }
    }

    suspend fun fetchArtist(query: String): Artist? {
        return try {
            val response = withContext(Dispatchers.IO) {
                apiInterface.getArtist(query).execute()
            }
            if (response.isSuccessful) {
                Log.d("fetchData", "fetchData: successful")
                response.body()
            } else {
                Log.d("fetchData", "fetchData: unsuccessful")
                null
            }
        } catch (e: Exception) {
            Log.e("fetchData", "fetchData: failed", e)
            null
        }
    }

    suspend fun fetchTrack(query: String): Data? {
        return try {
            val response = withContext(Dispatchers.IO) {
                apiInterface.getTrack(query).execute()
            }
            if (response.isSuccessful) {
                Log.d("fetchData", "fetchData: successful")
                response.body()
            } else {
                Log.d("fetchData", "fetchData: unsuccessful")
                null
            }
        } catch (e: Exception) {
            Log.e("fetchData", "fetchData: failed", e)
            null
        }
    }

    suspend fun fetchAlbum(query: String): Album? {
        return try {
            val response = withContext(Dispatchers.IO) {
                apiInterface.getAlbum(query).execute()
            }
            if (response.isSuccessful) {
                Log.d("fetchData", "fetchData: successful")
                response.body()
            } else {
                Log.d("fetchData", "fetchData: unsuccessful")
                null
            }
        } catch (e: Exception) {
            Log.e("fetchData", "fetchData: failed", e)
            null
        }
    }
}
