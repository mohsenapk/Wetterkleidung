package com.mohsen.apk.wetterkleidung.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "forecast_5days_weather")
data class Forecast5DaysWeather(
    @Embedded(prefix = "city_")
    val city: Forecast5DaysWeatherCity,
    @SerializedName("cod")
    val code: Int = 0,
    @SerializedName("cnt")
    val forecastCount: Int = 0,
    @SerializedName("list")
    val weatherList: List<Forecast5DaysWeatherDetail>? = null,
    var createdDate: String? = null
) {
    @PrimaryKey(autoGenerate = false)
    var pkid: Int = 0
}

data class Forecast5DaysWeatherCity(
    val id: Long = 0,
    @SerializedName("name")
    val cityName: String = "",
    @Embedded(prefix = "location_")
    @SerializedName("coord")
    val location: CityLocation? = null,
    val country: String = "",
    @SerializedName("timezone")
    val timeZone: Long = 0,
    val sunrise: Long = 0,
    val sunset: Long = 0
)

data class Forecast5DaysWeatherDetail(
    @SerializedName("dt")
    val date: Long = 0,
    @Embedded(prefix = "detail_")
    @SerializedName("main")
    val temp: Forecast5DaysWeatherTemp? = null,
    val weatherTitleList: List<WeatherTitle>,
    @Embedded(prefix = "clouds_")
    val clouds: Clouds? = null,
    @Embedded(prefix = "wind_")
    val wind: Wind? = null,
    @Embedded(prefix = "rain_")
    val rain: Forecast5DaysWeatherRain? = null,
    @SerializedName("dt_txt")
    val dateTimeText: String = ""
)

data class Forecast5DaysWeatherTemp(
    val temp: Double = 0.0,
    val feels_like: Double = 0.0,
    val temp_min: Double = 0.0,
    val temp_max: Double = 0.0,
    val pressure: Double = 0.0,
    val sea_level: Double = 0.0,
    val grnd_level: Double = 0.0,
    val humidity: Double = 0.0,
    val temp_kf: Double = 0.0
)

data class Forecast5DaysWeatherRain(
    @SerializedName("3h")
    val ThreeH: Double = 0.0
)
