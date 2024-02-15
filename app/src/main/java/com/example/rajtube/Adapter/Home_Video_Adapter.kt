package com.example.rajtube.Adapter

import android.content.Context
import android.media.MediaMetadataRetriever

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rajtube.Models.User
import com.example.rajtube.Models.Video
import com.example.rajtube.R
import com.example.rajtube.Utills.USER_NODE
import com.example.rajtube.Utills.VIDEO

import com.example.rajtube.databinding.HomeVideoDesignRvBinding
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import org.checkerframework.checker.index.qual.LengthOf
import java.io.IOException


class Home_Video_Adapter(var context: Context, var videoList: ArrayList<Video> , var listener : OnItemClickListener) :
    RecyclerView.Adapter<Home_Video_Adapter.ViewHolder>() {

    inner class ViewHolder(var binding: HomeVideoDesignRvBinding) : RecyclerView.ViewHolder(binding.root){
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
        var binding = HomeVideoDesignRvBinding.inflate(LayoutInflater.from(context), parent, false)

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

        var email = videoList[position].email
        Log.i("email" , email)

        Firebase.firestore.collection(USER_NODE)
            .document(email)
            .get()
            .addOnSuccessListener {
                if (!it.exists()){
                    Log.e("error" , "it is empty")
                }else {
                    var user = it.toObject<User>()
//                try {
//                    Glide.with(context)
//                        .load(user!!.image)
//                        .placeholder(R.drawable.user)
//                        .into(holder.binding.userProfileImage)
//                }catch (e : Exception){
//                    Log.e("error in catch" ,e.localizedMessage!!)
//                }

                    holder.binding.name.text = user!!.name
                }

            }
            .addOnFailureListener {
                Log.e("error" , it.localizedMessage)
            }

        holder.binding.tittle.text = videoList[position].tittle
        try {
            Firebase.firestore.collection(VIDEO)
                .document(videoList[position].tittle)
                .get()
                .addOnSuccessListener {
                    if (!it.exists()){
                        Log.e("error234" , "it does not exists")
                    }else {
                        var video = it.toObject<Video>()
                        holder.binding.views.text = video!!.views.toString()+ "views"
                    }
                }
                .addOnFailureListener {
                    Log.e("error123" , it.localizedMessage)
                }
        }catch (e : Exception){

        }

       // holder.binding.views.text = videoList[position].tittle
        try {
            val text  = TimeAgo.using(videoList.get(position).time.toLong()).toInt()
            holder.binding.time.text = text.toString()
        }catch (e : Exception){
            holder.binding.time.text = ""
        }

        //val videoUrl: String = videoList[position].videoUrl


         //  val durationInSeconds = getVideoDuration(context, videoUrl)/1000


//        val duration = formatDuration(durationInSeconds)
//        Log.i("durationInSeconds" , durationInSeconds.toString())
//        Log.i("duration" , duration)
//       holder.binding.length.text = duration


    }
//    fun getVideoDuration(context: Context, videoUrl: String): Long {
//        // Create a MediaMetadataRetriever
//        val retriever = MediaMetadataRetriever()
//
//        try {
//            // Set the data source to the video URL
//            retriever.setDataSource(videoUrl, HashMap<String, String>())
//
//            // Get the duration in milliseconds
//            val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
//            return durationString?.toLong() ?: 0
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } finally {
//            // Release the MediaMetadataRetriever
//            retriever.release()
//        }
//
//        return 0
//    }
//    fun formatDuration(durationInSeconds: Long): String {
//        val minutes = durationInSeconds / 60
//        val seconds = durationInSeconds % 60
//        return String.format("%d:%02d", minutes, seconds)
//    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}