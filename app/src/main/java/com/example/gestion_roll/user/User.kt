package com.example.gestion_roll.user

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.gestion_roll.Accueil
import com.example.gestion_roll.AccueilAdmin
import com.example.gestion_roll.AccueilBreak
import com.example.gestion_roll.MainActivity
import com.example.gestion_roll.client.NewClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.io.Serializable


data class Users (
    val Admin: Boolean = false,
    val Break: Boolean = false,
    val ID: String = "",
    val Login: String = ""
) : Serializable

//retourne true si admin et false sinon
fun isAdmin(callback: (ArrayList<Boolean>) -> Unit) {
    val auth: FirebaseAuth = Firebase.auth

    Firebase.firestore.collection("users")
        .whereEqualTo("ID", auth.uid.toString())
        .get()
        .addOnSuccessListener{ documents ->
            for (document in documents) {
                if (document["Admin"] == true) {
                    callback(arrayListOf(true, true))
                    return@addOnSuccessListener
                }
            }
            callback(arrayListOf(true, false))
            return@addOnSuccessListener
        }
    callback(arrayListOf(false, false))
}

fun setBreak(user: Users, context: Context) {
    runBlocking {
        val query = FirebaseFirestore.getInstance().collection("users").document(user.Login)
        query.get().addOnSuccessListener { documents ->
            if(documents["Login"] == "patouche") {
                Toast.makeText(context, "Impossible", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }
            if(documents["Break"] as Boolean) {
                query.update("Break", false)
            } else {
                query.update("Break", true)
            }
        }
    }
}

fun getBreak(user: Users) : Boolean {
    var value = false
    runBlocking {
        val query = FirebaseFirestore.getInstance().collection("users").document(user.Login).get()
        val result = query.await()
        if(result["Admin"] as Boolean) {
            value = true
        }
    }
    return value
}

/**
 * Cette fonction permet de retourner la liste des utilisateurs enregistrés dans la BDD Firebase
 * @param search Ce que l'utilisateur a renseigné pour trouver l'utilisateur cherché.
 * @return liste entière (ou partielle selon search) des utilisateurs enregistrés dans le BDD Firebase
 */
fun getUsersFromFirestore(search: String?) : List<Users> {

    val users = mutableListOf<Users>()
    val search2 = search?.uppercase()

    runBlocking {
        val query = FirebaseFirestore.getInstance().collection("users")
        val result : Query = if(search2==null) {
            query
        } else {
            query.orderBy("Login").startAt(search2)
        }

        for (document in result.get().await().documents) {
            val user = document.toObject(Users::class.java)
            user?.let { users.add(it) }
        }
    }
    return users
}

/**
 * Cette fonction permet de vérifier si l'utilisateur connecté n'est pas mis en pause et si il est dans
 * une mauvaise activité selon son rôle attribué (Admin ou non). Si oui, il est redirigé vers son activité associé
 * @param context Contexte de l'activité appelante (utile pour les redirections)
 */
fun connection(context: Context) {
    runBlocking {
        val query = FirebaseFirestore.getInstance().collection("users").whereEqualTo("ID", Firebase.auth.uid.toString())
        val result = query.get().await()
        for (document in result.documents) {
            val user = document.toObject(Users::class.java)
            if (user != null) {
                if(user.Break) {
                    if(context !is AccueilBreak){
                        val intent = Intent(context, AccueilBreak::class.java)
                        startActivity(context, intent, null)
                        return@runBlocking
                    }
                } else if(user.Admin) {
                    if(context is Accueil){
                        val intent = Intent(context, AccueilAdmin::class.java)
                        startActivity(context, intent, null)
                        return@runBlocking
                    }
                } else {
                    if(context is AccueilAdmin || context is UserManagement || context is NewUser ||
                        context is NewClient){
                        val intent = Intent(context, Accueil::class.java)
                        startActivity(context, intent, null)
                        return@runBlocking
                    }
                }
            } else {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(context, intent, null)
                return@runBlocking
            }
        }
    }
}

fun getUserFromID() : Users {
    var user = Users()
    runBlocking {
        val query = FirebaseFirestore.getInstance().collection("users")
            .whereEqualTo("ID", Firebase.auth.uid.toString())
        val result = query.get().await()
        for (document in result.documents) {
            user = document.toObject(Users::class.java)!!
            return@runBlocking
        }
    }
    return user
}
