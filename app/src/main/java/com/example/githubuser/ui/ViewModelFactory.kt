package com.example.githubuser.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.di.Injection
import com.example.githubuser.repository.FavoriteRepository
import com.example.githubuser.ui.favorites.FavoriteViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(private val favoriteRepository: FavoriteRepository) :
    ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
                return FavoriteViewModel(favoriteRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

        companion object {
            @Volatile
            private var instance: ViewModelFactory? = null
            fun getInstance(context: Context): ViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: ViewModelFactory(Injection.provideRepository(context))
                }.also { instance = it }
        }
    }
