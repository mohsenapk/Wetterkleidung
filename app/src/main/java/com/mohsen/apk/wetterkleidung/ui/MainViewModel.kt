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
    private var selectedDate = ""
    private lateinit var forecast5DaysWeather: Forecast5DaysWeather
    private lateinit var forecastWeatherDetailList: List<Forecast5DaysWeatherDetail>

    private val _seekBarMaxSize = MutableLiveData<Int>()
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
    val seekBarMaxSize: LiveData<Int> = _seekBarMaxSize

    fun changeDate(date: String) {
        selectedDate = date
    }

    fun start() = viewModelScope.launch {
        selectedDate = LocalDateTime.now().toString().substringBefore("T")
        forecast5Days("bremen", WeatherUnit.METRIC)
        forecast7DaysForList("bremen", WeatherUnit.METRIC)
    }

    private suspend fun forecast5Days(
        city: String,
        weatherUnit: WeatherUnit
    ) {
        _progress.value = true
        val weather = weatherRepository
            .getForecast5DaysWeather(city, weatherUnit)
        when (weather) {
            is RepositoryResponse.Success -> {
                forecast5DaysWeather = weather.data
                setForecastWeatherDetails()
                weatherPresentation()
            }
            is RepositoryResponse.Filure ->
                _snackBarError.value = weather.exception.message
        }
    }

    private fun setForecastWeatherDetails() {
        forecast5DaysWeather?.let {
            it.weatherList?.let { weatherList ->
                forecastWeatherDetailList = weatherList.filter { weather ->
                    weather.dateTimeText.substringBefore(" ") == selectedDate
                }
                _seekBarMaxSize.value = forecastWeatherDetailList.size
            }
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
            val temp = daily.temp?.day?.roundToInt().toString()
            val date = daily.date.toString()

            weatherLowInfoList.add(WeatherLowInformation(date, temp, ""))
        }
        if (weatherLowInfoList.isNotEmpty())
            _weatherLowInfoList.value = weatherLowInfoList
    }

    private fun weatherPresentation(index: Int = 0) {
//        if(forecastWeatherDetaileList.size < index)
//            return
        _progress.value = false
        _cityName.value = forecast5DaysWeather.city.cityName
        _date.value =
            forecastWeatherDetailList[index].dateTimeText.substringBefore(" ")
        _dayName.value = "Today"
        _temp.value =
            forecastWeatherDetailList[index].temp?.temp?.roundToInt()
        _tempDesc.value =
            forecastWeatherDetailList[index].weatherTitleList[0].description
        _weatherImageIconId.value =
            forecastWeatherDetailList[index].weatherTitleList[0].icon
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
        weatherPresentation(progress)
    }

    fun onResume() {

    }

    fun weatherImageIconWithId(ivIcon: ImageView?, imgId: String) {
        ivIcon?.let {
            //todo no no no - view send to repository?????what???
            weatherRepository.loadImageIcon(ivIcon, imgId)
        }
    }

}