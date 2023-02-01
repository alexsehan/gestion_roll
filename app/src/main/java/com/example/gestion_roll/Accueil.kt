package com.example.gestion_roll

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gestion_roll.client.NewClient
import com.example.gestion_roll.user.connection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Accueil : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil)

        val texte : TextView =  findViewById(R.id.text_id)
        val bouton : Button =  findViewById(R.id.button_deco)
        val buttonNewClient : Button =  findViewById(R.id.button_new_user)

        val user = auth.currentUser
        texte.text = user?.email

        buttonNewClient.setOnClickListener{
            startActivity(Intent(this, NewClient::class.java))
        }

        bouton.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        connection(this)
    }
}