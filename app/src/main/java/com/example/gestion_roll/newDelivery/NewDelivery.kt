package com.example.gestion_roll.newDelivery

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.gestion_roll.R
import com.example.gestion_roll.client.Client
import com.example.gestion_roll.user.Users
import com.example.gestion_roll.user.getUserFromID
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

        val etaText : EditText = findViewById(R.id.eta_new_delivery)
        val rehText : EditText = findViewById(R.id.reh_new_delivery)

        val buttonValidate: Button = findViewById(R.id.button_validate_new_delivery)

        //------------------ CHOOSE DATE ------------------

        val dateFormat = getDateInstance(DateFormat.SHORT, Locale("fr","FR"))
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var date : Date = calendar.time

        dateEditText.showSoftInputOnFocus = false
        dateEditText.text = SpannableStringBuilder(dateFormat.format(date))

        val datePickerDialog = DatePickerDialog(
            this,
            { _, y, m, d ->
                calendar.set(Calendar.YEAR, y)
                calendar.set(Calendar.MONTH, m)
                calendar.set(Calendar.DAY_OF_MONTH, d)

                date = calendar.time

                dateEditText.text = SpannableStringBuilder(dateFormat.format(date)) //SpannableStringBuilder("$d /${m + 1}/$y")
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

        var clientData = Client()

        val getClient = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                clientData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getSerializableExtra("EXTRA_RESULT", Client::class.java)!!
                } else {
                    @Suppress("DEPRECATION", "UNREACHABLE_CODE")
                    result.data?.getSerializableExtra("EXTRA_RESULT") as Client
                }
                textClient.text = clientData.name
            }
        }

        buttonClient.setOnClickListener{
            val intent = Intent(this, CityDeliveryManagement::class.java)
            getClient.launch(intent)
        }

        //------------------ CHOOSE USER ------------------
        var driverData: Users = getUserFromID()
        textUser.text = driverData.Login.uppercase()

        val getDriver = registerForActivityResult(ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                driverData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getSerializableExtra("EXTRA_RESULT", Users::class.java)!!
                } else {
                    @Suppress("DEPRECATION", "UNREACHABLE_CODE")
                    result.data?.getSerializableExtra("EXTRA_RESULT") as Users
                }
                textUser.text = driverData.Login.uppercase()
            }
        }

        buttonUser.setOnClickListener{
            val intent = Intent(this, DriverDeliveryManagement::class.java)
            intent.action = "new_delivery"
            getDriver.launch(intent)
        }

        //------------------ ROLLS ------------------

        buttonValidate.setOnClickListener{

            fun returnIntCheckNull(editText: EditText) : Int {
                val value = editText.text.toString()
                return if (value.isNotEmpty()) {
                    value.toIntOrNull() ?: 0
                } else 0
            }

            Delivery(date, driverData, clientData, returnIntCheckNull(tagText), returnIntCheckNull(ccText),
                returnIntCheckNull(ordText), returnIntCheckNull(etaText), returnIntCheckNull(rehText)).putInFirebase(this)
        }
    }
}