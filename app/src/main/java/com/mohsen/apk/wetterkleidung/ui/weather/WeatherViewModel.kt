package com.mohsen.apk.wetterkleidung.ui.weather

import android.widget.ImageView
import androidx.lifecycle.*
import com.mohsen.apk.wetterkleidung.db.prefrences.SharedPreferenceManager
import com.mohsen.apk.wetterkleidung.model.*
import com.mohsen.apk.wetterkleidung.repository.WeatherRepository
import com.mohsen.apk.wetterkleidung.utility.*
import org.threeten.bp.LocalDateTime
import kotlin.math.roundToInt
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val dateHelper: DateHelper,
    private val imageHelper: ImageHelper,
    private val seekBarManager: SeekBarManager,
    private val dayNameManager: DayNameManager,
    private val resourceManager: ResourceManager,
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
    private val _progressVisible = MutableLiveData<Boolean>()
    private val _parentVisible = MutableLiveData<Boolean>()
    private val _weatherImageIconId = MutableLiveData<String>()
    private val _weatherLowInfoList = MutableLiveData<List<WeatherLowInformation>>()
    private val _seekBarTextList = MutableLiveData<List<String>>()
    private val _seekBarSelectedText = MutableLiveData<String>()
    private val _seekTimeProgress = MutableLiveData<Float>()
    private val _changeBackImage = MutableLiveData<Int>()
    private val _changeStatusBarColor = MutableLiveData<Int>()
    private val _changeBackBottomColor = MutableLiveData<Int>()
    private val _changeTextColor = MutableLiveData<Int>()
    private val _changeAvatar = MutableLiveData<Int>()
    private val _progressAvatarImageVisible = MutableLiveData<Boolean>()
    private val _imgAvatarUmbrellaVisible = MutableLiveData<Boolean>()
    private val _seekBarVisibility = MutableLiveData<Boolean>()

    val snackBarError: LiveData<String> = _snackBarError
    val cityName: LiveData<String> = _cityName
    val date: LiveData<String> = _date
    val humidity: LiveData<String> = _humidity
    val clouds: LiveData<String> = _clouds
    val wind: LiveData<String> = _wind
    val dayName: LiveData<String> = _dayName
    val temp: LiveData<Int> = _temp
    val tempDesc: LiveData<String> = _tempDesc
    val progressVisible: LiveData<Boolean> = _progressVisible
    val parentVisible: LiveData<Boolean> = _parentVisible
    val weatherImageIconId: LiveData<String> = _weatherImageIconId
    val weatherLowInfoList: LiveData<List<WeatherLowInformation>> = _weatherLowInfoList
    val seekBarTextList: LiveData<List<String>> = _seekBarTextList
    val seekBarSelectedText: LiveData<String> = _seekBarSelectedText
    val seekTimeProgress: LiveData<Float> = _seekTimeProgress
    val changeBackImage: LiveData<Int> = _changeBackImage
    val changeStatusBarColor: LiveData<Int> = _changeStatusBarColor
    val changeBackBottomColor: LiveData<Int> = _changeBackBottomColor
    val changeTextColor: LiveData<Int> = _changeTextColor
    val changeAvatar: LiveData<Int> = _changeAvatar
    val progressAvatarImageVisible: LiveData<Boolean> = _progressAvatarImageVisible
    val imgAvatarUmbrellaVisible: LiveData<Boolean> = _imgAvatarUmbrellaVisible
    val seekBarVisibility: LiveData<Boolean> = _seekBarVisibility

    fun onResume() {
        weatherUnit = prefs.getWeatherUnit()
        val defaultCity = prefs.getCityDefault()
        if (defaultCity.toUpperCase() != forecast5DaysWeather?.city?.cityName?.toUpperCase())
            startAsync()
    }

    private fun startAsync() = viewModelScope.async {
        _changeStatusBarColor.value = resourceManager.getStatusColorId(-1)
        weatherUnit = prefs.getWeatherUnit()
        val defaultCity = prefs.getCityDefault()
        if (defaultCity.isEmpty()) {
            return@async
        }
        forecastWeather5DaysHourly(defaultCity, weatherUnit)
        forecastWeather5DaysAVG(defaultCity, weatherUnit)
        setFirstDayWeather()
    }

    private fun setFirstDayWeather() {
        val firstDay = forecast5DaysWeather?.weatherList?.get(0)?.date
        val firstDayDate = firstDay?.let { dateHelper.getDateFromTimestamp(firstDay) }
        firstDayDate?.let { dateChanged(it) }
    }

    private suspend fun forecastWeather5DaysHourly(
        city: String,
        weatherUnit: WeatherUnit
    ) {
        _progressVisible.value = true
        _parentVisible.value = false
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
        _dayName.value = dayNameManager.getDayName(date)
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
            firstUiSetup()
        }
    }

    private fun firstUiSetup() {
        presentation(0)
        changeAvatarWithWeather(selectedDayWeatherList[0])
        checkForSeekBarVisibility(selectedDayWeatherList.size)
        val firstSeekBarItemIndex = seekBarManager.getSeekBarValues(selectedDayWeatherList.size)[0]
        _seekBarSelectedText.value = seekBarManager.getSeekBarTextFromIndex(firstSeekBarItemIndex)
    }

    private fun checkForSeekBarVisibility(size: Int) {
        _seekBarVisibility.value = (size > 1)
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
        if (list.size < 5) return
        val list = getWeatherLowInfoList(list)
        if (list.isNotEmpty())
            _weatherLowInfoList.value = list
    }

    private fun getWeatherLowInfoList(list: List<ForecastWeatherDetail>): List<WeatherLowInformation> {
        val weatherLowInfoList = mutableListOf<WeatherLowInformation>()
        for (i in 0..4) {
            val daily = list[i]
            val temp = daily.temp?.day?.roundToInt().toString()
            val date = dateHelper.getDateFromTimestamp(daily.date)
            val day = dayNameManager.getDayName(daily.date)
            val iconId = daily.weatherTitleList[0].icon
            weatherLowInfoList.add(WeatherLowInformation(day, temp, iconId, date))
        }
        return weatherLowInfoList
    }

    private fun presentation(index: Int = 0) {
        seekBarSetup(selectedDayWeatherList.size)
        changeBackImageWithIndex(index)
        changeBackBottomColorWithIndex(index)
        changeTextColorWithIndex(index)
        weatherPresentation(index)
    }

    private fun seekBarSetup(maxSize: Int) {
        if (maxSize < 1) {
            return
        }
        allSeekTimeIndexes = seekBarManager.getSeekBarValues(maxSize)
        _seekBarTextList.value =
            allSeekTimeIndexes.map { seekBarManager.getTinySeekBarTextFromIndex(it) }
    }

    private fun changeAvatarWithWeather(weather: Forecast5DaysWeatherDetail) {
        val avatarImageResourceId = resourceManager.getAvatarResourceIdFromWeather(weather)
        if (avatarImageResourceId > 0)
            changeAvatarImageWithLoadingAsync(avatarImageResourceId)
        checkForUmbrellaAsync(weather)
    }

    private fun changeAvatarImageWithLoadingAsync(avatarImageResourceId: Int) =
        viewModelScope.async {
            _changeAvatar.value = 0
            _progressAvatarImageVisible.value = true
            delay(300)
            _progressAvatarImageVisible.value = false
            _changeAvatar.value = avatarImageResourceId
            cancel()
        }

    private fun checkForUmbrellaAsync(weather: Forecast5DaysWeatherDetail) =
        viewModelScope.async {
            val hasRain =
                weather.weatherTitleList[0].description
                    .toLowerCase().contains("rain")
            checkForUmbrellaAsyncHasRain(hasRain)
            cancel()
        }

    private suspend fun checkForUmbrellaAsyncHasRain(hasRain: Boolean) {
        _imgAvatarUmbrellaVisible.value = false
        _progressAvatarImageVisible.value = true
        delay(300)
        _progressAvatarImageVisible.value = false
        _imgAvatarUmbrellaVisible.value = hasRain
    }

    //bug IndexOutOfBoundsException: Index: 7, Size: 2 todo
    private fun weatherPresentation(index: Int) {
        val currentWeather = selectedDayWeatherList[index]
        _progressVisible.value = false
        _parentVisible.value = true
        _cityName.value = forecast5DaysWeather?.city?.cityName
        _date.value = currentWeather.dateTimeText.substringBefore(" ")
        _temp.value = currentWeather.temp?.temp?.roundToInt()
        _tempDesc.value = currentWeather.weatherTitleList[0].description
        _weatherImageIconId.value = currentWeather.weatherTitleList[0].icon
        _humidity.value = currentWeather.temp?.humidity.toString()
        _clouds.value = currentWeather.clouds?.cloudCount.toString()
        _wind.value = "${currentWeather.wind?.speed} - ${currentWeather.wind?.degree}"
    }

    fun seekBarProgressChangeOnSeeking(progress: Int) {
        _changeAvatar.value = 0
        _imgAvatarUmbrellaVisible.value = false
        _progressAvatarImageVisible.value = true
        setSeekBarSelectedText(progress)
        presentation(progress)
    }

    private fun setSeekBarSelectedText(progress: Int) {
        _seekBarSelectedText.value =
            seekBarManager.getSeekBarTextFromIndex(allSeekTimeIndexes[progress])
    }

    fun seekBarProgressChangedOnStopTouching(progress: Int) {
        changeAvatarWithWeather(selectedDayWeatherList[progress])
        setSeekBarSelectedText(progress)
    }

    private fun changeBackImageWithIndex(index: Int) {
        _changeBackImage.value = resourceManager.getBackImageResourceId(allSeekTimeIndexes[index])
        _changeStatusBarColor.value = resourceManager.getStatusColorId(allSeekTimeIndexes[index])
    }

    private fun changeBackBottomColorWithIndex(index: Int) {
        _changeBackBottomColor.value =
            resourceManager.getBackBottomColorId(allSeekTimeIndexes[index])
    }

    private fun changeTextColorWithIndex(index: Int) {
        _changeTextColor.value = resourceManager.getTextColorId(allSeekTimeIndexes[index])
    }

    fun weatherIconLoader(ivIcon: ImageView?, imgId: String) {
        ivIcon?.let {
            imageHelper.loadWeatherIcon(ivIcon, imgId)
        }
    }

}