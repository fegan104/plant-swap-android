package com.frankegan.plantswap.ui.account

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.frankegan.plantswap.data.model.PlantPost
import javax.inject.Inject

class UserPostsAdapter @Inject constructor() : PagingDataAdapter<PlantPost, UserPostsAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val body: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(item: PlantPost?) {
            if (item == null) {
                body.text = "Loading..."
            } else {
                body.text = item.toString()
            }
        }
    }

    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PlantPost>() {
            override fun areItemsTheSame(oldItem: PlantPost, newItem: PlantPost): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: PlantPost, newItem: PlantPost): Boolean {
                return oldItem == newItem
            }
        }
    }
}