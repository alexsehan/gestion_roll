package com.example.gestion_roll.delivery

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.gestion_roll.R
import com.example.gestion_roll.client.Client
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FindDeliveryManagement : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_delivery_management)

        //------------------ ENTREES VARIABLES ------------------

        val clientName: TextView = findViewById(R.id.client_name)
        val clientInfo: TextView = findViewById(R.id.client_info)

        val tag: TextView = findViewById(R.id.tag)
        val cc: TextView = findViewById(R.id.cc)
        val ord: TextView = findViewById(R.id.ord)
        val eta: TextView = findViewById(R.id.eta)
        val reh: TextView = findViewById(R.id.reh)

        val button: Button = findViewById(R.id.button)
        val buttonPdf: Button = findViewById(R.id.button_pdf)

        //------------------ RECUPERATION DONNEES ------------------

         var clientData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("EXTRA_RESULT", Client::class.java)!!
         } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("EXTRA_RESULT") as Client
         }

         Firebase.firestore.collection("clients").document(clientData.name).addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if ((snapshot != null) && snapshot.exists()) {
                clientData = snapshot.toObject(Client::class.java)!!

                tag.text = clientData.tag.toString()
                cc.text = clientData.cc.toString()
                ord.text = clientData.ordinaire.toString()
                eta.text = clientData.etagere.toString()
                reh.text = clientData.rehausse.toString()
            }
        }

        //------------------ AFFICHAGE DES DONNEES ------------------

        clientName.text = clientData.name
        clientInfo.text = clientData.info
        tag.text = clientData.tag.toString()
        cc.text = clientData.cc.toString()
        ord.text = clientData.ordinaire.toString()
        eta.text = clientData.etagere.toString()
        reh.text = clientData.rehausse.toString()

        //------------------ BOUTONS ------------------

        //bouton pour voir toutes les livraisons d'un client
        button.setOnClickListener {
            val intent = Intent(this, AllDeliveriesByClient::class.java)
            intent.putExtra("EXTRA_RESULT", clientData as java.io.Serializable)
            startActivity(intent)
        }

        //Télécharge un récapitulatif en pdf des livraisons
        buttonPdf.setOnClickListener {
            clientData.saveDeliveriesPDF(this)
        }


    }
}