package com.alexlipin.myretrofitapp.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.alexlipin.myretrofitapp.R
import com.alexlipin.myretrofitapp.model.FactItem

open class NumbersAdapter() : ListAdapter<FactItem, NumbersAdapter.ViewHolder>(BenchmarkInfoDiffCallback()) {

    private var listener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position))
        viewHolder.itemView.setOnClickListener {
            listener?.onItemClick(currentList[position])
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val info: TextView
        init { info = itemView.findViewById(R.id.info) }
        fun bind(item: FactItem) { info.text = item.text }
    }

    internal class BenchmarkInfoDiffCallback : DiffUtil.ItemCallback<FactItem>() {
        override fun areItemsTheSame(oldItem: FactItem, newItem: FactItem): Boolean {
            return oldItem.number == newItem.number
        }
        override fun areContentsTheSame(oldItem: FactItem, newItem: FactItem): Boolean {
            return oldItem.text == newItem.text && oldItem.number == newItem.number
        }
    }

    interface OnClickListener {
        fun onItemClick(item: FactItem)
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }
}