package com.tuan.englishforkid.presentation.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tuan.englishforkid.roomdata.Favor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val repository: FavorRepository) : ViewModel() {

    val allFavorites: LiveData<List<Favor>> = repository.allFavorites

    fun getRandomFavorites() = repository.getRandomFavorites()

}