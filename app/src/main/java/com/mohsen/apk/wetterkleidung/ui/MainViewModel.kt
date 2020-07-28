package com.mohsen.apk.wetterkleidung.ui

import android.widget.ImageView
import androidx.lifecycle.*
import com.mohsen.apk.wetterkleidung.model.*
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import timber.log.Timber
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
    private val _seekBarTimeShow = MutableLiveData<String>()
    private val _seekBarChangeProgress = MutableLiveData<Int>()
    private val _weatherImageIconId = MutableLiveData<String>()
    private val _weatherLowInfoList = MutableLiveData<List<WeatherLowInformation>>()

    val snackBarError: LiveData<String> = _snackBarError
    val cityName: LiveData<String> = _cityName
    val date: LiveData<String> = _date
    val dayName: LiveData<String> = _dayName
    val temp: LiveData<Int> = _temp
    val tempDesc: LiveData<String> = _tempDesc
    val progress: LiveData<Boolean> = _progress
    val seekBarTimeShow: LiveData<String> = _seekBarTimeShow
    val seekBarChangeProgress: LiveData<Int> = _seekBarChangeProgress
    val weatherImageIconId: LiveData<String> = _weatherImageIconId
    val weatherLowInfoList: LiveData<List<WeatherLowInformation>> = _weatherLowInfoList

    private fun refreshWeather(
        timePeriodIndex: Int = 0
    ) = viewModelScope.launch {
        forecast5Days("bremen", WeatherUnit.METRIC, timePeriodIndex)
        forecast7DaysForList("bremen", WeatherUnit.METRIC)
    }

    private suspend fun forecast5Days(
        city: String,
        weatherUnit: WeatherUnit,
        timePeriodIndex: Int
    ) {
        _progress.value = true
        val weather = weatherRepository
            .getForecast5DaysWeather(city, weatherUnit)
        when (weather) {
            is RepositoryResponse.Success -> {
                getCorrectWeatherWithIndex(weather.data, timePeriodIndex)
            }
            is RepositoryResponse.Filure ->
                _snackBarError.value = weather.exception.message
        }
    }

    private suspend fun forecast7DaysForList(
        city: String,
        weatherUnit: WeatherUnit
    ) {
        val forecastWeather =
            weatherRepository.getForecastWeather(city, weatherUnit)
        when (forecastWeather) {
            is RepositoryResponse.Success -> {
                if (forecastWeather.data.weatherList != null)
                    getForecastWeatherForList(forecastWeather.data.weatherList)
            }
            is RepositoryResponse.Filure -> null
        }
    }

    private fun getForecastWeatherForList(list: List<ForecastWeatherDetail>) {
        val weatherLowInfoList = mutableListOf<WeatherLowInformation>()
        if (list.size < 6)
            return
        for (i in 1..5) {
            val daily = list[i]
            weatherLowInfoList.add(
                WeatherLowInformation(
                    date = daily.date.toString(),
                    temp = daily.temp?.day ?: 0.0,
                    iconId = daily.weatherTitleList?.get(0).icon
                )
            )
        }
        if (weatherLowInfoList.isNotEmpty())
            _weatherLowInfoList.value = weatherLowInfoList
    }

    private fun getCorrectWeatherWithIndex(weather: Forecast5DaysWeather, timePeriodIndex: Int) {
        _progress.value = false
        _cityName.value = weather.city.cityName
        _date.value =
            weather.weatherList?.get(timePeriodIndex)?.dateTimeText.toString().substringBefore(" ")
        _dayName.value = "Today"
        _temp.value =
            weather.weatherList?.get(timePeriodIndex)?.temp?.temp?.roundToInt()
        _tempDesc.value =
            weather.weatherList?.get(timePeriodIndex)?.weatherTitleList?.get(0)?.description
        _weatherImageIconId.value =
            weather.weatherList?.get(timePeriodIndex)?.weatherTitleList?.get(0)?.icon
    }

    private fun loadWeatherIconImage(imageView: ImageView, iconId: String?) {
        iconId?.let { weatherRepository.loadImageIcon(imageView, iconId) }
    }

    fun seekBarProgressChange(progress: Int) {
        var lastSeekBarValue: SeekBarValue = SeekBarValue.ONE
        Timber.d("seek - onProgressChange - > $progress")
        when (progress) {
            0 -> lastSeekBarValue = SeekBarValue.ZERO
            1 -> lastSeekBarValue = SeekBarValue.ONE
            2 -> lastSeekBarValue = SeekBarValue.TWO
            3 -> lastSeekBarValue = SeekBarValue.THREE
            4 -> lastSeekBarValue = SeekBarValue.FOUR
            5 -> lastSeekBarValue = SeekBarValue.FIVE
            6 -> lastSeekBarValue = SeekBarValue.SIX
            7 -> lastSeekBarValue = SeekBarValue.SEVEN
        }
        _seekBarTimeShow.value = lastSeekBarValue.hours
        refreshWeather(progress)
    }

    private fun seekBarSetupFirstTime() {
        var progress = 0
        when (LocalDateTime.now().hour) {
            6, 7, 8 -> progress = 0
            9, 10, 11 -> progress = 1
            12, 13, 14 -> progress = 2
            15, 16, 17 -> progress = 3
            18, 19, 20 -> progress = 4
            21, 22, 23 -> progress = 5
            0, 1, 2 -> progress = 6
            3, 4, 5 -> progress = 7
        }
        _seekBarChangeProgress.value = progress
    }

    fun onResume() {
        seekBarSetupFirstTime()
    }

    fun weatherImageIconWithId(ivIcon: ImageView?, imgId: String) {
        ivIcon?.let {
            //todo no no no - view send to repository?????what???
            weatherRepository.loadImageIcon(ivIcon, imgId)
        }
    }

}