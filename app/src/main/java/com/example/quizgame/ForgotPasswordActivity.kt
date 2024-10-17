package com.example.quizgame

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizgame.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var forgotBindin: ActivityForgotPasswordBinding

    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        forgotBindin = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = forgotBindin.root

        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        forgotBindin.buttonReset.setOnClickListener {
            val userEmail = forgotBindin.editTextForgotEmail.text.toString()

            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task ->
                if(task.isSuccessful){

                    Toast.makeText(this,"We sent a password reset main to your email address", Toast.LENGTH_SHORT).show()
                    finish()

                }else{
                    Toast.makeText(this,task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()

                }
            }

        }

    }
}