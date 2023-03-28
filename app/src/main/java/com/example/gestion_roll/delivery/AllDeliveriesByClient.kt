package com.example.gestion_roll.delivery

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_roll.R
import com.example.gestion_roll.client.Client

class AllDeliveriesByClient : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_deliveries_by_client)

        //------------------ ENTREES VARIABLES ------------------
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        //------------------ RECUPERATION DONNEES ------------------

        val clientData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("EXTRA_RESULT", Client::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("EXTRA_RESULT") as Client
        }

        //------------------ AFFICHAGE ------------------

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val adapter = AdapterClientDeliveryManagement(this, clientData.getAllDeliveriesByClient())
        recyclerView.adapter = adapter

    }
}