package com.example.githubuser.ui.favorites

import androidx.lifecycle.ViewModel
import com.example.githubuser.database.Favorite
import com.example.githubuser.repository.FavoriteRepository

class FavoriteViewModel(private val favoriteRepository: FavoriteRepository) : ViewModel() {

    fun getAllFavorite() = favoriteRepository.getAllFavorite()

    fun insertFavorite(favorite: Favorite) {
        favoriteRepository.insertFavorite(favorite, true)
    }

    fun deleteFavorite(favorite: Favorite) {
        favoriteRepository.deleteFavorite(favorite, false)
    }

    fun selectUserFavorite(favorite: String) = favoriteRepository.selectUser(favorite)

}