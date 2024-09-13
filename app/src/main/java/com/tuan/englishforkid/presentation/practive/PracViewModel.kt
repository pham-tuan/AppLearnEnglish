package com.tuan.englishforkid.presentation.practive

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tuan.englishforkid.roomdata.Prac
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PracViewModel @Inject constructor(private val repository: PracRepository) : ViewModel() {

    val allPrac: LiveData<List<Prac>> = repository.allPractice

    fun getRandomPrac() = repository.getRandomPrac()
    fun getRandomListen() = repository.getRandomListen()

}