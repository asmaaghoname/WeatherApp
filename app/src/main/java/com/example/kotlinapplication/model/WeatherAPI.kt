package com.example.kotlinapplication.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("data/2.5/onecall?")
    suspend fun getWeatherForecast(@Query("lat")lat:Double,
                                   @Query("lon")lon:Double,
                                   @Query("appid")appid:String,
                                   @Query("units")units:String,
                                   @Query("lang")lang:String,
                                   @Query("exclude")exclude:String
    ): Response<WeatherData>
}