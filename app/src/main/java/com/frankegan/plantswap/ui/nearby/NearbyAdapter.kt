package com.frankegan.plantswap.ui.nearby

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.frankegan.plantswap.R
import com.frankegan.plantswap.data.model.PlantPost
import com.frankegan.plantswap.databinding.NearbyListItemBinding

class NearbyAdapter : RecyclerView.Adapter<NearbyAdapter.ViewHolder>() {

    var plantPosts: List<PlantPost> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NearbyListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(plantPosts[position])
    }

    override fun getItemCount(): Int = plantPosts.size

    inner class ViewHolder(private val binding: NearbyListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(plantPost: PlantPost) {
            with(binding) {
                title.text = plantPost.title
                description.text = plantPost.description
                heroImage.load(plantPost.photos.firstOrNull()) {
                    placeholder(ColorDrawable(ContextCompat.getColor(binding.root.context, R.color.colorPrimary)))
                    error(ColorDrawable(ContextCompat.getColor(binding.root.context, R.color.colorPrimary)))
                }
            }
        }
    }
}