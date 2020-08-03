package com.mohsen.apk.wetterkleidung.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mohsen.apk.wetterkleidung.R
import timber.log.Timber

class SeekTimeAdapter(
    private val listTimeId: List<Int>
) : RecyclerView.Adapter<SeekTimeAdapter.MHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_seek_times, parent, false)
        return MHolder(view)
    }

    override fun getItemCount(): Int = listTimeId.size

    override fun onBindViewHolder(holder: MHolder, position: Int) =
        holder.setImage(listTimeId[position])

    inner class MHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val image: ImageView = view.findViewById(R.id.imgTime)
        fun setImage(id: Int) {
            when (id) {
                0 -> image.setImageResource(R.drawable.time0_3)
                1 -> image.setImageResource(R.drawable.time6_9)
                2 -> image.setImageResource(R.drawable.time9_12)
                3 -> image.setImageResource(R.drawable.time12_15)
                4 -> image.setImageResource(R.drawable.time15_18)
                5 -> image.setImageResource(R.drawable.time18_21)
                6 -> image.setImageResource(R.drawable.time21_24)
            }
        }
    }
}