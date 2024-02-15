package com.example.rajtube.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.rajtube.Activity.ViewChannelActivity
import com.example.rajtube.LogInActivity
import com.example.rajtube.Models.User
import com.example.rajtube.R
import com.example.rajtube.Utills.USER_NODE
import com.example.rajtube.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding =  FragmentProfileBinding.inflate(inflater, container, false)


       Firebase.firestore.collection(USER_NODE)
           .document(Firebase.auth.currentUser!!.email.toString())
           .get()
           .addOnSuccessListener {
               if (it.exists()){
                   var user : User? = it.toObject<User>()
                   if (user != null){
                       binding.name.text = user.name
                       binding.email.text = user.email

                       if (!user.image.isNullOrEmpty()){
                           Glide.with(this)
                               .load(user.image)
                               .placeholder(R.drawable.user)
                               .into(binding.userProfileImage)
                       }
                   }
               }
           }

        binding.viewChannelBtn.setOnClickListener {
            startActivity(Intent(requireContext() , ViewChannelActivity::class.java))
        }

        binding.logOutBtn.setOnClickListener {
            Firebase.auth.signOut()
            startActivity(Intent(requireContext() , LogInActivity::class.java))
            activity?.finish()
        }

        return binding.root
    }

    companion object {
    }
}