package com.example.githubuser.repository

import androidx.lifecycle.LiveData
import com.example.githubuser.data.retrofit.ApiService
import com.example.githubuser.database.Favorite
import com.example.githubuser.database.FavoriteDao
import com.example.githubuser.utils.AppExecutors

class FavoriteRepository private constructor(
    private val favoriteDao: FavoriteDao,
    private val appExecutors: AppExecutors
){

    fun getAllFavorite() : LiveData<List<Favorite>> {
        return favoriteDao.getAllFavorites()
    }

    fun selectUser(username: String) : LiveData<Favorite> {
        return favoriteDao.selectUser(username)
    }

    fun insertFavorite(favorite: Favorite, favoriteState: Boolean) {
        appExecutors.diskIO.execute {
            favorite.isFavorited = favoriteState
            favoriteDao.insert(favorite)
        }
    }

    fun deleteFavorite(favorite: Favorite, favoriteState: Boolean) {
        appExecutors.diskIO.execute {
            favorite.isFavorited = favoriteState
            favoriteDao.delete(favorite)
        }
    }

    companion object {
       @Volatile
       private var instance: FavoriteRepository? = null
        fun getInstance(
            favoriteDao: FavoriteDao,
            appExecutors: AppExecutors
        ) : FavoriteRepository = instance ?: synchronized(this) {
            instance ?: FavoriteRepository( favoriteDao, appExecutors)
        }.also { instance = it }
    }
}