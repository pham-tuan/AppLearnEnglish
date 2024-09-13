package com.tuan.englishforkid.db

import com.tuan.englishforkid.model.User
import javax.inject.Inject

class DataRemoteAPI @Inject constructor(private val apiServices: APIServices) {
    suspend fun getListTopic() = apiServices.getListTopic()
    suspend fun getListDetail(type: String) = apiServices.getListDetail(type)
    suspend fun putIdData(iddata: String) = apiServices.updateStatus(iddata)
    suspend fun getListPractice(status: String) = apiServices.getListPractice(status)
    suspend fun getListUser() = apiServices.getListUser()
    suspend fun postRegister(user: User) = apiServices.postUser(user)
    suspend fun updateUser(id: Int, nameuser: String , gmail: String , pass: String) = apiServices.updateUser(id , nameuser, gmail, pass)
    suspend fun changePass(gmail: String , pass: String) = apiServices.changePass( gmail, pass)

}