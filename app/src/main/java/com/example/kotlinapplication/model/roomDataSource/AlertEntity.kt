package com.example.kotlinapplication.model.roomDataSource

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "Alert")
class AlertEntity :  Serializable{
    @PrimaryKey(autoGenerate = true)
    var id :Int = 0
    lateinit  var alertTime: String
    lateinit var alertDay: String
    lateinit var alertDayAr: String
    lateinit var alertEvent: String
    lateinit var alertType: String

    var enabled: Boolean = false
    lateinit var event: String
    var start: Long = 0L
    var end: Long =0L
    lateinit var description: String
}

