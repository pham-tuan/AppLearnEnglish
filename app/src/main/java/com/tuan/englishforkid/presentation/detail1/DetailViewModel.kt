package com.tuan.englishforkid.presentation.detail1

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.tuan.englishforkid.repository.Repository
import com.tuan.englishforkid.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: Repository) : ViewModel(){
    fun getListDetail(type:String) = liveData(Dispatchers.IO) {
        emit(DataResult.loading())
        try {
            emit(DataResult.success(repository.getListDetail(type)))
        } catch (e: Exception) {
            Log.e("detail", e.toString())
            emit(DataResult.error(e.message ?: "Error Data"))
        }
    }

    fun putIdData(idData: String) = liveData(Dispatchers.IO) {
        emit(DataResult.loading())
        try {
            emit(DataResult.success(repository.putIdData(idData)))
        } catch (e: Exception) {
            Log.e("putdata_detail", e.toString())
            emit(DataResult.error(e.message ?: "Error Data"))
        }
    }


}