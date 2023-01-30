package com.example.gestion_roll

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gestion_roll.client.NewClient
import com.example.gestion_roll.delivery.NewDelivery
import com.example.gestion_roll.user.UserManagement
import com.example.gestion_roll.user.connection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccueilAdmin : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil_admin)

        val texte : TextView =  findViewById(R.id.text_id)
        val bouton : Button =  findViewById(R.id.button_deco)
        val buttonNewClient : Button =  findViewById(R.id.button_new_user)
        val buttonUserManagement : Button =  findViewById(R.id.button_user_management)
        val buttonNewDelivery : Button =  findViewById(R.id.button_new_delivery)

        val user = auth.currentUser
        texte.text = user?.email

        bouton.setOnClickListener{
            Firebase.auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }

        buttonNewClient.setOnClickListener{
            startActivity(Intent(this, NewClient::class.java))
        }

        buttonUserManagement.setOnClickListener{
            startActivity(Intent(this, UserManagement::class.java))
        }

        buttonNewDelivery.setOnClickListener{
            startActivity(Intent(this, NewDelivery::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()
        connection(this)
    }
}






