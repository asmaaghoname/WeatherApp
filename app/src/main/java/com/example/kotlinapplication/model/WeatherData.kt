package com.example.kotlinapplication.model

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose



data class Current ( @SerializedName("dt") @Expose var dt: Int? ,
                     @SerializedName("sunrise") @Expose var sunrise: Int? ,
                     @SerializedName("sunset") @Expose var sunset: Int?,
                     @SerializedName("temp") @Expose var temp: Double?,
                     @SerializedName("feels_like") @Expose var feelsLike: Double? ,
                     @SerializedName("pressure") @Expose var pressure: Int?,
                     @SerializedName("humidity") @Expose var humidity: Int? ,
                     @SerializedName("dew_point") @Expose var dewPoint: Double? ,
                     @SerializedName("uvi") @Expose var uvi: Double? ,
                     @SerializedName("clouds") @Expose var clouds: Int? ,
                     @SerializedName("visibility") @Expose var visibility: Int?,
                     @SerializedName("wind_speed") @Expose var windSpeed: Double?,
                     @SerializedName("wind_deg") @Expose var windDeg: Int?,
                     @SerializedName("wind_gust") @Expose var windGust: Double?,
                     @SerializedName("weather") @Expose var weather: List<Weather>?) {}

data class Daily ( @SerializedName("dt") @Expose var dt: Int?,
                   @SerializedName("sunrise") @Expose var sunrise: Int? ,
                   @SerializedName("sunset") @Expose var sunset: Int? ,
                   @SerializedName("temp") @Expose var temp: Temp? ,
                   @SerializedName("feels_like") @Expose var feelsLike: FeelsLike? ,
                   @SerializedName("pressure") @Expose var pressure: Int? ,
                   @SerializedName("humidity") @Expose var humidity: Int? ,
                   @SerializedName("dew_point") @Expose var dewPoint: Double?,
                   @SerializedName("wind_speed") @Expose var windSpeed: Double?,
                   @SerializedName("wind_deg") @Expose var windDeg: Int? ,
                   @SerializedName("weather") @Expose var weather: List<Weather__>? ,
                   @SerializedName("clouds") @Expose var clouds: Int? ,
                   @SerializedName("pop") @Expose var pop: Double? ,
                   @SerializedName("rain") @Expose var rain: Double?,
                   @SerializedName("uvi") @Expose var uvi: Double?)

data class FeelsLike (  @SerializedName("day") @Expose var day: Double?,
                        @SerializedName("night") @Expose var night: Double?,
                        @SerializedName("eve") @Expose var eve: Double?,
                        @SerializedName("morn") @Expose var morn: Double?)

data class Hourly(   @SerializedName("dt") @Expose var dt: Int?,
                     @SerializedName("temp") @Expose var temp: Double?,
                     @SerializedName("feels_like") @Expose var feelsLike: Double? ,
                     @SerializedName("pressure") @Expose var pressure: Int? ,
                     @SerializedName("humidity") @Expose var humidity: Int?,
                     @SerializedName("dew_point") @Expose var dewPoint: Double? ,
                     @SerializedName("uvi") @Expose var uvi: Double?,
                     @SerializedName("clouds") @Expose var clouds: Int? ,
                     @SerializedName("visibility") @Expose var visibility: Int? ,
                     @SerializedName("wind_speed") @Expose var windSpeed: Double?,
                     @SerializedName("wind_deg") @Expose var windDeg: Int?,
                     @SerializedName("weather") @Expose var weather: List<Weather_>? ,
                     @SerializedName("pop") @Expose var pop: Double?,
                     @SerializedName("rain") @Expose var rain: Rain?)

data class Minutely (    @SerializedName("dt") @Expose var dt: Int?,
                         @SerializedName("precipitation") @Expose var precipitation: Double? )

data class Rain (@SerializedName("1h") @Expose private var _1h: Double?){
    fun get1h(): Double? {
        return _1h
    }
    fun set1h(_1h: Double?) {
        this._1h = _1h
    }
}

data class Temp(  @SerializedName("day") @Expose var day: Double? ,
                  @SerializedName("min") @Expose var min: Double?,
                  @SerializedName("max") @Expose var max: Double? ,
                  @SerializedName("night") @Expose var night: Double? ,
                  @SerializedName("eve") @Expose var eve: Double? ,
                  @SerializedName("morn") @Expose var morn: Double?)
data class Weather (    @SerializedName("id") @Expose var id: Int? ,
                        @SerializedName("main") @Expose var main: String? ,
                        @SerializedName("description") @Expose var description: String? ,
                        @SerializedName("icon") @Expose var icon: String?)

data class WeatherData( @SerializedName("lat") @Expose var lat: Double?,
                             @SerializedName("lon") @Expose var lon: Double? ,
                             @SerializedName("timezone") @Expose var timezone: String?,
                             @SerializedName("timezone_offset")@Expose var timezoneOffset: Int? ,
                             @SerializedName("current") @Expose var current: Current? ,
                             @SerializedName("minutely") @Expose var minutely: List<Minutely>? ,
                             @SerializedName("hourly") @Expose var hourly: List<Hourly>? ,
                             @SerializedName("daily") @Expose var daily: List<Daily>?,
                             @SerializedName("alerts") @Expose var alert: List<Alert>?)

data class Weather_(    @SerializedName("id") @Expose var id: Int? ,
                        @SerializedName("main") @Expose var main: String? ,
                        @SerializedName("description") @Expose var description: String? ,
                        @SerializedName("icon") @Expose var icon: String? )

data class Weather__ ( @SerializedName("id") @Expose var id: Int? ,
                       @SerializedName("main") @Expose var main: String? ,
                       @SerializedName("description") @Expose var description: String? ,
                       @SerializedName("icon") @Expose var icon: String? )

data class Alert( @SerializedName("sender_name") @Expose var senderName: String? ,
                  @SerializedName("event") @Expose var event: String?,
                  @SerializedName("start") @Expose var start: Int?,
                  @SerializedName("end") @Expose var end: Int?,
                  @SerializedName("description") @Expose var description: String?)