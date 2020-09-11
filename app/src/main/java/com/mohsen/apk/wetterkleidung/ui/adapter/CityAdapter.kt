package com.mohsen.apk.wetterkleidung.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.model.City
import com.mohsen.apk.wetterkleidung.utility.ImageHelper

class CityAdapter(
    private val list: List<City>,
    private val imageHelper: ImageHelper,
    private val itemClicked: (city: City) -> Unit,
    private val itemDeleteClicked: (city: City) -> Unit
) :
    RecyclerView.Adapter<CityAdapter.CityHolder>() {
    private lateinit var context: Context
    private lateinit var deletedItem: City
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_city_info, parent, false)
        return CityHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: CityHolder, position: Int) =
        holder.setData(list[position])

    inner class CityHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val parentView: ConstraintLayout = view.findViewById(R.id.parentView)
        private val tvCity: TextView = view.findViewById(R.id.tvCity)
        private val tvTemp: TextView = view.findViewById(R.id.tvTemp)
        private val ivIcon: ImageView = view.findViewById(R.id.imgWeatherIcon)
        private val ivDelete: ImageView = view.findViewById(R.id.ivDelete)


        init {
            parentView.setOnClickListener {
                itemClicked(list[adapterPosition])
                setDefaultCityWithItemClicked(adapterPosition)
            }
            ivDelete.setOnClickListener {
                deleteItem(adapterPosition)
                itemDeleteClicked(deletedItem)
            }
        }

        private fun setDefaultCityWithItemClicked(position: Int) {
            list.map { it.isDefault = false }
            setDefaultCity(position)
        }

        private fun setDefaultCity(position: Int) {
            list[position].isDefault = true
            notifyDataSetChanged()
        }

        private fun deleteItem(position: Int) {
            deletedItem = list[position]
            list.toMutableList().removeAt(position)
            notifyDataSetChanged()
        }

        fun setData(city: City) {
            tvCity.text = city.name
            tvTemp.text = city.temp
            imageHelper.loadWeatherIcon(ivIcon, city.tempIconId)
            setNotDefaultCityView()
            if (city.isDefault)
                setDefaultCityView()
        }

        private fun setDefaultCityView() {
            parentView.setBackgroundResource(R.drawable.border_solid_white)
        }

        private fun setNotDefaultCityView() {
            parentView.setBackgroundResource(R.drawable.border_white)
        }

    }
}