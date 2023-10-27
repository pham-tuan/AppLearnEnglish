package com.tuan.englishforkid.db

import com.tuan.englishforkid.model.User
import javax.inject.Inject

class DataRemoteAPI @Inject constructor(private val apiServices: APIServices) {
    suspend fun getListTopic() = apiServices.getListTopic()
    suspend fun getListDetail(type: String) = apiServices.getListDetail(type)
    suspend fun putIdData(iddata: String) = apiServices.updateStatus(iddata)
    suspend fun getListPractice(status: String) = apiServices.getListPractice(status)
    suspend fun getListUser() = apiServices.getListUser()
    suspend fun postRegister(nameuser: String,gmail: String,pass: String) = apiServices.postUser(nameuser,gmail,pass)

}