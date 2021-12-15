package com.afaf.headsupgame

import retrofit2.Call
import retrofit2.http.*

interface APIInterface {



    @GET("/celebrities/")
    fun getCelebs(): Call<Celeb?>?

}