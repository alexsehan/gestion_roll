package com.example.gestion_roll.newDelivery

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.gestion_roll.Accueil
import com.example.gestion_roll.AccueilAdmin
import com.example.gestion_roll.client.Client
import com.example.gestion_roll.client.getClientByName
import com.example.gestion_roll.user.Users
import com.example.gestion_roll.user.isAdmin
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import java.util.*

data class Delivery(
    var date: Date = Date(), var livreur: String = Users().Login, var client : String = Client().name,
    var TAG: Int = 0, var CC: Int = 0, var ORDINAIRE: Int = 0,
    var ETAGERE: Int = 0, var REHAUSSE: Int = 0) {

    fun putInFirebase(context: Context) {
        if(TAG == 0 && CC == 0 && ORDINAIRE == 0 && ETAGERE == 0 && REHAUSSE == 0) {
            Toast.makeText(context, "Tous les champs roll sont nuls !", Toast.LENGTH_SHORT).show()
            return
        } else if(client == "") {
            Toast.makeText(context, "Pas de client sélectionné !", Toast.LENGTH_SHORT).show()
            return
        }

        val query = FirebaseFirestore.getInstance().collection("Deliveries")

        runBlocking {
            query.document().get().addOnSuccessListener { document ->
                if (document.exists()) {
                    Toast.makeText(context, "Nom déjà utilisé !", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                } else {
                    query.document().set(this@Delivery)
                        .addOnSuccessListener {
                            getClientByName(client).addRoll(context, TAG, CC, ORDINAIRE, ETAGERE, REHAUSSE)
                            isAdmin { list ->
                                if(list[0]){
                                    if (list[1]) {
                                        context.startActivity(Intent(context, AccueilAdmin::class.java))
                                    } else {
                                        context.startActivity(Intent(context, Accueil::class.java))
                                    }
                                }
                            }
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

    fun delete(context: Context) {

        val db = Firebase.firestore.collection("Deliveries")

        db.whereEqualTo("date", Timestamp(this.date)).get()
            .addOnSuccessListener {
                for(document in it.documents) {
                    db.document(document.id).delete()
                }
                //Toast.makeText(context, "Livraison supprimée !", Toast.LENGTH_SHORT).show()

                getClientByName(this.client).addRoll(context, -this.TAG, -this.CC, -this.ORDINAIRE, -this.ETAGERE, -this.REHAUSSE)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Oups erreur PB1, suppression impossible", Toast.LENGTH_SHORT).show()
            }


    }
}

