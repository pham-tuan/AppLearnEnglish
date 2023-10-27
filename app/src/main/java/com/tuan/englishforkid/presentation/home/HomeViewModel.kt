package com.tuan.englishforkid.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tuan.englishforkid.utils.DataResult
import com.tuan.englishforkid.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getListTopic() = liveData(Dispatchers.IO) {
        emit(DataResult.loading())
        try {
            emit(DataResult.success(repository.getListTopic()))
        } catch (e: Exception) {
            Log.e("topic", e.toString())
            emit(DataResult.error(e.message ?: "Error Data"))
        }
    }
}