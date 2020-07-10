package com.mohsen.apk.wetterkleidung.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "current_weather")
data class CurrentWeather(
    val id: Long = 0,
    @SerializedName("cod")
    val code: Int = 0,
    @SerializedName("dt")
    val date: Long = 0,
    @SerializedName("name")
    val cityName: String = "",
    @Embedded(prefix = "location_")
    @SerializedName("coord")
    val location: CityLocation? = null,
    @SerializedName("weather")
    val currentWeatherTitle: List<CurrentWeatherTitle>? = null,
    @Embedded(prefix = "temp_")
    @SerializedName("main")
    val currentWeatherTemp: CurrentWeatherTemp? = null,
    @Embedded(prefix = "wind_")
    @SerializedName("wind")
    val currentWeatherWind: CurrentWeatherWind? = null,
    @Embedded(prefix = "clouds_")
    @SerializedName("clouds")
    val currentWeatherClouds: CurrentWeatherClouds? = null
) {
    @PrimaryKey(autoGenerate = false)
    var pkid = 0
}

data class CurrentWeatherClouds(
    @SerializedName("all")
    val cloudCount: Int = 0
)

data class CurrentWeatherWind(
    val speed: Double = 0.0,
    @SerializedName("deg")
    val degree: Int = 0
)

data class CurrentWeatherTemp(
    val temp: Double = 0.0,
    @SerializedName("feels_like")
    val feelsLike: Double = 0.0,
    @SerializedName("temp_min")
    val minTemp: Double = 0.0,
    @SerializedName("temp_max")
    val maxTemp: Double = 0.0,
    val pressure: Double = 0.0,
    val humidity: Double = 0.0
)

data class CurrentWeatherTitle(
    val id: Int = 0,
    @SerializedName("main")
    val title: String = "",
    val description: String = "",
    val icon: String = ""
)

data class CityLocation(
    val lon: Double = 0.0,
    val lat: Double = 0.0
)
