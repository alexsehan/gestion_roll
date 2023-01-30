package com.example.gestion_roll.delivery

import android.content.Context
import android.widget.Toast
import com.example.gestion_roll.client.Client
import com.example.gestion_roll.user.Users
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import java.util.Date

data class Delivery(
    var id: Long = 0,
    var date: Date = Date(), var livreur: Users = Users(), var client : Client = Client(),
    var TAG: Number = 0, var CC: Number = 0, var ORDINAIRE: Number = 0) {


    fun putInFirebase(context: Context) {
        val query = FirebaseFirestore.getInstance().collection("livraisons").document(id.toString())

        runBlocking {
            query.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(context, "Nom déjà utilisé !", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                } else {
                    query.set(this@Delivery)
                        .addOnSuccessListener {
                            Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Problem num. 1 NewClient", Toast.LENGTH_SHORT).show()
                            return@addOnFailureListener
                        }
                }
                return@addOnSuccessListener
            }
            return@runBlocking
        }
    }
}
