package com.tuan.englishforkid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class DetailModel(var response: DetailResponse)
data class DetailResponse(@SerializedName("detail") var listDetail: ArrayList<Detail>? = null)
@Parcelize

data class Detail(

    @SerializedName("iddata") val iddata: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("imgdetail") val imgdetail: String? = null,
    @SerializedName("vocabulary") val vocabulary: String? = null,
    @SerializedName("spelling") val spelling: String? = null,
    @SerializedName("means") val means: String? = null,
    @SerializedName("sound") val sound: String? = null,
    @SerializedName("status") val status: String? = null,

    ) : Parcelable