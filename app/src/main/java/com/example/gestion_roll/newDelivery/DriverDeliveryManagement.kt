package com.example.gestion_roll.newDelivery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_roll.R
import com.example.gestion_roll.user.getUsersFromFirestore

class DriverDeliveryManagement : AppCompatActivity(), OnResultCallback {

    override fun setResult(data: Intent) {
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_delivery_management)

        val searchInput = findViewById<EditText>(R.id.driver_search_management)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        var data = getUsersFromFirestore(null)

        val adapter = AdapterNewDeliveryDriver(this, data, this)
        recyclerView.adapter = adapter

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                data = getUsersFromFirestore(s.toString())
                adapter.updateData(data)
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable) {}
        })
    }
}
