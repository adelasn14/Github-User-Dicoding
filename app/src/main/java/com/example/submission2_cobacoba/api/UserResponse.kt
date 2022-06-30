package com.example.submission2_cobacoba.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserResponse(
    @field:SerializedName("items")
    val users: ArrayList<User>
)

data class User(
    val login: String,
    val id: Int,
    val avatar_url: String
)

data class DetailUserResponse(
    val login: String,
    val avatar_url: String,
    val followers_url: String,
    val following_url: String,
    val name: String,
    val location: String,
    val company: String,
    val followers: Int,
    val following: Int
)

@Entity(tableName = "fav_user")
data class FavoriteUser(
    val login: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val avatar_url: String
) : Serializable
