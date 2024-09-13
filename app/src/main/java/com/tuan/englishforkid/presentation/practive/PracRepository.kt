package com.tuan.englishforkid.presentation.practive

import androidx.lifecycle.LiveData
import com.tuan.englishforkid.roomdata.DAO
import com.tuan.englishforkid.roomdata.Prac
import javax.inject.Inject

class PracRepository @Inject constructor(private val pracDao: DAO) {
    val allPractice: LiveData<List<Prac>> = pracDao.getAllPrac()

     fun getRandomPrac(): LiveData<List<Prac>> = pracDao.getRandomPractice()
     fun getRandomListen(): LiveData<List<Prac>> = pracDao.getRandomPractice()

}