package com.mohsen.apk.wetterkleidung.repository

import android.widget.ImageView
import com.mohsen.apk.wetterkleidung.db.localService.WeatherLocalService
import com.mohsen.apk.wetterkleidung.model.*
import com.mohsen.apk.wetterkleidung.network.remoteService.WeatherRemoteService
import com.mohsen.apk.wetterkleidung.utility.DateHelper
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import kotlinx.coroutines.*
import okio.IOException
import org.threeten.bp.LocalDateTime
import java.lang.Exception

interface WeatherRepository {
    suspend fun getCurrentWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<CurrentWeather>

    suspend fun getForecastWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<ForecastWeather>

    suspend fun getForecast5DaysWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<Forecast5DaysWeather>

    fun loadImageIcon(imageView: ImageView, iconId: String)
}

class WeatherRepositoryImpl(
    private val remote: WeatherRemoteService,
    private val local: WeatherLocalService,
    private val dateHelper: DateHelper,
    private val imageHelper: ImageHelper
) : WeatherRepository {

    override suspend fun getCurrentWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<CurrentWeather> = coroutineScope {
        val data = getCurrentWeatherLocal()
        data ?: getCurrentWeatherRemote(city, weatherUnit)
    }

    private suspend fun getCurrentWeatherLocal(): RepositoryResponse<CurrentWeather>? =
        coroutineScope {
            val localData: CurrentWeather? =
                async(Dispatchers.IO) { local.getCurrentWeather() }.await()
            val createdDate = localData?.createdDate
            if (createdDate != null && !dateHelper.isDateExpired(LocalDateTime.parse(createdDate)))
                RepositoryResponse.Success(localData)
            else
                null
        }

    private suspend fun getCurrentWeatherRemote(
        city: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<CurrentWeather> = coroutineScope {
        try {
            val remoteData = remote.getCurrentWeather(city, weatherUnit)
            launch(Dispatchers.IO) { local.setCurrentWeather(remoteData) }.join()
            RepositoryResponse.Success(remoteData)
        } catch (e: IOException) {
            RepositoryResponse.Filure(e)
        }
    }

    override suspend fun getForecastWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<ForecastWeather> = coroutineScope {
        val data = getForecastWeatherLocal()
        data ?: getForecastWeatherRemote(city, weatherUnit)
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
        city: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<ForecastWeather> = coroutineScope {
        try {
            val remoteData = remote.getForecastWeather(city, weatherUnit)
            launch(Dispatchers.IO) { local.setForecastWeather(remoteData) }
            RepositoryResponse.Success(remoteData)
        } catch (e: Exception) {
            RepositoryResponse.Filure(e)
        }
    }

    override suspend fun getForecast5DaysWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<Forecast5DaysWeather> = coroutineScope {
        val data = getForecast5DaysWeatherLocal()
        data ?: getForecast5DaysWeatherRemote(city, weatherUnit)
    }

    private suspend fun getForecast5DaysWeatherLocal(): RepositoryResponse<Forecast5DaysWeather>? =
        coroutineScope {
            val localData =
                async(Dispatchers.IO) { local.getForecast5DaysWeather() }.await()
            val createDate = localData?.createdDate
            if (createDate != null && dateHelper.isDateExpired(LocalDateTime.parse(createDate)))
                RepositoryResponse.Success(localData)
            else
                null
        }

    private suspend fun getForecast5DaysWeatherRemote(
        city: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<Forecast5DaysWeather> = coroutineScope {
        try {
            val remoteData = remote.getForecast5DaysWeather(city, weatherUnit)
            launch(Dispatchers.IO) { local.setForecast5DaysWeather(remoteData) }
            RepositoryResponse.Success(remoteData)
        } catch (e: IOException) {
            RepositoryResponse.Filure(e)
        }
    }

    override fun loadImageIcon(imageView: ImageView, iconId: String) {
        imageHelper.loadWeatherIcon(imageView, iconId)
    }
}