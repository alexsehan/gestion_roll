package com.example.gestion_roll.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gestion_roll.R

class UserManagement : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val buttonNewUser = findViewById<Button>(R.id.button_new_user)

        buttonNewUser.setOnClickListener {
            startActivity(Intent(this, NewUser::class.java))
        }

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        val data = getUsersFromFirestore(null)
        val adapter = MyAdapter(this, data)
        recyclerView.adapter = adapter
    }
}