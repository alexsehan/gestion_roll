package com.example.gestion_roll.client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.gestion_roll.R
import com.example.gestion_roll.user.connection

class NewClient2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_client2)

        val buttonNewClient: Button = findViewById(R.id.button_new_user)
        val nameNewClient: EditText = findViewById(R.id.type_nv_client)
        val cityNewClient: EditText = findViewById(R.id.ville_nv_client)
        val numNewClient: EditText = findViewById(R.id.num_nv_client)
        val infosNewClient: EditText = findViewById(R.id.facultatif_nv_client)

        buttonNewClient.setOnClickListener {
            val type: String = nameNewClient.text.toString().uppercase()
            val city: String = cityNewClient.text.toString().uppercase()
            val num: String = numNewClient.text.toString().uppercase()
            val infos: String = infosNewClient.text.toString().uppercase()

            if (city.isBlank())
                Toast.makeText(this, "Merci de renseigner la ville !", Toast.LENGTH_SHORT).show()
            else if (num.isBlank())
                Toast.makeText(this, "Merci de renseigner le numéro !", Toast.LENGTH_SHORT).show()
            else {
                val client = Client("$num $type $city", city, num, infos, type)
                client.putInFirebase(this)
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        connection(this)
    }
}
