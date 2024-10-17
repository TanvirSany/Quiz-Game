package com.example.quizgame

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.quizgame.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding: ActivityLoginBinding

    var auth : FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var googleSigninClient:GoogleSignInClient

    lateinit var activityResultLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val textOfGoogleButton = loginBinding.buttonGoogleSignin.getChildAt(0) as TextView
        textOfGoogleButton.text = "Continue with google"
        textOfGoogleButton.setTextColor(Color.BLACK)
        textOfGoogleButton.textSize = 16F


        registerActivityForGoogleSignin()

        loginBinding.buttonSignin.setOnClickListener {

            val userEmail = loginBinding.editTextLoginEmail.text.toString()
            val userPassword = loginBinding.editTextLoginPassword.text.toString()

            signInUser(userEmail,userPassword)


        }

        loginBinding.buttonGoogleSignin.setOnClickListener{

            signInGoogle()

        }

        loginBinding.textViewSignup.setOnClickListener {

            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)

        }

        loginBinding.textViewForgotPassword.setOnClickListener {

            val intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)

        }

    }

    private fun signInGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("44331706111-78uh55rkp7j9vb3ma1q5g9kefqaqjfa5.apps.googleusercontent.com")
            .requestEmail().build()

        googleSigninClient = GoogleSignIn.getClient(this,gso)

        signIn()
    }

    private fun signIn() {
        val signInIntent : Intent = googleSigninClient.signInIntent

        activityResultLauncher.launch(signInIntent)

    }

    private fun registerActivityForGoogleSignin(){

        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                val resultCode = result.resultCode
                val data = result.data

                if(resultCode == RESULT_OK && data != null){
                    val task : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                    firebaseSignInWithGoogle(task)
                }


        })

    }

    private fun firebaseSignInWithGoogle(task: Task<GoogleSignInAccount>) {

        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext, "Welcome to Quiz Game", Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(account)
        }catch(e: ApiException){
            Toast.makeText(applicationContext, e.localizedMessage, Toast.LENGTH_SHORT).show()

        }
    }

    //retrieve data
    private fun firebaseGoogleAccount(account: GoogleSignInAccount) {

        val authCredential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(authCredential).addOnCompleteListener { task ->
            if(task.isSuccessful){
                //val user  = auth.currentUser

            }
        }

    }


    fun signInUser(userEmail : String, userPassword : String){

        auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Toast.makeText(applicationContext, "Welcome to quiz game", Toast.LENGTH_SHORT ).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext, task.exception?.localizedMessage, Toast.LENGTH_SHORT ).show()

            }
        }

    }

    override fun onStart() {
        super.onStart()

        val user = auth.currentUser
        if(user != null){
            Toast.makeText(applicationContext, "Welcome to quiz game",Toast.LENGTH_SHORT).toString()
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}