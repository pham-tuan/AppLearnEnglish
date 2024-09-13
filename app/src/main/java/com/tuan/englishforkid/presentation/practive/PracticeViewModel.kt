package com.tuan.englishforkid.presentation.practive

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tuan.englishforkid.repository.Repository
import com.tuan.englishforkid.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class PracticeViewModel @Inject constructor (private val repository: Repository): ViewModel()  {
    fun getlistPractice(status:String) = liveData(Dispatchers.IO) {
        emit(DataResult.loading())
        try {
            emit(DataResult.success(repository.getListPractice(status)))
        } catch (e: Exception) {
            Log.e("practice", e.toString())
            emit(DataResult.error(e.message ?: "Error Data"))
        }
    }

}