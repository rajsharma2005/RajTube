package com.example.rajtube.Activity


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rajtube.Adapter.ViewChannelAdapter
import com.example.rajtube.Models.Video
import com.example.rajtube.Utills.VIDEO
import com.example.rajtube.databinding.ActivityViewChannelBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class ViewChannelActivity : AppCompatActivity() , ViewChannelAdapter.OnItemClickListener {
    private var binding: ActivityViewChannelBinding? = null

    lateinit var adapter: ViewChannelAdapter
    lateinit var videoList: ArrayList<Video>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewChannelBinding.inflate(layoutInflater)

        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding!!.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        videoList = ArrayList()

        binding!!.videosRv.layoutManager = LinearLayoutManager(this)
        adapter = ViewChannelAdapter(this, videoList , this)
        binding!!.videosRv.adapter = adapter

        Firebase.firestore
            .collection(Firebase.auth.currentUser!!.email + VIDEO)
            .get()
            .addOnSuccessListener {
                var tempList = ArrayList<Video>()
                if (it.isEmpty) {
                    Log.e("error", "it is empty")
                }
                for (i in it) {
                    var video: Video = i.toObject<Video>()!!

                    tempList.add(video)

                }

                videoList.addAll(tempList)
               // binding!!.progressBar.visibility = View.GONE
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.e("error ", it.localizedMessage)
            }

    }

    override fun onItemClick(position: Int) {
        // Handle item click here
        // You can access the clicked item position and perform any required actions
        // For example, you can start a new activity to show video details

        val clickedVideo = videoList[position]

        val intent = Intent(this, VideoPlayerActivity::class.java)
        intent.putExtra("videoUrl", clickedVideo.videoUrl)
        intent.putExtra("title", clickedVideo.tittle)
        intent.putExtra("email", clickedVideo.email)
        intent.putExtra("time", clickedVideo.time)
        startActivity(intent)
    }
}