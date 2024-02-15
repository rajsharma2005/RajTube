package com.example.rajtube.Activity


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.rajtube.HomeActivity
import com.example.rajtube.Models.User
import com.example.rajtube.Models.Video
import com.example.rajtube.Utills.THUMBNAIL_FOLDER
import com.example.rajtube.Utills.USER_NODE
import com.example.rajtube.Utills.VIDEO
import com.example.rajtube.Utills.VIDEO_FOLDER
import com.example.rajtube.Utills.uploadImage
import com.example.rajtube.Utills.uploadVideo
import com.example.rajtube.databinding.ActivityAddVideoBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

class AddVideoActivity : AppCompatActivity() {
    private var binding : ActivityAddVideoBinding? = null
    private lateinit var videoUrl : String
    private lateinit var imageUrl : String
    lateinit var progressDialog: ProgressDialog

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri ->
        uri?.let {
            uploadVideo(this ,uri , VIDEO_FOLDER , progressDialog){
                url ->
                if (url != null){


                    videoUrl = url
                }
            }
        }
    }



    var imageUrl2 : String? = null

    private val launcher2 = registerForActivityResult(ActivityResultContracts.GetContent()){
            uri ->
        uri?.let {
            uploadImage(uri , THUMBNAIL_FOLDER , progressDialog){
                    url ->
                if (url != null){

                    binding!!.uploadImage.setImageURI(uri)
                    imageUrl2 = url
                }
            }
        }
    }






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVideoBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setSupportActionBar(binding!!.toolbar)

        progressDialog = ProgressDialog(this)

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding!!.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this , HomeActivity::class.java))

            finish()
        }

        binding!!.uploadVideo.setOnClickListener {
            launcher.launch("video/*")
        }
        binding!!.uploadImage.setOnClickListener {
          //  launch.launch("images/*")
            launcher2.launch("image/*")
        }



        binding!!.upload.setOnClickListener {
            Firebase.firestore.collection(USER_NODE)
                .document(Firebase.auth.currentUser!!.email.toString())
                .get()
                .addOnSuccessListener {
                    var user : User? = it.toObject<User>()
                    var video = Video(videoUrl ,
                        binding!!.tittle.editText?.text.toString(),
                        imageUrl2!! ,
                        user!!.email.toString() ,
                        time = System.currentTimeMillis().toString()
                        )


                    Firebase.firestore.collection(VIDEO)
                        .document(video.tittle)
                        .set(video)
                        .addOnSuccessListener {
                            Firebase.firestore.collection(Firebase.auth.currentUser!!.email.toString()+ VIDEO)
                                .document()
                                .set(video)
                                .addOnSuccessListener {
                                    startActivity(Intent(this , HomeActivity::class.java))
                                    finish()
                                }
                            finish()
                        }
                        .addOnFailureListener {
                            Log.i("failure2" , "We fail at second step")
                        }

                }
                .addOnFailureListener {
                    Log.i("failure" , "We fail at first step")
                }



        }


    }
}