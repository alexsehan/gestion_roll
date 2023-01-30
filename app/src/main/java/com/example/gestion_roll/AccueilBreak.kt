package com.example.gestion_roll

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.gestion_roll.user.connection
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccueilBreak : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil_break)

        val buttonDeco : Button =  findViewById(R.id.button_deco)

        buttonDeco.setOnClickListener{
            Firebase.auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()
        connection(this)
    }
}