package com.example.rajtube.Activity


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.bumptech.glide.Glide
import com.example.rajtube.Models.User
import com.example.rajtube.Models.Video
import com.example.rajtube.R
import com.example.rajtube.Utills.SUBS
import com.example.rajtube.Utills.USER_NODE
import com.example.rajtube.Utills.VIDEO
import com.example.rajtube.databinding.ActivityVideoPlayerBinding
import com.github.marlonlom.utilities.timeago.TimeAgo
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class VideoPlayerActivity : AppCompatActivity() {
    private var binding: ActivityVideoPlayerBinding? = null
    private lateinit var playerView: PlayerView
    private lateinit var player: ExoPlayer
    private var isLiked: Boolean = false
    private var isDisLiked: Boolean = false
    private var likeCount: Int = 0
    private var view: Int = 3
    private var dislikeCount: Int = 0
    private lateinit var title: String
    private lateinit var video: Video
    private var isSubs: Boolean = false
    private var subs: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)


        //getting the values from intent
        val videoUrl = intent.getStringExtra("videoUrl")
        title = intent.getStringExtra("title")!!
        val email = intent.getStringExtra("email")
        val time = intent.getStringExtra("time")


        //seeting the player
        playerView = findViewById(R.id.player_view)
        player = ExoPlayer.Builder(this@VideoPlayerActivity).build()
        playerView.player = player

        val mediaItem = MediaItem.Builder()
            .setUri(videoUrl)
            .setMimeType(MimeTypes.APPLICATION_MP4)
            .build()

        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()

        //for updating the viewsF

        CoroutineScope(Dispatchers.IO).launch {


            Firebase.firestore.collection(VIDEO)
                .document(title)
                .get()
                .addOnSuccessListener {
                    val vid: Video = it.toObject()!!
                    Log.i("view1", vid.views.toString())
                    view = vid.views
                    Log.i("view2", view.toString())
                    view += 1
                    updateViews()
                }
                .addOnFailureListener {
                    Log.i("view1234", it.localizedMessage!!)
                }

            Log.i("view3", view.toString())

            //setting up function to show like
            retrieveData()

            binding!!.like.setOnClickListener {
                settingLike()
                updateLike()
            }

            binding!!.title.text = title
            try {
                val text = TimeAgo.using(time!!.toLong())
                binding!!.time.text = text
            } catch (e: Exception) {
                binding!!.time.text = ""
            }

            binding!!.comments.setOnClickListener {
                Toast.makeText(
                    this@VideoPlayerActivity, "Sorry , You can't access this right now . " +
                            "We are working on this", Toast.LENGTH_LONG
                ).show()
            }

            binding!!.dislikeBtn.setOnClickListener {
                if (!isDisLiked) {
                    binding!!.dislike.setImageResource(R.drawable.unlike)
                    dislikeCount += 1
                    isLiked = true
                    settingLike()
                    isDisLiked = true
                } else {
                    binding!!.dislike.setImageResource(R.drawable.dislike)
                    dislikeCount -= 1
                    isLiked = false
                    isDisLiked = false
                }

                updateDisLike()
            }

            binding!!.share.setOnClickListener {
                shareVideo(videoUrl!!)
            }


            if (email != null) {
                Firebase.firestore.collection(USER_NODE)
                    .document(email)
                    .get()
                    .addOnSuccessListener {
                        if (!it.exists()) {
                            Log.e("error234", "it does not exists")
                        } else {
                            val user = it.toObject<User>()!!
                            Glide.with(this@VideoPlayerActivity)
                                .load(user.image)
                                .placeholder(R.drawable.user)
                                .into(binding!!.userProfileImage)

                            binding!!.name.text = user.name


                            Firebase.firestore.collection(USER_NODE)
                                .document(user.email.toString())
                                .get()
                                .addOnSuccessListener {
                                    val youTuber: User = it.toObject<User>()!!
                                    binding!!.subscribeCount.text =
                                        youTuber.subs.toString() + "  Subscribe"
                                    subs = youTuber.subs
                                    Log.i("subs", subs.toString())

                                    binding!!.name.setOnClickListener {
                                        var intent = Intent(
                                            this@VideoPlayerActivity,
                                            ProfileActivity::class.java
                                        )
                                        intent.putExtra("user", youTuber.email.toString())
                                        Log.i("email", youTuber.email.toString())
                                        intent.putExtra("isSubs", isSubs)
                                        startActivity(intent)
                                    }
                                }


                            Firebase.firestore.collection(Firebase.auth.currentUser!!.email + SUBS)
                                .whereEqualTo("email", user.email)
                                .get()
                                .addOnSuccessListener {
                                    if (it.documents.size == 0) {
                                        isSubs = false
                                    } else {
                                        isSubs = true
                                        binding!!.subscribe.text = "UnSubscribe"
                                    }
                                }
                            binding!!.subscribe.setOnClickListener {
                                if (isSubs) {
                                    subs -= 1
                                    Firebase.firestore.collection(USER_NODE)
                                        .document(user.email.toString())
                                        .update("subs", subs)

                                    Firebase.firestore.collection(
                                        Firebase.auth.currentUser!!.email + SUBS
                                    ).whereEqualTo("email", user.email)
                                        .get()
                                        .addOnSuccessListener {
                                            Firebase.firestore.collection(
                                                Firebase.auth.currentUser!!.email + SUBS
                                            ).document(it.documents.get(0).id)
                                                .delete()
                                            binding!!.subscribe.text = "Subscribe"
                                            isSubs = false
                                        }
                                } else {
                                    subs += 1
                                    Firebase.firestore.collection(USER_NODE)
                                        .document(user.email.toString())
                                        .update("subs", subs)
                                    Firebase.firestore
                                        .collection(Firebase.auth.currentUser!!.email + SUBS)
                                        .document(user.email.toString())
                                        .set(user)
                                        .addOnSuccessListener {
                                            isSubs = true
                                            binding!!.subscribe.text = "UnSubscribe"
                                        }
                                }
                            }
                        }

                    }
                    .addOnFailureListener {
                        Log.i("error", it.localizedMessage!!)
                    }
            }


        }


    }

    private fun settingLike() {
        if (!isLiked) {
            binding!!.like.setImageResource(R.drawable.redlike)
            likeCount += 1
            isLiked = true
        } else {
            binding!!.like.setImageResource(R.drawable.like)
            likeCount -= 1
            isLiked = false
        }
    }

    private fun updateViews() {
        Firebase.firestore
            .collection(VIDEO)
            .document(title)
            .update("views", view)
            .addOnSuccessListener {
                binding!!.views.text = view.toString()
            }
    }

    private fun updateLike() {
        Firebase.firestore.collection(VIDEO)
            .document(title)
            .update("like", likeCount)
            .addOnSuccessListener {
                retrieveData()
            }
            .addOnFailureListener {
                Log.i("ho gya", it.localizedMessage)
            }
    }

    private fun updateDisLike() {
        Firebase.firestore.collection(VIDEO)
            .document(title)
            .update("disLike", dislikeCount)
            .addOnSuccessListener {
                retrieveData()
            }
            .addOnFailureListener {
                Log.i("ho gya", it.localizedMessage)
            }
    }


    private fun retrieveData() {
        Firebase.firestore
            .collection(VIDEO)
            .document(title)
            .get()
            .addOnSuccessListener {
                if (!it.exists()) {
                    Log.e("error", "it does not exists")
                }
                video = it.toObject<Video>()!!
                likeCount = video.like
                dislikeCount = video.disLike


                binding!!.likeNum.text = likeCount.toString()
                binding!!.disLikeNum.text = dislikeCount.toString()

            }
            .addOnFailureListener {
                it.localizedMessage?.let { it1 -> Log.e("error", it1) }
            }
    }

    private fun shareVideo(videoUri: String) {
        val videoFile = Uri.parse(videoUri)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, videoFile)
            type = "video/*"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (player.isPlaying) {
            player.stop()
        }
    }


}

