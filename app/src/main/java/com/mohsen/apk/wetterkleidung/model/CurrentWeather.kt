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
    val weatherTitle: List<WeatherTitle>? = null,
    @Embedded(prefix = "temp_")
    @SerializedName("main")
    val currentWeatherTemp: CurrentWeatherTemp? = null,
    @Embedded(prefix = "wind_")
    @SerializedName("wind")
    val wind: Wind? = null,
    @Embedded(prefix = "clouds_")
    @SerializedName("clouds")
    val clouds: Clouds? = null,
    var createdDate: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var pkid = 0
}

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

