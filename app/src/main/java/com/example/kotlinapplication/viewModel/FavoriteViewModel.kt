package com.example.kotlinapplication.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kotlinapplication.model.roomDataSource.DataBase
import com.example.kotlinapplication.model.roomDataSource.FavoriteDao
import com.example.kotlinapplication.model.roomDataSource.FavoriteEntity
import kotlinx.coroutines.*

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    private val db: DataBase = DataBase.getDatabase(application)!!
    private val favoriteDao: FavoriteDao = db.favoriteDao()

    private val favoriteData = MutableLiveData<List<FavoriteEntity>>()
    private val error = MutableLiveData<String>()

    fun insert_Update(favorite: FavoriteEntity){
        val coroutineExceptionHandler = CoroutineExceptionHandler{  _, th ->
            error.postValue(th.localizedMessage)
        }
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            favoriteDao.insert(favorite)
        }
    }

    fun getAll() {
        val coroutineExceptionHandler = CoroutineExceptionHandler{  _, th ->
            error.postValue(th.localizedMessage)
        }
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            val data = favoriteDao.getAll()
            withContext(Dispatchers.Main) {
                favoriteData.postValue(data)
            }
        }
    }

    fun delete(favorite: FavoriteEntity){
        val coroutineExceptionHandler = CoroutineExceptionHandler{  _, th ->
            error.postValue(th.localizedMessage)
        }
        CoroutineScope(Dispatchers.IO + coroutineExceptionHandler).launch {
            favoriteDao.delete(favorite)
        }
    }

    fun getFavoriteInfo(): LiveData<List<FavoriteEntity>> {
        return favoriteData
    }

    fun getError(): LiveData<String> {
        return error
    }
}