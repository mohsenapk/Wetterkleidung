package com.mohsen.apk.wetterkleidung.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.model.WeatherLowInformation
import com.mohsen.apk.wetterkleidung.utility.DateHelper
import com.mohsen.apk.wetterkleidung.utility.ImageHelper
import org.threeten.bp.LocalDateTime

class WeatherLowInfoAdapter(
    private val list: List<WeatherLowInformation>,
    private val imageHelper: ImageHelper,
    private val dateHelper: DateHelper,
    private val dateSelect: (strDate: LocalDateTime) -> Unit
) : RecyclerView.Adapter<WeatherLowInfoAdapter.VHolder>() {
    private var dayList = list.subList(1, list.size)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather_low_info, parent, false)
        return VHolder(view)
    }

    override fun getItemCount() = dayList.size

    override fun onBindViewHolder(holder: VHolder, position: Int) =
        holder.setData(dayList[position])

    inner class VHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvDate: TextView = view.findViewById(R.id.tvDate)
        private val tvTemp: TextView = view.findViewById(R.id.tvTemp)
        private val ivIcon: ImageView = view.findViewById(R.id.imgWeatherIcon)

        init {
            view.setOnClickListener {
                dateSelect(dayList[adapterPosition].date)
                addOrRemoveTodayFromList(dayList[adapterPosition].date)
            }
        }

        private fun addOrRemoveTodayFromList(date: LocalDateTime) {
            dayList = if (dateHelper.isToday(date)) list.subList(1, list.size) else list
            notifyDataSetChanged()
        }


        fun setData(weather: WeatherLowInformation) {
            tvDate.text = weather.dayOrDate
            tvTemp.text = weather.tempStr
            imageHelper.loadWeatherIcon(ivIcon, weather.iconId)
        }
    }
}