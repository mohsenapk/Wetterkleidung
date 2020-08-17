package com.mohsen.apk.wetterkleidung.ui.main

import android.preference.PreferenceManager
import android.widget.ImageView
import androidx.lifecycle.*
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
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
    private val imageHelper: ImageHelper,
    private val prefs: SharedPreferenceManager
) : ViewModel() {

    private lateinit var selectedDayWeatherList: List<Forecast5DaysWeatherDetail>
    private var forecast5DaysWeather: Forecast5DaysWeather? = null

    private val _snackBarError = MutableLiveData<String>()
    private val _cityName = MutableLiveData<String>()
    private val _date = MutableLiveData<String>()
    private val _wind = MutableLiveData<String>()
    private val _clouds = MutableLiveData<String>()
    private val _humidity = MutableLiveData<String>()
    private val _dayName = MutableLiveData<String>()
    private val _temp = MutableLiveData<Int>()
    private val _tempDesc = MutableLiveData<String>()
    private val _progress = MutableLiveData<Boolean>()
    private val _weatherImageIconId = MutableLiveData<String>()
    private val _weatherLowInfoList = MutableLiveData<List<WeatherLowInformation>>()
    private val _seekBarTimes = MutableLiveData<List<Int>>()
    private val _goToCityActivity = MutableLiveData<Unit>()

    val snackBarError: LiveData<String> = _snackBarError
    val cityName: LiveData<String> = _cityName
    val date: LiveData<String> = _date
    val humidity: LiveData<String> = _humidity
    val clouds: LiveData<String> = _clouds
    val wind: LiveData<String> = _wind
    val dayName: LiveData<String> = _dayName
    val temp: LiveData<Int> = _temp
    val tempDesc: LiveData<String> = _tempDesc
    val progress: LiveData<Boolean> = _progress
    val weatherImageIconId: LiveData<String> = _weatherImageIconId
    val weatherLowInfoList: LiveData<List<WeatherLowInformation>> = _weatherLowInfoList
    val seekBarTimes: LiveData<List<Int>> = _seekBarTimes
    val goToCityActivity: LiveData<Unit> = _goToCityActivity

    fun start() = viewModelScope.launch {
        val defaultCity = prefs.getCityDefault()
        if (defaultCity.isNotEmpty()) {
            forecastWeather5DaysHourly(defaultCity, WeatherUnit.METRIC)
            forecastWeather5DaysAVG(defaultCity, WeatherUnit.METRIC)
            dateChanged(LocalDateTime.now())
        } else
            _goToCityActivity.value = Unit
    }

    fun onResume() {
        val defaultCity = prefs.getCityDefault()
        if (defaultCity.toUpperCase() != forecast5DaysWeather?.city?.cityName?.toUpperCase())
            start()
    }

    private suspend fun forecastWeather5DaysHourly(
        city: String,
        weatherUnit: WeatherUnit
    ) {
        _progress.value = true
        val weather = weatherRepository
            .getForecastWeather5DaysHourly(city, weatherUnit)
        when (weather) {
            is RepositoryResponse.Success -> {
                forecast5DaysWeather = weather.data
            }
            is RepositoryResponse.Failure ->
                _snackBarError.value = weather.exception.message
        }
    }

    fun dateChanged(date: LocalDateTime) {
        _dayName.value = getDayName(date)
        changeCurrentWeatherList(date)
    }

    private fun changeCurrentWeatherList(date: LocalDateTime) {
        if (forecast5DaysWeather == null)
            return
        forecast5DaysWeather?.weatherList?.let { weatherList ->
            selectedDayWeatherList = weatherList.filter { weather ->
                weather.dateTimeText.substringBefore(" ") ==
                        date.toString().substringBefore("T")
            }
            weatherPresentation(0)
        }
    }

    private fun seekBarSetup(maxSize: Int) {
        val allSeekBarValues = listOf<Int>(7, 6, 5, 4, 3, 2, 1, 0)
        _seekBarTimes.value = allSeekBarValues.subList(0, maxSize).reversed()
    }

    private suspend fun forecastWeather5DaysAVG(
        city: String,
        weatherUnit: WeatherUnit
    ) {
        val forecastWeather =
            weatherRepository.getForecastWeather7DaysAVG(city, weatherUnit)
        when (forecastWeather) {
            is RepositoryResponse.Success -> {
                if (forecastWeather.data.weatherList != null)
                    getForecastWeatherForList(forecastWeather.data.weatherList)
            }
            is RepositoryResponse.Failure -> null
        }
    }

    private fun getForecastWeatherForList(list: List<ForecastWeatherDetail>) {
        if (list.size < 5)
            return
        val list = getWeatherLowInfoList(list)
        if (list.isNotEmpty())
            _weatherLowInfoList.value = list
    }

    private fun getWeatherLowInfoList(list: List<ForecastWeatherDetail>): List<WeatherLowInformation> {
        val weatherLowInfoList = mutableListOf<WeatherLowInformation>()
        for (i in 0..5) {
            val daily = list[i]
            val temp = daily.temp?.day?.roundToInt().toString()
            val date = dateHelper.getDateFromTimestamp(daily.date)
            val day = getDayName(daily.date)
            val iconId = daily.weatherTitleList[0].icon
            weatherLowInfoList.add(WeatherLowInformation(day, temp, iconId, date))
        }
        return weatherLowInfoList
    }

    private fun getDayName(timeStampNumber: Long): String {
        return when {
            dateHelper.isToday(timeStampNumber) -> "Today"
            dateHelper.isMorning(timeStampNumber) -> "Morning"
            else -> dateHelper.getDayOfWeekFromTimestamp(timeStampNumber)
                .toString().toLowerCase().capitalize()
        }
    }

    private fun getDayName(date: LocalDateTime): String {
        return when {
            dateHelper.isToday(date) -> "Today"
            dateHelper.isMorning(date) -> "Morning"
            else -> dateHelper.getDayOfWeekFromTimestamp(date)
                .toString().toLowerCase().capitalize()
        }
    }

    //bug IndexOutOfBoundsException: Index: 7, Size: 2 todo
    private fun weatherPresentation(index: Int = 0) {
        seekBarSetup(selectedDayWeatherList.size)
        val currentWeather = selectedDayWeatherList[index]
        _progress.value = false
        _cityName.value = forecast5DaysWeather?.city?.cityName
        _date.value = currentWeather.dateTimeText.substringBefore(" ")
        _temp.value = currentWeather.temp?.temp?.roundToInt()
        _tempDesc.value = currentWeather.weatherTitleList[0].description
        _weatherImageIconId.value = currentWeather.weatherTitleList[0].icon
        _humidity.value = currentWeather.temp?.humidity.toString()
        _clouds.value = currentWeather.clouds?.cloudCount.toString()
        _wind.value = "${currentWeather.wind?.speed} - ${currentWeather.wind?.degree}"
    }

    fun rvSeekBarChangeIndex(progress: Int) {
        if (progress < 0)
            weatherPresentation(0)
        else
            weatherPresentation(progress)
    }

    fun weatherIconLoader(ivIcon: ImageView?, imgId: String) {
        ivIcon?.let {
            imageHelper.loadWeatherIcon(ivIcon, imgId)
        }
    }

}