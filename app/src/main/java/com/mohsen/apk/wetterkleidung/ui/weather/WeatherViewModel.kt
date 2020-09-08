package com.mohsen.apk.wetterkleidung.ui.weather

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
import com.mohsen.apk.wetterkleidung.R

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val dateHelper: DateHelper,
    private val imageHelper: ImageHelper,
    private val prefs: SharedPreferenceManager
) : ViewModel() {

    private lateinit var selectedDayWeatherList: List<Forecast5DaysWeatherDetail>
    private var forecast5DaysWeather: Forecast5DaysWeather? = null
    private lateinit var allSeekTimeIndexes: List<Int>
    private lateinit var weatherUnit: WeatherUnit

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
    private val _seekBarTextList = MutableLiveData<List<String>>()
    private val _seekBarSelectedText = MutableLiveData<String>()
    private val _seekTimeProgress = MutableLiveData<Float>()
    private val _changeBackImage = MutableLiveData<Int>()
    private val _changeBackBottomColor = MutableLiveData<Int>()
    private val _changeTextColor = MutableLiveData<Int>()
    private val _changeAvatar = MutableLiveData<Int>()

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
    val seekBarTextList: LiveData<List<String>> = _seekBarTextList
    val seekBarSelectedText: LiveData<String> = _seekBarSelectedText
    val seekTimeProgress: LiveData<Float> = _seekTimeProgress
    val changeBackImage: LiveData<Int> = _changeBackImage
    val changeBackBottomColor: LiveData<Int> = _changeBackBottomColor
    val changeTextColor: LiveData<Int> = _changeTextColor
    val changeAvatar: LiveData<Int> = _changeAvatar

    fun start() = viewModelScope.launch {
        weatherUnit = prefs.getWeatherUnit()
        val defaultCity = prefs.getCityDefault()
        if (defaultCity.isEmpty()) {
            return@launch
        }
        forecastWeather5DaysHourly(defaultCity, weatherUnit)
        forecastWeather5DaysAVG(defaultCity, weatherUnit)
        dateChanged(LocalDateTime.now())
    }

    fun onResume() {
        weatherUnit = prefs.getWeatherUnit()
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
        _seekTimeProgress.value = 0F
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
            presentation(0)
        }
    }

    private fun seekBarSetup(maxSize: Int) {
        if (maxSize < 1) return
        allSeekTimeIndexes = listOf<Int>(7, 6, 5, 4, 3, 2, 1, 0).subList(0, maxSize).reversed()
        _seekBarTextList.value = allSeekTimeIndexes.map { getMinSeekBarTextFromIndex(it) }
        _seekBarSelectedText.value = getSeekBarTextFromIndex(allSeekTimeIndexes[0])
    }

    private fun getMinSeekBarTextFromIndex(index: Int): String {
        return when (index) {
            0 -> SeekBarValue.ZERO.minText
            1 -> SeekBarValue.ONE.minText
            2 -> SeekBarValue.TWO.minText
            3 -> SeekBarValue.THREE.minText
            4 -> SeekBarValue.FOUR.minText
            5 -> SeekBarValue.FIVE.minText
            6 -> SeekBarValue.SIX.minText
            7 -> SeekBarValue.SEVEN.minText
            else -> ""
        }
    }

    private fun getSeekBarTextFromIndex(index: Int): String {
        return when (index) {
            0 -> SeekBarValue.ZERO.hours
            1 -> SeekBarValue.ONE.hours
            2 -> SeekBarValue.TWO.hours
            3 -> SeekBarValue.THREE.hours
            4 -> SeekBarValue.FOUR.hours
            5 -> SeekBarValue.FIVE.hours
            6 -> SeekBarValue.SIX.hours
            7 -> SeekBarValue.SEVEN.hours
            else -> ""
        }
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
            dateHelper.isMorning(timeStampNumber) -> "Tomorrow"
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

    private fun presentation(index: Int = 0) {
        seekBarSetup(selectedDayWeatherList.size)
        changeBackImageWithIndex(index)
        changeBackBottomColorWithIndex(index)
        changeTextColorWithIndex(index)
        weatherPresentation(index)
        changeAvatarWithWeather(selectedDayWeatherList[index])
    }

    private fun changeAvatarWithWeather(weather: Forecast5DaysWeatherDetail) {
        var avatarImageResourceId = 0
        when (weather?.temp?.feels_like?.roundToInt()) {
            in (-100..5) -> avatarImageResourceId = R.drawable.avatar_very_cold
            in (5..10) -> avatarImageResourceId = R.drawable.avatar_cold
            in (10..15) -> avatarImageResourceId = R.drawable.avatar_littel_cold
            in (15..20) -> avatarImageResourceId = R.drawable.avatar_normal
            in (20..25) -> avatarImageResourceId = R.drawable.avatar_little_hot
            in (25..30) -> avatarImageResourceId = R.drawable.avatar_hot
            in (30..100) -> avatarImageResourceId = R.drawable.avatar_very_hot
        }
        if (avatarImageResourceId > 0)
            _changeAvatar.value = avatarImageResourceId
    }


    //bug IndexOutOfBoundsException: Index: 7, Size: 2 todo
    private fun weatherPresentation(index: Int) {
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

    fun seekBarProgressChanged(progress: Int) {
        presentation(progress)
        _seekBarSelectedText.value = getSeekBarTextFromIndex(allSeekTimeIndexes[progress])
    }

    private fun changeBackImageWithIndex(index: Int) {
        _changeBackImage.value = getBackImageResourceId(allSeekTimeIndexes[index])
    }

    private fun changeBackBottomColorWithIndex(index: Int) {
        _changeBackBottomColor.value = getBackBottomColorId(allSeekTimeIndexes[index])
    }

    private fun changeTextColorWithIndex(index: Int) {
        _changeTextColor.value = getTextColorId(allSeekTimeIndexes[index])
    }

    private fun getTextColorId(index: Int): Int {
        return when (index) {
            6, 7 -> R.color.white
            else -> R.color.black
        }
    }

    private fun getBackImageResourceId(index: Int): Int {
        return when (index) {
            0, 1 -> R.drawable.back_day_break
            6 -> R.drawable.back_evening
            7 -> R.drawable.back_night
            else -> R.drawable.back_day
        }
    }

    private fun getBackBottomColorId(index: Int): Int {
        return when (index) {
            0, 1 -> R.color.backBottomDayBreak
            6 -> R.color.backBottomEvening
            7 -> R.color.backBottomNight
            else -> R.color.backBottomDay
        }
    }

    fun weatherIconLoader(ivIcon: ImageView?, imgId: String) {
        ivIcon?.let {
            imageHelper.loadWeatherIcon(ivIcon, imgId)
        }
    }

}