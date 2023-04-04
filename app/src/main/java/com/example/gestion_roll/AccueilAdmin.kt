package com.example.gestion_roll

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.gestion_roll.client.NewClient2
import com.example.gestion_roll.delivery.CityDeliveryManagement
import com.example.gestion_roll.newDelivery.NewDelivery
import com.example.gestion_roll.user.UserManagement
import com.example.gestion_roll.user.connection
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccueilAdmin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accueil_admin)

        val bouton : Button =  findViewById(R.id.button_deco)
        val buttonNewClient : Button =  findViewById(R.id.button_new_user)
        val buttonUserManagement : Button =  findViewById(R.id.button_user_management)
        val buttonNewDelivery : Button =  findViewById(R.id.button_new_delivery)
        val buttonDeliveryManagement : Button =  findViewById(R.id.button_delivery_management)

        bouton.setOnClickListener{
            Firebase.auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
        }

        buttonNewClient.setOnClickListener{
            startActivity(Intent(this, NewClient2::class.java))
        }

        buttonUserManagement.setOnClickListener{
            startActivity(Intent(this, UserManagement::class.java))
        }

        buttonNewDelivery.setOnClickListener{
            startActivity(Intent(this, NewDelivery::class.java))
        }

        buttonDeliveryManagement.setOnClickListener{
            startActivity(Intent(this, CityDeliveryManagement::class.java))
        }
    }

    public override fun onStart() {
        super.onStart()
        connection(this)
    }
}






