package com.mohsen.apk.wetterkleidung.ui

import android.widget.ImageView
import androidx.lifecycle.*
import com.mohsen.apk.wetterkleidung.model.*
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.utility.DateHelper
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime
import kotlin.math.roundToInt

class MainViewModel(
    private val weatherRepository: WeatherRepository,
    private val dateHelper: DateHelper,
    private val imageHelper: ImageHelper
) : ViewModel() {
    private var selectedDate = ""
    private var seekBarSize = 0
    private lateinit var selectedDaySeekBarValues: List<Int>
    private lateinit var forecast5DaysWeather: Forecast5DaysWeather
    private lateinit var selectedDayWeatherList: List<Forecast5DaysWeatherDetail>

    private val _snackBarError = MutableLiveData<String>()
    private val _cityName = MutableLiveData<String>()
    private val _date = MutableLiveData<String>()
    private val _dayName = MutableLiveData<String>()
    private val _temp = MutableLiveData<Int>()
    private val _tempDesc = MutableLiveData<String>()
    private val _progress = MutableLiveData<Boolean>()
    private val _seekBarChangeProgress = MutableLiveData<Int>()
    private val _weatherImageIconId = MutableLiveData<String>()
    private val _weatherLowInfoList = MutableLiveData<List<WeatherLowInformation>>()
    private val _seekBarTimes = MutableLiveData<List<Int>>()

    val snackBarError: LiveData<String> = _snackBarError
    val cityName: LiveData<String> = _cityName
    val date: LiveData<String> = _date
    val dayName: LiveData<String> = _dayName
    val temp: LiveData<Int> = _temp
    val tempDesc: LiveData<String> = _tempDesc
    val progress: LiveData<Boolean> = _progress
    val seekBarChangeProgress: LiveData<Int> = _seekBarChangeProgress
    val weatherImageIconId: LiveData<String> = _weatherImageIconId
    val weatherLowInfoList: LiveData<List<WeatherLowInformation>> = _weatherLowInfoList
    val seekBarTimes: LiveData<List<Int>> = _seekBarTimes

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
                setSelectedDayWeatherList()
                weatherPresentation()
            }
            is RepositoryResponse.Filure ->
                _snackBarError.value = weather.exception.message
        }
    }

    private fun setSelectedDayWeatherList() {
        forecast5DaysWeather?.let {
            it.weatherList?.let { weatherList ->
                selectedDayWeatherList = weatherList.filter { weather ->
                    weather.dateTimeText.substringBefore(" ") == selectedDate
                }
                seekBarSize = selectedDayWeatherList.size - 1
                setSelectedDaySeekBarValues()
                _seekBarTimes.value = selectedDaySeekBarValues
            }
        }
    }

    private fun setSelectedDaySeekBarValues() {
        selectedDaySeekBarValues = getLastItemsOfSeekBarValues(seekBarSize)
    }

    private fun getLastItemsOfSeekBarValues(count: Int): List<Int> {
        val allSeekBarValues = listOf<Int>(6, 5, 4, 3, 2, 1, 0)
        return allSeekBarValues.subList(0, count + 1).reversed()
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
            val date =
                if (dateHelper.isMorning(daily.date))
                    "Morning"
                else
                    dateHelper.getDayOfWeekFromTimestamp(daily.date).toString().toLowerCase().capitalize()
            val iconId = daily.weatherTitleList[0].icon
            weatherLowInfoList.add(WeatherLowInformation(date, temp, iconId))
        }
        if (weatherLowInfoList.isNotEmpty())
            _weatherLowInfoList.value = weatherLowInfoList
    }

    private fun weatherPresentation(index: Int = 0) {
        _progress.value = false
        _cityName.value = forecast5DaysWeather.city.cityName
        _date.value =
            selectedDayWeatherList[index].dateTimeText.substringBefore(" ")
        _dayName.value = "Today"
        _temp.value =
            selectedDayWeatherList[index].temp?.temp?.roundToInt()
        _tempDesc.value =
            selectedDayWeatherList[index].weatherTitleList[0].description
        _weatherImageIconId.value =
            selectedDayWeatherList[index].weatherTitleList[0].icon
    }

    fun rvSeekBarChangeIndex(progress: Int) {
        weatherPresentation(progress)
    }

    fun weatherIconLoader(ivIcon: ImageView?, imgId: String) {
        ivIcon?.let {
            imageHelper.loadWeatherIcon(ivIcon, imgId)
        }
    }

}