package com.example.submission2_cobacoba.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: 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")
    fun getSearchUser(
        @Query("user") query: String
    ): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: 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")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: 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")
    fun getFollowersUser(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: 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")
    fun getFollowingsUser(
        @Path("username") username: String
    ): Call<ArrayList<User>>
}