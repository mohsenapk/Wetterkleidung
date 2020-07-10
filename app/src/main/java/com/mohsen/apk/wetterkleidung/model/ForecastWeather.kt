package com.mohsen.apk.wetterkleidung.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "forecast_weather")
data class ForecastWeather(
    @Embedded(prefix = "city_")
    val city: ForecastWeatherCity,
    @SerializedName("cod")
    val code: Int = 0,
    @SerializedName("cnt")
    val forecastCount: Int = 0,
    @SerializedName("list")
    val weatherList: List<ForecastWeatherDetail>? = null
){
    @PrimaryKey(autoGenerate = false)
    var pkid:Int = 0
}

data class ForecastWeatherCity(
    val id: Long = 0,
    @SerializedName("name")
    val cityName: String = "",
    @Embedded(prefix = "location_")
    @SerializedName("coord")
    val location: CityLocation? = null,
    val country: String = "",
    val timeZone: Long = 0
)

data class ForecastWeatherDetail(
    @SerializedName("dt")
    val date: Long = 0,
    val sunrise: Long = 0,
    val sunset: Long = 0,
    @Embedded(prefix = "detail_")
    val temp: ForecastWeatherTemp? = null,
    @SerializedName("feels_like")
    @Embedded(prefix = "detail_")
    val feelsLike: ForecastWeatherFeelsLike? = null,
    val pressure: Int = 0,
    val humidity: Int = 0,
    val speed: Double = 0.0,
    val deg: Int = 0,
    val clouds: Int = 0,
    val rain: Double = 0.0,
    val weatherTitleList : List<WeatherTitle>
)

data class ForecastWeatherTemp(
    val day: Double = 0.0,
    val min: Double = 0.0,
    val max: Double = 0.0,
    val night: Double = 0.0,
    @SerializedName("eve")
    val evening: Double = 0.0,
    @SerializedName("morn")
    val morning: Double = 0.0
)

data class ForecastWeatherFeelsLike(
    val day: Double = 0.0,
    val night: Double = 0.0,
    @SerializedName("eve")
    val evening: Double = 0.0,
    @SerializedName("morn")
    val morning: Double = 0.0
)
