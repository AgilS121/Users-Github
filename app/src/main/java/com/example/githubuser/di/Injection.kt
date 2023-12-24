package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.data.retrofit.ApiConfig
import com.example.githubuser.database.FavoriteRoomDatabase
import com.example.githubuser.repository.FavoriteRepository
import com.example.githubuser.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): FavoriteRepository {
        val database = FavoriteRoomDatabase.getDatabase(context)
        val dao = database.favoriteDao()
        val appExecutors = AppExecutors()
        return FavoriteRepository.getInstance( dao, appExecutors)
    }
}