package com.example.quizgame

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizgame.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root

        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainBinding.buttonSignout.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()

            val googleSignInClient = GoogleSignIn.getClient(this,gso)
            googleSignInClient.signOut().addOnCompleteListener { task ->

                if(task.isSuccessful){
                    Toast.makeText(applicationContext,"Signout is successfull", Toast.LENGTH_LONG).show()
                }
            }

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()

        }

        mainBinding.buttonStartQuiz.setOnClickListener {

            val intetn = Intent(this, QuizActivity::class.java)
            startActivity(intetn)

        }
    }
}