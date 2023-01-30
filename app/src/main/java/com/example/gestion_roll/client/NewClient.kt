package com.example.gestion_roll.client

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gestion_roll.R
import com.example.gestion_roll.user.connection

class NewClient : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_client)

        val buttonNewClient: Button = findViewById(R.id.button_new_user)
        val nameNewClient: EditText = findViewById(R.id.type_nv_client)
        val cityNewClient: EditText = findViewById(R.id.ville_nv_client)

        buttonNewClient.setOnClickListener {
            val name: String = nameNewClient.text.toString().uppercase()
            val city: String = cityNewClient.text.toString().uppercase()

            if (name.isBlank())
                Toast.makeText(this, "Merci de renseigner le nom !", Toast.LENGTH_SHORT).show()
            else if (city.isBlank())
                Toast.makeText(this, "Merci de renseigner la ville !", Toast.LENGTH_SHORT).show()
            else {
                val client = Client(name, city)
                client.putInFirebase(this)
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        connection(this)
    }
}

