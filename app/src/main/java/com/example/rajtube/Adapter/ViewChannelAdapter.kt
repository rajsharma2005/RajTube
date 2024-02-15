package com.example.rajtube.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rajtube.Models.Video
import com.example.rajtube.R
import com.example.rajtube.databinding.ChannelRvDesignBinding
import com.github.marlonlom.utilities.timeago.TimeAgo


class ViewChannelAdapter(var context: Context, var videoList: ArrayList<Video> , var listener : OnItemClickListener) :
    RecyclerView.Adapter<ViewChannelAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ChannelRvDesignBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    listener.onItemClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var binding = ChannelRvDesignBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(videoList[position].thumbnail)
            .placeholder(R.drawable.user)
            .into(holder.binding.thumbnail )

        holder.binding.tittle.text = videoList[position].tittle
       // holder.binding.views.text = videoList[position].tittle
        try {
            val text  = TimeAgo.using(videoList.get(position).time.toLong())
            holder.binding.time.text = text
        }catch (e : Exception){
            holder.binding.time.text = ""
        }


    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

}