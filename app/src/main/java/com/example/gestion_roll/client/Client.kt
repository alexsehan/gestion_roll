package com.example.gestion_roll.client

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

data class Client(
    var name: String = "", var city: String = "",
    var tag : Int = 0, var cc : Int = 0, var ordinaire : Int = 0)
{
    fun putInFirebase(context: Context) {
        val query = FirebaseFirestore.getInstance().collection("clients").document(name)

        runBlocking {
            query.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(context, "Nom déjà utilisé !", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                } else {
                    query.set(this@Client)
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

fun getClientsFromFirestore(search: String?) : List<Client> {

    val clients = mutableListOf<Client>()
    val search2 = search?.uppercase()

    runBlocking {
        val query = FirebaseFirestore.getInstance().collection("clients")
        val result : Query = if(search2==null) {
            query
        } else {
            query.orderBy("city").startAt(search2)
        }

        for (document in result.get().await().documents) {
            val client = document.toObject(Client::class.java)
            client?.let { clients.add(it) }
        }
    }
    return clients
}


