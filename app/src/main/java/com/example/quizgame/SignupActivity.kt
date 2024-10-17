package com.example.quizgame

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizgame.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    lateinit var signupBinding : ActivitySignupBinding

    var auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view = signupBinding.root

        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        signupBinding.buttonSignup.setOnClickListener {

            val email = signupBinding.editTextSignupEmail.text.toString()
            val password = signupBinding.editTextSignupPassword.text.toString()

            signupWithFirebase(email, password)


        }



    }

    fun signupWithFirebase(email: String, password: String){

        signupBinding.progressBarSignup.visibility = View.VISIBLE
        signupBinding.buttonSignup.isClickable = false

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if(task.isSuccessful){

                Toast.makeText(applicationContext, "Your account has been created", Toast.LENGTH_SHORT).show()
                finish()
                signupBinding.progressBarSignup.visibility = View.INVISIBLE
                signupBinding.buttonSignup.isClickable = true

            }else{
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()

            }

        }

    }

}