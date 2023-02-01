package com.example.gestion_roll.newDelivery

import android.content.Context
import android.widget.Toast
import com.example.gestion_roll.client.Client
import com.example.gestion_roll.user.Users
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.runBlocking
import java.util.*

data class Delivery(
    var date: Date = Date(), var livreur: Users = Users(), var client : Client = Client(),
    var TAG: Int = 0, var CC: Int = 0, var ORDINAIRE: Int = 0,
    var ETAGERE: Int = 0, var REHAUSSE: Int = 0) {

    fun putInFirebase(context: Context) {
        if(TAG == 0 && CC == 0 && ORDINAIRE == 0 && ETAGERE == 0 && REHAUSSE == 0) {
            Toast.makeText(context, "Tous les champs roll sont nuls !", Toast.LENGTH_SHORT).show()
            return
        } else if(client.name == "") {
            Toast.makeText(context, "Pas de client sélectionné !", Toast.LENGTH_SHORT).show()
            return
        }

        val query = FirebaseFirestore.getInstance().collection("Deliveries").document()

        runBlocking {
            query.get().addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(context, "Nom déjà utilisé !", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                } else {
                    query.set(this@Delivery)
                        .addOnSuccessListener {
                            client.addRoll(context, TAG, CC, ORDINAIRE, ETAGERE, REHAUSSE)
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
