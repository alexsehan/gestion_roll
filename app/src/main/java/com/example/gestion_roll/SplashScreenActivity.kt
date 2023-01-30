package com.example.gestion_roll

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gestion_roll.user.isAdmin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = Firebase.auth

        val currentUser = auth.currentUser

        if(currentUser != null) {
            isAdmin { list ->
                if(list[0]){
                    if (list[1]) {
                        startActivity(Intent(this, AccueilAdmin::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this, Accueil::class.java))
                        finish()
                    }
                }
            }
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}