package com.example.rajtube


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.rajtube.Models.User
import com.example.rajtube.databinding.ActivityLogInBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class LogInActivity : AppCompatActivity() {
    private var binding : ActivityLogInBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

       binding!!.login.setOnClickListener{
            if (binding!!.emailInput.editText!!.text.toString().equals("") or
                binding!!.passwordInput.editText!!.text.toString().equals("")
                ){
                Toast.makeText(this , "Please fill all the details" , Toast.LENGTH_SHORT).show()
            }else{
                //Toast.makeText(this , "At least this is working" , Toast.LENGTH_SHORT).show()

                var user = User(binding!!.emailInput.editText!!.text.toString(),
                    binding!!.passwordInput.editText!!.text.toString())
                Firebase.auth.signInWithEmailAndPassword(user.email!!,user.password!!)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            startActivity(Intent(this , HomeActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this , it.exception!!.localizedMessage , Toast.LENGTH_LONG).show()

                        }
                    }
                    .addOnFailureListener {
                        Log.e("error" , it.message.toString())

                    }
            }
        }
    }
}