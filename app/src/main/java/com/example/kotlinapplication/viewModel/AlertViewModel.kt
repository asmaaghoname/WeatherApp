package com.example.kotlinapplication.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinapplication.model.WeatherData
import com.example.kotlinapplication.model.WeatherService
import com.example.kotlinapplication.model.roomDataSource.AlertDao
import com.example.kotlinapplication.model.roomDataSource.AlertEntity
import com.example.kotlinapplication.model.roomDataSource.DataBase
import kotlinx.coroutines.*

class AlertViewModel (application: Application) : AndroidViewModel(application) {

    private val db: DataBase = DataBase.getDatabase(application)!!
    private val alertDao: AlertDao = db.alertDao()

    private val alertInfo = MutableLiveData<List<AlertEntity>>()
    private val alertSomeInfo = MutableLiveData<List<AlertEntity>>()
    private val error = MutableLiveData<String>()

    fun insert(alert: AlertEntity){
        val coroutineExceptionHandler = CoroutineExceptionHandler{  _, th ->
            error.postValue(th.localizedMessage)
        }
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            alertDao.insert(alert)
        }
    }

    fun getAll() {
        CoroutineScope(Dispatchers.IO).launch {
            val data = alertDao.getAll()
            withContext(Dispatchers.Main) {
                alertInfo.postValue(data)
            }
        }
    }

    fun getSome(alertTime: String, enabled: Boolean){

        CoroutineScope(Dispatchers.IO).launch {
            val data = alertDao.getSome(alertTime, enabled)
            withContext(Dispatchers.Main) {
                alertSomeInfo.postValue(data)
            }
        }
    }

    fun delete(alert: AlertEntity){
        val coroutineExceptionHandler = CoroutineExceptionHandler{  _, th ->
            error.postValue(th.localizedMessage)
        }
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            alertDao.delete(alert)
        }
    }

    fun getAlertInfo(): LiveData<List<AlertEntity>> {
        return alertInfo
    }

    fun getAlertSomeInfo(): LiveData<List<AlertEntity>> {
        return alertSomeInfo
    }

    fun getError(): LiveData<String> {
        return error
    }
}