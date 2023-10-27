package com.tuan.englishforkid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class TopicModel(var response: TopicResponse)
data class TopicResponse(@SerializedName("topic") var listTopic: ArrayList<Topic>? = null)
@Parcelize

data class Topic(

    @SerializedName("idtopic") val idTopic: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("imgtopic") val imgtopic: String? = null,
    @SerializedName("title") val title: String? = null,

    ): Parcelable
