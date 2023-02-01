package com.example.gestion_roll.client

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await

data class Client(
    var name: String = "", var city: String = "",
    var tag : Int = 0, var cc : Int = 0, var ordinaire : Int = 0,
    var etagere : Int = 0, var rehausse : Int = 0) : java.io.Serializable
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


    /**
     * Cette fonction permet d'ajouter ou de retirer des rolls/etagères/réhausses sur le compte total du client
     * @param context Contexte de l'activité appelante (utile pour les toasts)
     * @param tag nombre de rolls tag ajoutés ou retirés du total
     * @param cc nombre de rolls cc ajoutés ou retirés du total
     * @param ordinaire nombre de rolls ordinaires ajoutés ou retirés du total
     * @param etagere nombre d'étageres ajoutés ou retirés du total
     * @param rehausse nombre de rehausses ajoutés ou retirés du total
     */
    fun addRoll(context: Context, tag: Int, cc : Int, ordinaire: Int, etagere: Int, rehausse: Int) {

        val query = FirebaseFirestore.getInstance().collection("clients").document(name)
        val batch = FirebaseFirestore.getInstance().batch()
        query.get().addOnSuccessListener { documents ->

            if (tag != 0) {
                batch.update(query, "tag", documents["tag"].toString().toInt() + tag)
            }
            if (cc != 0) {
                batch.update(query, "cc", documents["cc"].toString().toInt() + cc)
            }
            if (ordinaire != 0) {
                batch.update(query,"ordinaire",documents["ordinaire"].toString().toInt() + ordinaire)
            }
            if (etagere != 0) {
                batch.update(query, "etagere", documents["etagere"].toString().toInt() + etagere)
            }
            if (rehausse != 0) {
                batch.update(query, "rehausse", documents["rehausse"].toString().toInt() + rehausse)
            }

            batch.commit().addOnSuccessListener{
                Toast.makeText(context, "OK", Toast.LENGTH_SHORT).show()
            }
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



