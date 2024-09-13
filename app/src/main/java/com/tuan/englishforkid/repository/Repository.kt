package com.tuan.englishforkid.repository


import com.tuan.englishforkid.db.DataRemoteAPI
import com.tuan.englishforkid.model.User
import javax.inject.Inject

class Repository @Inject constructor(private val dataRemoteAPI: DataRemoteAPI) {
    suspend fun getListTopic() = dataRemoteAPI.getListTopic()
    suspend fun getListDetail(type: String) = dataRemoteAPI.getListDetail(type)
    suspend fun putIdData(iddata: String) = dataRemoteAPI.putIdData(iddata)
    suspend fun getListPractice(status: String) = dataRemoteAPI.getListPractice(status)
    suspend fun getListUser() = dataRemoteAPI.getListUser()
    suspend fun postRegister(user: User) = dataRemoteAPI.postRegister(user)
    suspend fun putUser(id: Int, nameuser: String , gmail: String, pass: String) = dataRemoteAPI.updateUser(id, nameuser, gmail, pass)
    suspend fun changePass(gmail: String, pass: String) = dataRemoteAPI.changePass(gmail, pass)

}