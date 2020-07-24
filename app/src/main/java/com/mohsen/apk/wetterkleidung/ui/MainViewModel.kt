package com.mohsen.apk.wetterkleidung.ui

import android.widget.ImageView
import androidx.lifecycle.*
import com.mohsen.apk.wetterkleidung.base.BaseApplication
import com.mohsen.apk.wetterkleidung.model.*
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import com.mohsen.apk.wetterkleidung.utility.ImageHelperImpl
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

class MainViewModel(
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _snackBarError = MutableLiveData<String>()
    private val _cityName = MutableLiveData<String>()
    private val _date = MutableLiveData<String>()
    private val _dayName = MutableLiveData<String>()
    private val _temp = MutableLiveData<Int>()
    private val _tempDesc = MutableLiveData<String>()
    private val _progress = MutableLiveData<Boolean>()

    val snackBarError: LiveData<String> = _snackBarError
    val cityName: LiveData<String> = _cityName
    val date: LiveData<String> = _date
    val dayName: LiveData<String> = _dayName
    val temp: LiveData<Int> = _temp
    val tempDesc: LiveData<String> = _tempDesc
    val progress: LiveData<Boolean> = _progress

    fun refreshWeather(
        time: Int = 0,
        imageView: ImageView? = null
    ) = viewModelScope.launch {
        _progress.value = true
        val weather = weatherRepository
            .getForecast5DaysWeather("bremen", WeatherUnit.METRIC)

        when (weather) {
            is RepositoryResponse.Success -> {
                if (imageView != null)
                    loadWeatherIconImage(
                        imageView,
                        weather.data.weatherList?.get(0)?.weatherTitleList?.get(0)?.icon
                    )
                setWeatherToLiveData(weather.data)
            }
            is RepositoryResponse.Filure ->
                _snackBarError.value = weather.exception.message
        }
    }

    private fun setWeatherToLiveData(weather: Forecast5DaysWeather) {
        _progress.value = false
        _cityName.value = weather.city.cityName
        _date.value = weather.weatherList?.get(0)?.dateTimeText.toString().substringBefore(" ")
        _dayName.value = "Today"
        _temp.value = weather.weatherList?.get(0)?.temp?.temp?.roundToInt()
        _tempDesc.value = weather.weatherList?.get(0)?.weatherTitleList?.get(0)?.description
    }

    private fun loadWeatherIconImage(imageView: ImageView, iconId: String?) {
        iconId?.let { weatherRepository.loadImageIcon(imageView, iconId) }
    }

}