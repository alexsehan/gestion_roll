package com.example.gestion_roll.delivery

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.gestion_roll.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.text.DateFormat.getDateInstance
import java.util.*

class NewDelivery : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_delivery)

        //------------------ ENTREES VARIABLES ------------------

        val buttonClient: Button = findViewById(R.id.button_client_new_delivery)
        val textClient: TextView = findViewById(R.id.client_name_new_delivery_management)

        val buttonUser: Button = findViewById(R.id.button_user_new_delivery)
        val textUser: TextView = findViewById(R.id.text_user_new_delivery_management)

        val dateEditText: EditText = findViewById(R.id.date_new_delivery)

        val tagText : EditText = findViewById(R.id.tag_new_delivery)
        val ccText : EditText = findViewById(R.id.cc_new_delivery)
        val ordText : EditText = findViewById(R.id.ord_new_delivery)

        //------------------ CHOOSE DATE ------------------

        val dateFormat = getDateInstance(DateFormat.SHORT, Locale("fr","FR"))
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        dateEditText.showSoftInputOnFocus = false

        dateEditText.text = SpannableStringBuilder(dateFormat.format(calendar.time))

        val datePickerDialog = DatePickerDialog(
            this,
            { _, y, m, d ->
                calendar.set(Calendar.YEAR, y)
                calendar.set(Calendar.MONTH, m)
                calendar.set(Calendar.DAY_OF_MONTH, d)

                dateEditText.text = SpannableStringBuilder(dateFormat.format(calendar.time)) //SpannableStringBuilder("$d /${m + 1}/$y")
            },
            year, month, day
        )

        dateEditText.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                datePickerDialog.show()
            }
        }

        dateEditText.setOnClickListener {
                datePickerDialog.show()
        }

        //------------------ CHOOSE CLIENT ------------------

        val getClient = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.getStringExtra("EXTRA_RESULT")
                textClient.text = data
            }
        }

        buttonClient.setOnClickListener{
            val intent = Intent(this, CityDeliveryManagement::class.java)
            getClient.launch(intent)
        }

        //------------------ CHOOSE USER ------------------

        val mail = Firebase.auth.currentUser?.email
        textUser.text = mail.toString().dropLast(11).uppercase()

        val getDriver = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.getStringExtra("EXTRA_RESULT")
                textClient.text = data
            }
        }

        buttonUser.setOnClickListener{
            Log.d("tag1", "1")
            val intent = Intent(this, DriverDeliveryManagement::class.java)
            getDriver.launch(intent)
        }

        //------------------ ROLLS ------------------

    }
}