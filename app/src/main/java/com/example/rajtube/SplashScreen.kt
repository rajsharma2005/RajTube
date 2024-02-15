package com.example.rajtube

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            if (Firebase.auth.currentUser == null)
                startActivity(Intent(this , SignInActivity::class.java))
            else
                startActivity(Intent(this , HomeActivity::class.java))
            finish()
        } , 2000)
    }
}
