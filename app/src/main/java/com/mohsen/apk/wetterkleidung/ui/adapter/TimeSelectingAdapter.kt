package com.mohsen.apk.wetterkleidung.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mohsen.apk.wetterkleidung.R
import com.mohsen.apk.wetterkleidung.model.TimeSelect

class TimeSelectingAdapter(
    private val list: List<TimeSelect>,
    private val backList: (list: List<TimeSelect>) -> Unit
) : RecyclerView.Adapter<TimeSelectingAdapter.MHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_selecting, parent, false)
        return MHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MHolder, position: Int) {
        holder.setData(list[position], position)
    }

    inner class MHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var selectedPosition = 0
        private val clParent = view.findViewById<ConstraintLayout>(R.id.clParent)
        private val tvTime = view.findViewById<TextView>(R.id.tvTime)
        private val checkTime = view.findViewById<CheckBox>(R.id.checkTime)

        init {
            initUi()
        }

        fun setData(timeSelect: TimeSelect, position: Int) {
            selectedPosition = position
            tvTime.text = timeSelect.text
            checkTime.isChecked = timeSelect.selected
            checkForSelectAllChange(false)
        }

        private fun initUi() {
            clParent.setOnClickListener { selectedItem() }
        }

        private fun selectedItem() {
            checkTime.isChecked = !checkTime.isChecked
            list[selectedPosition].selected = !list[selectedPosition].selected
            if (selectedPosition == 0)
                selectAllItems()
            else
                checkForSelectAllChange()
            backList(list.filter { it.selected })
        }

        private fun selectAllItems() {
            list.map { it.selected = list[0].selected }
            notifyDataSetChanged()
        }

        private fun checkForSelectAllChange(withNotify: Boolean = true) {
            val notTimeSelected = list.subList(1, list.size).firstOrNull { !it.selected }
            list[0].selected = (notTimeSelected == null)
            if (withNotify)
                notifyItemChanged(0)
        }
    }
}