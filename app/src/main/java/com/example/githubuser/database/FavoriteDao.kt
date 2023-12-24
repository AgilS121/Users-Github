package com.example.githubuser.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT * FROM favorite WHERE username = :username")
    fun selectUser(username: String): LiveData<Favorite>

    @Query("SELECT * from favorite")
    fun getAllFavorites() : LiveData<List<Favorite>>

}