package com.example.kotlinapplication.view.Alert

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.example.kotlinapplication.HomeActivity
import com.example.kotlinapplication.R
import com.example.kotlinapplication.model.Alert
import com.example.kotlinapplication.model.WeatherService
import com.example.kotlinapplication.model.roomDataSource.AlertDao
import com.example.kotlinapplication.model.roomDataSource.AlertEntity
import com.example.kotlinapplication.model.roomDataSource.DataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AlertBroadcast: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        when (p1?.action) {
            Intent.ACTION_TIME_TICK -> getDataBaseAlerts(p0!!)
        }
    }

    private fun getAPIAlerts(context: Context, databaseData: List<AlertEntity>) {
//
//        val pref = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
//        val lat = pref.getFloat("lat", 0.0F)
//        val lon = pref.getFloat("lon", 0.0F)
//        val unit = pref.getString("unit", "metric")!!
//        val language = pref.getString("language", "en")!!
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val response = WeatherService.getWeatherService().getWeatherForecast(lat.toDouble(),lon.toDouble(),"b69f6bb5e8ac38e7329603eef",unit,language,"minutely,hourly,daily,current")
//            withContext(Dispatchers.Main){
//                if(response.isSuccessful){
//                    if(!response.body()!!.alert.isNullOrEmpty()) {
//                        val data = response.body()!!.alert!!
//                        checkDays(context, databaseData, data)
//                    }
//                }
//            }
//        }

        //test weather alerts


        val newAlert = Alert("Hi","Temp",0,0,"heat")
        newAlert. event = "Gale-force gusts"
        newAlert.start = 0
        newAlert. end = 0
        newAlert.description = "There is a risk of gale-force gusts (Level 2 of 4).Max. gusts: 55-70 km/h; Wind direction: west; Increased gusts: near showers ~ 85 km/h"
        val data = listOf(newAlert)
        checkDays(context, databaseData, data)


    }



    private fun getDataBaseAlerts(context: Context) {


        val db: DataBase = DataBase.getDatabase(context.applicationContext)!!
        val alertDao: AlertDao = db.alertDao()

        val calendar = Calendar.getInstance()
        val currentTime = "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)}"
        println(currentTime)
        CoroutineScope(Dispatchers.IO).launch {
            val data = alertDao.getSome(currentTime, true)
            withContext(Dispatchers.Main) {
                if(!data.isNullOrEmpty()){
                    getAPIAlerts(context, data)
                }
            }
        }
    }

    private fun checkDays(context: Context, databaseAlertInfo: List<AlertEntity>, apiData: List<Alert>) {
        val calendar = Calendar.getInstance()
        val DayNum = calendar.get(Calendar.DAY_OF_WEEK)
        var currentDay = ""
        when (DayNum) {
            1 -> currentDay = "Sunday"
            2 -> currentDay = "Monday"
            3 -> currentDay = "Tuesday"
            4 -> currentDay = "Wednesday"
            5 -> currentDay = "Thursday"
            6 -> currentDay = "Friday"
            7 -> currentDay = "Saturday"
        }

        for (api in apiData){
            for (db in databaseAlertInfo){
                if(api.event == db.alertEvent) {
                    if (db.alertDay.contains(currentDay) || db.alertDay == "ALL Days" || (db.alertDay == "WEEKEND" && (currentDay == "Friday" || currentDay == "Saturday"))) {
                        if (db.alertType == "Default Sound"){
                            displayAlert(context, api.event, api.description, "Ok", "Cancel")
                        }else{
                            displayNotification( api.event.toString(), api.description.toString(),context)
                        }
                    } else if (db.alertDay == "NONE") {
                        if (db.alertType == "Default Sound"){
                            displayAlert(context, api.event, api.description, "Ok", "Cancel")
                        }else{
                            displayNotification( api.event.toString(), api.description.toString(),context)
                           }

                        db.enabled = false
                        disableAlert(context, db)
                    }
                }
            }
        }
    }

    private fun disableAlert(context: Context, alert: AlertEntity) {
        val db: DataBase = DataBase.getDatabase(context.applicationContext)!!
        val alertDao: AlertDao = db.alertDao()

        CoroutineScope(Dispatchers.IO).launch {
            alertDao.insert(alert)
        }
    }

    private fun displayAlert(context: Context, title: String?, body: String?, yes: String?, no: String?) {
        val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val ringtone = RingtoneManager.getRingtone(context, notification)
        ringtone.play()

        val manager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.gravity = Gravity.CENTER
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.alpha = 1.0f
        layoutParams.packageName = context.packageName
        layoutParams.buttonBrightness = 1f
        val view = View.inflate(context.applicationContext, R.layout.alert_view, null)
        val titleLbl = view.findViewById<View>(R.id.alertTitle) as TextView
        val bodyLbl = view.findViewById<View>(R.id.alertBody) as TextView
        val yesButton = view.findViewById<View>(R.id.yesBtn) as Button
        val noButton = view.findViewById<View>(R.id.noBtn) as Button
        titleLbl.text = title
        bodyLbl.text = body
        yesButton.text = yes
        noButton.text = no
        yesButton.setOnClickListener {
            manager.removeView(view)
            ringtone.stop()
        }
        noButton.setOnClickListener {
            manager.removeView(view)
            ringtone.stop()
        }
        manager.addView(view, layoutParams)
    }

 private fun displayNotification(notificationTitle: String, notificationBody: String, context: Context) {
        val intent = Intent(context, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
        val lowIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_ONE_SHOT)
        val notificationBuilder = NotificationCompat.Builder(context, "1")
        val mNotifyManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = context.getString(R.string.channel_name)
            val description: String = context.getString(R.string.channel_description) //user visible
            val importance = NotificationManager.IMPORTANCE_LOW
            val att = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            val mChannel = NotificationChannel("1", name, importance)
            mChannel.description = description
            mChannel.enableLights(false)
            mChannel.enableVibration(false)
            mChannel.vibrationPattern = longArrayOf(0L)
            mChannel.setSound(null, att)
            mNotifyManager.createNotificationChannel(mChannel)
            notificationBuilder
                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .setVibrate(longArrayOf(0L))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setColor(ContextCompat.getColor(context, R.color.teal_700))
                    .setContentTitle(notificationTitle)
                    .setAutoCancel(true)
                    .setContentIntent(lowIntent)
        } else {
            notificationBuilder.setContentTitle(notificationTitle)
                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                    .setCategory(NotificationCompat.CATEGORY_EVENT)
                    .setVibrate(longArrayOf(0L))
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setColor(ContextCompat.getColor(context, R.color.teal_700))
                    .setAutoCancel(true)
                    .setContentIntent(lowIntent)
        }
        notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(notificationBody))
        notificationBuilder.setContentText(notificationBody)
        mNotifyManager.notify(2, notificationBuilder.build())
    }

}