package com.tuan.englishforkid.presentation.favorite

import androidx.lifecycle.LiveData
import com.tuan.englishforkid.roomdata.DAO
import com.tuan.englishforkid.roomdata.Favor
import javax.inject.Inject

class FavorRepository @Inject constructor(private val favorDao: DAO) {

    val allFavorites: LiveData<List<Favor>> = favorDao.getAll()

    fun getRandomFavorites(): LiveData<List<Favor>> = favorDao.getRandomFavorites()

}