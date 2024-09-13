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
class UserViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    fun postUser(user: User) = liveData(Dispatchers.IO) {
        emit(DataResult.loading())
        try {
            val response = repository.postRegister(user) //TODO: Error do khong parse data sang object cua Gson => Solution: Config lai gson
            if (response.isSuccessful) {
                emit(DataResult.success(response.body()))
            } else {
                emit(DataResult.error("ErrorPstUser: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("postuser", "ErrorPstUser: ${e.message}", e)
            emit(DataResult.error("ErrorPostUser: ${e.message}"))
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

        fun putUserProfile(id: Int, nameuser: String, gmail: String, pass: String) = liveData(Dispatchers.IO) {
        emit(DataResult.loading())
        try {
            emit(DataResult.success(repository.putUser(id, nameuser, gmail, pass)))
        } catch (e: Exception) {
            Log.e("putInfor_user", e.toString())
            emit(DataResult.error(e.message ?: "Error Data"))
        }
    }

    fun putNewPass(gmail: String, pass: String) = liveData(Dispatchers.IO) {
        emit(DataResult.loading())
        try {
            val response = repository.changePass(gmail, pass)
            if (response.isSuccessful) {
                Log.d("ChangePass", "Success: ${response.body()}")
                emit(DataResult.success(response))
            } else {
                Log.e("ChangePass", "Error: ${response.errorBody()?.string()}")
                emit(DataResult.error("Server error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("ChangePass", "Exception: ${e.message}", e)
            emit(DataResult.error(e.message ?: "Error Data"))
        }
    }

}