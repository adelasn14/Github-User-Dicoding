package com.example.submission2_cobacoba.api

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFav(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM fav_user")
    fun getFavUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM fav_user WHERE fav_user.id = :id")
    suspend fun checkUser(id: Int): Int

    @Query("DELETE FROM fav_user WHERE fav_user.id = :id")
    suspend fun removeFromFav(id: Int): Int
}