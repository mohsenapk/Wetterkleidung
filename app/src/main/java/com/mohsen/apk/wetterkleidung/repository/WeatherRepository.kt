package com.mohsen.apk.wetterkleidung.repository

import com.mohsen.apk.wetterkleidung.db.localService.WeatherLocalService
import com.mohsen.apk.wetterkleidung.model.*
import com.mohsen.apk.wetterkleidung.network.remoteService.WeatherRemoteService
import com.mohsen.apk.wetterkleidung.utility.DateHelper
import kotlinx.coroutines.*
import org.threeten.bp.LocalDateTime

interface WeatherRepository {
    suspend fun getCurrentWeather(
        cityName: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<CurrentWeather>

    suspend fun getCurrentWeatherWithLatAndLon(
        lat: Double,
        lon: Double,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<CurrentWeather>

    suspend fun getForecastWeather7DaysAVG(
        cityName: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<ForecastWeather>

    suspend fun getForecastWeather5DaysHourly(
        cityName: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<Forecast5DaysWeather>
}

class WeatherRepositoryImpl(
    private val remote: WeatherRemoteService,
    private val local: WeatherLocalService,
    private val dateHelper: DateHelper
) : WeatherRepository {

    override suspend fun getCurrentWeather(
        cityName: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<CurrentWeather> = coroutineScope {
        val data = getCurrentWeatherLocal(cityName)
        data ?: getCurrentWeatherRemote(cityName, weatherUnit)
    }

    override suspend fun getCurrentWeatherWithLatAndLon(
        lat: Double,
        lon: Double,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<CurrentWeather> =
        remote.getCurrentWeatherWithLatAndLon(lat.toString(), lon.toString(), weatherUnit)


    private suspend fun getCurrentWeatherLocal(cityName: String): RepositoryResponse<CurrentWeather>? =
        coroutineScope {
            val localData: CurrentWeather? =
                async(Dispatchers.IO) { local.getCurrentWeather(cityName) }.await()
            val createdDate = localData?.createdDate
            if (createdDate != null && !dateHelper.isDateExpired(LocalDateTime.parse(createdDate)))
                RepositoryResponse.Success(localData)
            else
                null
        }

    private suspend fun getCurrentWeatherRemote(
        cityName: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<CurrentWeather> = coroutineScope {
        val remoteData = remote.getCurrentWeather(cityName, weatherUnit)
        if (remoteData is RepositoryResponse.Success)
            launch(Dispatchers.IO) { local.setCurrentWeather(remoteData.data) }
        remoteData
    }

    override suspend fun getForecastWeather7DaysAVG(
        cityName: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<ForecastWeather> = coroutineScope {
        val data = getForecastWeatherLocal()
        data ?: getForecastWeatherRemote(cityName, weatherUnit)
    }

    private suspend fun getForecastWeatherLocal(): RepositoryResponse<ForecastWeather>? =
        coroutineScope {
            val localData: ForecastWeather? =
                async(Dispatchers.IO) { local.getForecastWeather() }.await()
            val createdDate = localData?.createdDate
            if (createdDate != null && !dateHelper.isDateExpired(LocalDateTime.parse(createdDate)))
                RepositoryResponse.Success(localData)
            else
                null
        }

    private suspend fun getForecastWeatherRemote(
        cityName: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<ForecastWeather> = coroutineScope {
        val remoteData = remote.getForecastWeather(cityName, weatherUnit)
        if (remoteData is RepositoryResponse.Success)
            launch(Dispatchers.IO) { local.setForecastWeather(remoteData.data) }
        remoteData
    }

    override suspend fun getForecastWeather5DaysHourly(
        cityName: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<Forecast5DaysWeather> = coroutineScope {
        val data = getForecast5DaysWeatherLocal()
        data ?: getForecast5DaysWeatherRemote(cityName, weatherUnit)
    }

    private suspend fun getForecast5DaysWeatherLocal(): RepositoryResponse<Forecast5DaysWeather>? =
        coroutineScope {
            val localData =
                async(Dispatchers.IO) { local.getForecast5DaysWeather() }.await()
            val createDate = localData?.createdDate
            if (createDate != null && !dateHelper.isDateExpired(LocalDateTime.parse(createDate)))
                RepositoryResponse.Success(localData)
            else
                null
        }

    private suspend fun getForecast5DaysWeatherRemote(
        cityName: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<Forecast5DaysWeather> = coroutineScope {
        val remoteData = remote.getForecast5DaysWeather(cityName, weatherUnit)
        if (remoteData is RepositoryResponse.Success)
            launch(Dispatchers.IO) { local.setForecast5DaysWeather(remoteData.data) }
        remoteData
    }

}