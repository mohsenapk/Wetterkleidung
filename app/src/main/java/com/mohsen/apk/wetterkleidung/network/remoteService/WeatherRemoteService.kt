package com.mohsen.apk.wetterkleidung.network.remoteService

import com.mohsen.apk.wetterkleidung.internal.CityNotFoundException
import com.mohsen.apk.wetterkleidung.internal.GeneralApiException
import com.mohsen.apk.wetterkleidung.internal.NoInternetConnectionException
import com.mohsen.apk.wetterkleidung.model.*
import com.mohsen.apk.wetterkleidung.network.apiService.WeatherApi
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.Exception
import kotlin.coroutines.RestrictsSuspension

interface WeatherRemoteService {
    suspend fun getCurrentWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<CurrentWeather>

    suspend fun getCurrentWeatherWithLatAndLon(
        lat: String,
        lon: String,
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
}

class WeatherRemoteServiceImpl(private val retrofit: Retrofit) : WeatherRemoteService {

    override suspend fun getCurrentWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<CurrentWeather> = callAPI {
        retrofit.create(WeatherApi::class.java)
            .getCurrentWeather(city, weatherUnit.name)
    }

    override suspend fun getCurrentWeatherWithLatAndLon(
        lat: String,
        lon: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<CurrentWeather> =
        callAPI {
            retrofit.create(WeatherApi::class.java)
                .getCurrentWeatherWithLatAndLon(lat, lon, weatherUnit.name)
        }

    override suspend fun getForecastWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<ForecastWeather> =
        callAPI {
            retrofit.create(WeatherApi::class.java)
                .getForecastWeather(city, weatherUnit.name)
        }

    override suspend fun getForecast5DaysWeather(
        city: String,
        weatherUnit: WeatherUnit
    ): RepositoryResponse<Forecast5DaysWeather> =
        callAPI {
            retrofit.create(WeatherApi::class.java)
                .getForecast5DaysWeather(city, weatherUnit.name)
        }

    private suspend fun <T> callAPI(
        retrofitService: suspend () -> Response<T>
    ): RepositoryResponse<T> {
        return try {
            createRepositoryResponse(retrofitService.invoke())
        } catch (e: Exception) {
            RepositoryResponse.Failure(e)
        }
    }

    private fun <T> createRepositoryResponse(response: Response<T>): RepositoryResponse<T> {
        return if (response.isSuccessful)
            RepositoryResponse.Success(response.body() as T)
        else
            RepositoryResponse.Failure(handleHTTPCode(response.raw().code))
    }

    private fun handleHTTPCode(code: Int): IOException {
        return when (code) {
            404 -> CityNotFoundException()
            else -> GeneralApiException()
        }
    }

}
