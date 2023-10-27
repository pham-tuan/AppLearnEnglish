package com.tuan.englishforkid.presentation.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tuan.englishforkid.model.User
import com.tuan.englishforkid.repository.Repository
import com.tuan.englishforkid.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
@HiltViewModel
class UserViewModel@Inject constructor(private val repository: Repository) : ViewModel()  {
    fun postUser(nameuser: String,gmail: String,pass: String) = liveData(Dispatchers.IO) {
        emit(DataResult.loading())
        try {
            emit(DataResult.success(repository.postRegister(nameuser,gmail,pass)))
        } catch (e: Exception) {
            Log.e("postuser", e.toString())
            emit(DataResult.error(e.message ?: "Error Data"))
        }
    }

    fun getUser() = liveData(Dispatchers.IO) {
        emit(DataResult.loading())
        try {
            emit(DataResult.success(repository.getListUser()))
        } catch (e: Exception) {
            Log.e("getuser", e.toString())
            emit(DataResult.error(e.message ?: "Error Data"))
        }
    }
}