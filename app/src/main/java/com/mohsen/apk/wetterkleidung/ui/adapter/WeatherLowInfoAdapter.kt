package com.mohsen.apk.wetterkleidung.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.model.WeatherLowInformation
import org.threeten.bp.LocalDate
import java.time.DayOfWeek

class WeatherLowInfoAdapter(
    private val list: List<WeatherLowInformation>
) : RecyclerView.Adapter<WeatherLowInfoAdapter.VHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_weather_low_info, parent, false)
        return VHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VHolder, position: Int) = holder.setData(list[position])

    inner class VHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvDate: TextView = view.findViewById(R.id.tvDate)
        private val tvTemp: TextView = view.findViewById(R.id.tvTemp)
        private val ivIcon: ImageView = view.findViewById(R.id.ivIcon)

        fun setData(weather: WeatherLowInformation) {
            tvDate.text = weather.dayOrDate
            tvTemp.text = weather.tempStr
        }
    }
}