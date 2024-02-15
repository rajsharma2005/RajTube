package com.example.rajtube.Activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.rajtube.Adapter.ViewChannelAdapter
import com.example.rajtube.Models.User
import com.example.rajtube.Models.Video
import com.example.rajtube.R
import com.example.rajtube.Utills.SUBS
import com.example.rajtube.Utills.USER_NODE
import com.example.rajtube.Utills.VIDEO
import com.example.rajtube.databinding.ActivityProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class ProfileActivity : AppCompatActivity(), ViewChannelAdapter.OnItemClickListener {
    private var binding: ActivityProfileBinding? = null

    lateinit var adapter: ViewChannelAdapter
    lateinit var videoList: ArrayList<Video>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        binding!!.toolbar.setOnClickListener {
            onBackPressed()
        }

        var email = intent.getStringExtra("user")
        var isSubs = intent.hasExtra("isSubs")


        if (isSubs){
            binding!!.subscribe.text = "UnSubscribe"
        }else{
            binding!!.subscribe.text = "Subscribe"
        }

        Firebase.firestore.collection(USER_NODE)
            .document(email.toString())
            .get()
            .addOnSuccessListener {
                var user: User? = it.toObject()
                Glide.with(this)
                    .load(user!!.image)
                    .placeholder(R.drawable.user)
                    .into(binding!!.userProfileImage)

                binding!!.name.text = user.name
                binding!!.email.text = user.email
                binding!!.subs.text = user.subs.toString() + "Subscribers"
            }

        videoList = ArrayList()

        binding!!.videosRv.layoutManager = LinearLayoutManager(this)
        adapter = ViewChannelAdapter(this, videoList, this)
        binding!!.videosRv.adapter = adapter

        Firebase.firestore.collection(email.toString() + VIDEO)
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
                Log.e("error ", it.localizedMessage!!)
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