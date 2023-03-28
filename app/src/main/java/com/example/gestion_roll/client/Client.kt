package com.example.gestion_roll.client

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.gestion_roll.Accueil
import com.example.gestion_roll.AccueilAdmin
import com.example.gestion_roll.newDelivery.Delivery
import com.example.gestion_roll.user.isAdmin
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


data class Client(
    var name: String = "", var city: String = "",
    var num : String = "", var info : String = "",
    var type : String = "",
    var tag : Int = 0, var cc : Int = 0, var ordinaire : Int = 0,
    var etagere : Int = 0, var rehausse : Int = 0) : java.io.Serializable
{
    fun getAllDeliveriesByClient() :  MutableList<Delivery> {
        val deliveries = mutableListOf<Delivery>()

        val query = Firebase.firestore.collection("Deliveries")
            .whereEqualTo("client", name)
            .get()

        runBlocking {
            for (document in query.await().documents) {
                val delivery = document.toObject(Delivery::class.java)
                delivery?.let { deliveries.add(it) }
            }
        }
        return deliveries
    }

    /**
     * Cette fonction permet de sauvegarder dans un fichier pdf le récapitulatif de toutes les
     * livraisons d'un client.
     * @param context Contexte de l'activité appelante (utile pour les toasts)
     */
    fun saveDeliveriesPDF(context: Context) {

        try {
            // Créer un nouveau document PDF
            val document = Document()

            // Ouvrir le document pour y écrire
            val directory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val date = SimpleDateFormat("dd-MM-yyyy-HH-mm", Locale.getDefault()).format(Date())
            val file = File(directory, this.name + " - " + date + ".pdf")
            FileOutputStream(file).use { fos ->
                PdfWriter.getInstance(document, fos)
                document.open()

                PdfWriter.getInstance(document, FileOutputStream(file))
                document.open()

                // Intro
                val paragraphIntro = Paragraph(
                    "Historique des livraisons de : " + this.name + " à la date du " +
                            SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                )

                document.add(paragraphIntro)

                document.add(Paragraph(" "))
                val separator = LineSeparator()
                separator.lineWidth = 1f
                document.add(separator)

                val listDeliveries = this.getAllDeliveriesByClient()
                document.add(Paragraph(" "))
                val table = PdfPTable(7)
                table.widthPercentage = 100f // Définit la largeur de la table à 100% de la page

                table.addCell("DATE")
                table.addCell("LIVREUR")
                table.addCell("TAG")
                table.addCell("CC")
                table.addCell("ORDINAIRE")
                table.addCell("ETAGERE")
                table.addCell("REHAUSSE")


                for (i in listDeliveries.indices) {
                    table.addCell(
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                            listDeliveries[i].date
                        )
                    )
                    table.addCell(listDeliveries[i].livreur.uppercase())
                    table.addCell(listDeliveries[i].TAG.toString())
                    table.addCell(listDeliveries[i].CC.toString())
                    table.addCell(listDeliveries[i].ORDINAIRE.toString())
                    table.addCell(listDeliveries[i].ETAGERE.toString())
                    table.addCell(listDeliveries[i].REHAUSSE.toString())
                }

                table.addCell("TOTAL")
                table.addCell("")
                table.addCell(this.tag.toString())
                table.addCell(this.cc.toString())
                table.addCell(this.ordinaire.toString())
                table.addCell(this.etagere.toString())
                table.addCell(this.rehausse.toString())

                document.add(table)

                // Fermer le document
                document.close()

                // Demander les autorisations nécessaires pour écrire des fichiers sur le stockage externe
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        1
                    )
                    return
                }

                if (file.exists()) {
                    Toast.makeText(context, "Enregistré avec succès", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Erreur voir admin", Toast.LENGTH_SHORT).show()
        }


    }

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

fun getClientByName(name : String) : Client
{
    var client = Client()

    val query = FirebaseFirestore.getInstance().collection("clients")
        .whereEqualTo("name", name)

    runBlocking {
        for (document in query.get().await().documents) {
            client = document.toObject(Client::class.java)!!
        }
    }

    return client
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