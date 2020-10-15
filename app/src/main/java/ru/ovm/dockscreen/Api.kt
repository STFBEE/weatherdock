package ru.ovm.dockscreen

import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") q: String,
        @Query("appid") appid: String,
        @Query("lang") lang: String
    ): Response?
}

data class Response(
    val coord: Coordinates,
    val weather: List<Weather>,
    val main:Main
)

data class Coordinates(
    val lon: Double, val lat: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Double,
    val humidity: Double,
)

