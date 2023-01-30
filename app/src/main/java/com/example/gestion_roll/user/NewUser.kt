package com.example.gestion_roll.user

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.gestion_roll.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class NewUser : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_user)

        auth = Firebase.auth
        val db = Firebase.firestore

        //------------------ ENTREES VARIABLES ------------------

        val boutonConnecter : Button =  findViewById(R.id.button_connecter_nv_user)
        val loginEntree : EditText =  findViewById(R.id.login_nv_user)
        val modeAdmin : SwitchCompat = findViewById(R.id.switch_admin_nv_user)

        //------------------ BOUTON ------------------

        boutonConnecter.setOnClickListener {
            val loginBrut : String = loginEntree.text.toString()
            val isAdmin : Boolean = modeAdmin.isChecked

            if(loginBrut.isBlank())
                Toast.makeText(this, "Merci de renseigner le login !", Toast.LENGTH_SHORT).show()
            else {
                val user = hashMapOf(
                    "Login" to loginBrut,
                    "Admin" to isAdmin
                )

                val docRef =  db.collection("users").document(loginBrut)
                docRef.get()
                    .addOnSuccessListener {document ->
                        if (document.exists()) {
                            Toast.makeText(this, "Login déjà utilisé !", Toast.LENGTH_SHORT).show()
                        } else {
                            db.collection("tempUsers").document(loginBrut).set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "PB enregistrement nouvel user", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "PB vérif login", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        connection(this)
    }
}
