package com.example.gestion_roll

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.gestion_roll.user.connection
import com.example.gestion_roll.user.isAdmin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //------------------ ENTREES VARIABLES ------------------

        val boutonConnecter : Button =  findViewById(R.id.button_connecter)
        val loginEntree : EditText =  findViewById(R.id.login_connexion)
        val mdpEntree: TextView =  findViewById(R.id.mdp_connexion)

        val boutonHelperLogin : ImageButton =  findViewById(R.id.button_help_login)
        val boutonHelperMdp : ImageButton = findViewById(R.id.bouton_help_mdp)
        val boutonHelperPremiereConnection : ImageButton = findViewById(R.id.bouton_help_prem_connec)
        val modePremiereConnection : SwitchCompat = findViewById(R.id.switch_premiere_connection)

        auth = Firebase.auth
        val db = Firebase.firestore

        //------------------ HELPERS ------------------

        val builder1 = AlertDialog.Builder(this)
        builder1.setTitle("Un login ?")
        builder1.setMessage("Souvent un prénom/surnom : alex, dominique2, etc.\nUn oublie? " +
                "Il faudrait voir un administrateur !")
        builder1.setCancelable(true)
        builder1.setPositiveButton(
            "C'est noté !"
        ) { dialog, _ -> dialog.cancel() }

        val builder2 = AlertDialog.Builder(this)
        builder2.setTitle("Un mot-de-passe ?")
        builder2.setMessage("Souvent une date de naissance au format JJMMAAAA !")
        builder2.setCancelable(true)
        builder2.setPositiveButton(
            "C'est noté !"
        ) { dialog, _ -> dialog.cancel() }

        val builder3 = AlertDialog.Builder(this)
        builder2.setTitle("Première connection ?")
        builder2.setMessage("A cocher seulement si c'est la première connection !")
        builder2.setCancelable(true)
        builder2.setPositiveButton(
            "C'est noté !"
        ) { dialog, _ -> dialog.cancel() }

        val popLogin = builder1.create()
        val popMdp = builder2.create()
        val popPremiereConnection = builder3.create()

        boutonHelperLogin.setOnClickListener{
            popLogin.show()
        }
        boutonHelperMdp.setOnClickListener{
            popMdp.show()
        }
        boutonHelperPremiereConnection.setOnClickListener{
            popPremiereConnection.show()
        }

        //------------------ BOUTONS ------------------

        boutonConnecter.setOnClickListener {
            val loginBrut : String = loginEntree.text.toString()
            val mdp : String = mdpEntree.text.toString()
            val isFirst : Boolean = modePremiereConnection.isChecked

            if(loginBrut.isBlank())
                Toast.makeText(this, "Merci de renseigner le login !", Toast.LENGTH_SHORT).show()
            else if(mdp.isBlank())
                Toast.makeText(this, "Merci de renseigner le mot-de-passe !", Toast.LENGTH_SHORT).show()
            else {
                val login = loginBrut.plus("@serres.com")
                if(mdp.length < 6)
                    Toast.makeText(this, "Le mot-de-passe renseigné doit être supérieur à 6 caractères !", Toast.LENGTH_SHORT).show()
                else if (isFirst) {
                    db.collection("tempUsers").document(loginBrut).get()
                        .addOnSuccessListener { document ->
                            if (document.exists()) {
                              auth.createUserWithEmailAndPassword(
                                    document["Login"].toString().plus("@serres.com"),mdp
                                )
                                    .addOnSuccessListener {

                                       val user = hashMapOf(
                                            "ID"    to auth.currentUser?.uid,
                                            "Login" to loginBrut,
                                            "Admin" to document["Admin"],
                                            "Break" to false
                                        )

                                        db.collection("users").document(loginBrut).set(user)
                                            .addOnSuccessListener {
                                                db.collection("tempUsers").document(loginBrut).delete()
                                                    .addOnSuccessListener {
                                                        Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
                                                        startActivity( Intent(this, Accueil::class.java))
                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(this, "PB4 voir avec un admin", Toast.LENGTH_SHORT).show()
                                                    }
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(this, "PB3 voir avec un admin", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "PB2 voir avec un admin", Toast.LENGTH_SHORT).show()
                                    }
                            } else
                                Toast.makeText(this, "TempUser pas trouvé !", Toast.LENGTH_SHORT).show()
                        }
                }

                else {
                    auth.signInWithEmailAndPassword(login, mdp)
                        .addOnCompleteListener(this) { task ->
                            if(task.isSuccessful) {

                                isAdmin { list ->
                                    if(list[0]){
                                        if (list[1]) {
                                            startActivity(Intent(this, AccueilAdmin::class.java))
                                            finish()
                                        } else {
                                            startActivity(Intent(this, Accueil::class.java))
                                            finish()
                                        }
                                    }
                                }
                            } else
                                Toast.makeText(baseContext, "Aïe, c'est pas bon !",Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        connection(this)
    }
}


