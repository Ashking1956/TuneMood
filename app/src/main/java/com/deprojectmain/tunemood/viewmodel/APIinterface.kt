package com.deprojectmain.tunemood.viewmodel

import com.deprojectmain.tunemood.data.MyData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIinterface {
    @Headers(
        "x-rapidapi-key: c2bf61b42bmshe11c581d7572f9ap1ad486jsn089a23d5d10d",
        "x-rapidapi-host: deezerdevs-deezer.p.rapidapi.com"
    )

    @GET("search")
    fun getData(@Query("q") query: String): Call<MyData>

}
