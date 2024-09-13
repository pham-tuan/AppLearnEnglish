package com.tuan.englishforkid.db

import com.tuan.englishforkid.model.DetailResponse
import com.tuan.englishforkid.model.NewPassResponse
import com.tuan.englishforkid.model.PracticeResponse
import com.tuan.englishforkid.model.TopicResponse
import com.tuan.englishforkid.model.User
import com.tuan.englishforkid.model.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface APIServices {
    @GET("topic")
    suspend fun getListTopic(): Response<TopicResponse>

    @GET("detail/{type}")
    suspend fun getListDetail(@Path("type") type: String?): Response<DetailResponse>

    @PUT("detail/{iddata}")
    suspend fun updateStatus(@Path("iddata") iddata: String?): Response<DetailResponse>

    @GET("practice/{status}")
    suspend fun getListPractice(@Path("status") status: String?): Response<PracticeResponse>

    @GET("login")
    suspend fun getListUser(): Response<UserResponse>


    @Headers("Content-Type: application/json")
    @POST("login/")
    suspend fun postUser(
//        @Field("nameuser") nameuser: String?,
//        @Field("gmail") gmail: String?,
//        @Field("pass") pass: String?
        @Body userR: User,
    ): Response<UserResponse>

    @FormUrlEncoded
    @PUT("login/{id}")
    suspend fun updateUser(
        @Path("id") id: Int?,
        @Field("nameuser") nameuser: String?,
        @Field("gmail") gmail: String?,
        @Field("pass") pass: String?
    ): Response<UserResponse>

    @FormUrlEncoded
    @PUT("login/updatepass/{gmail}")
    suspend fun changePass(
        @Path("gmail") gmail: String,
        @Field("pass") pass: String
    ): Response<NewPassResponse>

}   