package com.example.kotlinapplication.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinapplication.model.WeatherData
import com.example.kotlinapplication.model.WeatherService
import kotlinx.coroutines.*

class HomeViewModel : ViewModel() {

    val WeatherLiveData = MutableLiveData<WeatherData>()
    val loadingLiveData = MutableLiveData<Boolean>()


    fun fetchData(lat: Double, lon: Double, lang :String, unit:String ) {
        val exceptionHandlerException = CoroutineExceptionHandler { _, _ ->
            Log.i("TAG", "exce: ")
            loadingLiveData.postValue(false)
        }
        CoroutineScope(Dispatchers.IO + exceptionHandlerException).launch {
            val response = WeatherService.getWeatherService().getWeatherForecast(lat,lon,"642e502b69f6bb5e8ac38e7329603eef",unit,lang,"minutely")
            withContext(Dispatchers.Main) {
                loadingLiveData.postValue(false)
                if (response.isSuccessful) {
                    WeatherLiveData.postValue(response.body())
                    Log.i("TAG", "sucesss: ")

                }else{
                    Log.i("TAG", "fail: ")
                }
            }
        }

    }


}