package com.tuan.englishforkid.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PracticeModel(var response: PracticeResponse)
data class PracticeResponse(@SerializedName("practice") var listPractice: ArrayList<Practice>? = null)
@Parcelize
data class Practice(
    @SerializedName("iddata") val iddata: String? = null,
    @SerializedName("imgdetail") val imgpractice: String? = null,
    @SerializedName("vocabulary") val vocabulary: String? = null,
    @SerializedName("spelling") val spelling: String? = null,
    @SerializedName("means") val means: String? = null,
    @SerializedName("sound") val sound: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("result") val result: String? = null,
    @SerializedName("a") val a: String? = null,
    @SerializedName("b") val b: String? = null,
    @SerializedName("c") val c: String? = null,
    @SerializedName("d") val d: String? = null,
): Parcelable