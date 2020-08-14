package com.mohsen.apk.wetterkleidung.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.model.City
import com.mohsen.apk.wetterkleidung.utility.ImageHelper

class CityAdapter(
    private val list: List<City>,
    private val imageHelper: ImageHelper
) :
    RecyclerView.Adapter<CityAdapter.CityHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city_info, parent, false)
        return CityHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CityHolder, position: Int) =
        holder.setData(list[position])

    inner class CityHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvCity: TextView = view.findViewById(R.id.tvCity)
        private val tvTemp: TextView = view.findViewById(R.id.tvTemp)
        private val ivIcon: ImageView = view.findViewById(R.id.ivIcon)

        fun setData(city: City) {
            tvCity.text = city.name
            tvTemp.text = city.temp.toString()
            imageHelper.loadWeatherIcon(ivIcon, city.tempIconId)
        }
    }
}