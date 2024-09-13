package com.tuan.englishforkid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserResponse(@SerializedName("login") var listUser:ArrayList<User>?=null)
data class NewPassResponse(@SerializedName("login") var listUser:ArrayList<User>?=null)
@Parcelize
data class User(
    @SerializedName("id") val iddata: Int? = null,
    @SerializedName("nameuser") val nameuser: String? = null,
    @SerializedName("gmail") val gmail: String? = null,
    @SerializedName("pass") val pass: String? = null,

): Parcelable