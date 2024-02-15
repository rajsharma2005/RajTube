package com.example.rajtube.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rajtube.Activity.VideoPlayerActivity
import com.example.rajtube.Adapter.Home_Video_Adapter
import com.example.rajtube.Models.Video
import com.example.rajtube.Utills.VIDEO
import com.example.rajtube.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class HomeFragment : Fragment()  , Home_Video_Adapter.OnItemClickListener{

    private lateinit var binding : FragmentHomeBinding
    lateinit var adapter : Home_Video_Adapter
    lateinit var videoList : ArrayList<Video>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentHomeBinding.inflate(inflater, container, false)

        videoList = ArrayList<Video>()

        binding.recycleView.layoutManager = LinearLayoutManager(requireContext())

            adapter = Home_Video_Adapter(requireContext() , videoList , this)


        binding.recycleView.adapter = adapter

        Firebase.firestore
                .collection(VIDEO)
                .get()
                .addOnSuccessListener {
                    var tempList = ArrayList<Video>()
                    if (it.isEmpty) {
                        Log.e("error", "it is empty")
                    }
                    for (i in it){
                        var videos = i.toObject<Video>()
                        tempList.add(videos)
                    }

                    videoList.addAll(tempList)
                    binding.progressBar.visibility = View.GONE
                    adapter.notifyDataSetChanged()
                }


        return binding.root
    }

      override fun onItemClick(position: Int) {
        // Handle item click here
        // You can access the clicked item position and perform any required actions
        // For example, you can start a new activity to show video details

        val clickedVideo = videoList[position]

        val intent = Intent(requireContext(), VideoPlayerActivity::class.java)
        intent.putExtra("videoUrl", clickedVideo.videoUrl)
        intent.putExtra("title", clickedVideo.tittle)
        intent.putExtra("thumbnail", clickedVideo.thumbnail)
        intent.putExtra("email", clickedVideo.email)
        intent.putExtra("time", clickedVideo.time)
          try {
              intent.putExtra("like" , clickedVideo.like)
          }catch (e : Exception){

          }

        startActivity(intent)
    }

    companion object {

    }
}
