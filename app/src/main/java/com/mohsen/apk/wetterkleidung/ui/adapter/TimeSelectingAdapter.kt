package com.mohsen.apk.wetterkleidung.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mohsen.apk.wetterkleidung.R

class TimeSelectingAdapter(
    private val list: List<String>
) : RecyclerView.Adapter<TimeSelectingAdapter.MHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_selecting, parent, false)
        return MHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MHolder, position: Int){
        holder.setData(list[position])
    }

    inner class MHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvTime = view.findViewById<TextView>(R.id.tvTime)
        private val checkTime = view.findViewById<CheckBox>(R.id.checkTime)

        fun setData(strText: String){
            tvTime.text = strText
        }
    }
}