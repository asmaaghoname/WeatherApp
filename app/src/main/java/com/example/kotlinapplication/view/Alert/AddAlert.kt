package com.example.kotlinapplication.view.Alert

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinapplication.R
import com.example.kotlinapplication.model.roomDataSource.AlertEntity
import com.example.kotlinapplication.viewModel.AlertViewModel
import java.util.*

class AddAlert : AppCompatActivity() {

    private lateinit var eventSpinner: Spinner
    private lateinit var typeSpinner: Spinner
    private lateinit var time: TextView
    private lateinit var satCheckBox: CheckBox
    private lateinit var sunCheckBox: CheckBox
    private lateinit var monCheckBox: CheckBox
    private lateinit var tueCheckBox: CheckBox
    private lateinit var wedCheckBox: CheckBox
    private lateinit var thuCheckBox: CheckBox
    private lateinit var friCheckBox: CheckBox
    private lateinit var addbtn: Button

    private lateinit var viewModel: AlertViewModel
    private lateinit var alert: AlertEntity
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alert)

        eventSpinner = findViewById(R.id.alertEventSpinner)
        typeSpinner = findViewById(R.id.alertTypeSpinner)
        time = findViewById(R.id.timeLbl)
        satCheckBox = findViewById(R.id.saturday)
        sunCheckBox = findViewById(R.id.sunday)
        monCheckBox = findViewById(R.id.monday)
        tueCheckBox = findViewById(R.id.tuesday)
        wedCheckBox = findViewById(R.id.wednesday)
        thuCheckBox = findViewById(R.id.thursday)
        friCheckBox = findViewById(R.id.friday)
        addbtn = findViewById(R.id.addBtn)

        addbtn.setOnClickListener {
            addAlert()
        }

        time.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)

            val pickerTime = TimePickerDialog(this, { _, sHour, sMinute ->
                time.text = "$sHour:$sMinute"
            }, hour, minutes, true)
            pickerTime.show()
        }

//        if (intent.getStringExtra("status") == "update"){
//            alert = intent.getSerializableExtra("alert") as AlertEntity
//            //setUpdateData()
//        }

        viewModel = ViewModelProvider(this).get(AlertViewModel::class.java)
    }

    private fun addAlert() {
        val days = StringBuilder()
        val daysAr = StringBuilder()

        if (satCheckBox.isChecked && sunCheckBox.isChecked && monCheckBox.isChecked && tueCheckBox.isChecked &&
            wedCheckBox.isChecked && thuCheckBox.isChecked && friCheckBox.isChecked) {
            days.append("ALL Days")
            daysAr.append("كل الأيام")
        }else if (satCheckBox.isChecked && friCheckBox.isChecked) {
            days.append("WEEKEND")
            daysAr.append("الأجازات")
        } else {
            if (satCheckBox.isChecked) {
                days.append("Saturday.")
                daysAr.append("السبت.")
            }
            if (sunCheckBox.isChecked) {
                days.append("Sunday.")
                daysAr.append("الأحد.")
            }
            if (monCheckBox.isChecked) {
                days.append("Monday.")
                daysAr.append("الأثنين.")
            }
            if (tueCheckBox.isChecked) {
                days.append("Tuesday.")
                daysAr.append("الثلاثاء-")
            }
            if (wedCheckBox.isChecked) {
                days.append("Wednesday.")
                daysAr.append("الإربعاء-")
            }
            if (thuCheckBox.isChecked) {
                days.append("Thursday.")
                daysAr.append("الخميس -")
            }
            if (friCheckBox.isChecked) {
                days.append("Friday.")
                daysAr.append("الجمعه-")
            }
            if(satCheckBox.isChecked || sunCheckBox.isChecked || monCheckBox.isChecked ||tueCheckBox.isChecked ||
                wedCheckBox.isChecked || thuCheckBox.isChecked || friCheckBox.isChecked)
            {
                days.deleteCharAt(days.lastIndex)
            }
            else
            {
                days.append("No Days")
                daysAr.append("لا يوجد أيام")
            }

        }


        // save in Room
        val newAlert = AlertEntity()
        newAlert.alertTime = time.text.toString()
        newAlert. alertDay = days.toString()
        newAlert. alertDayAr = daysAr.toString()
        newAlert.alertEvent = eventSpinner.selectedItem.toString()
        newAlert. alertType = typeSpinner.selectedItem.toString()
        newAlert.  enabled = true
        newAlert. event = ""
        newAlert.start = 0L
        newAlert. end = 0L
        newAlert.description = ""

        viewModel.insert(newAlert)
        finish()
    }
}