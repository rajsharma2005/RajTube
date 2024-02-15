package com.example.rajtube.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rajtube.Activity.AddVideoActivity
import com.example.rajtube.databinding.FragmentAddBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddFragment : BottomSheetDialogFragment() {

    private lateinit var binding : FragmentAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentAddBinding.inflate(inflater, container, false)

        binding.addVideo.setOnClickListener{
            startActivity(Intent(requireContext() , AddVideoActivity::class.java))
            activity?.finish()
        }


        return binding.root
    }
    override fun onStart() {
        super.onStart()
        dialog?.let {
            // Make the dialog dismiss when clicking outside
            it.setCanceledOnTouchOutside(true)
        }
    }

    companion object {

    }
}