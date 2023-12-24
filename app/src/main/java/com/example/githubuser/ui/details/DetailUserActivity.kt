package com.example.githubuser.ui.details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.database.Favorite
import com.example.githubuser.databinding.ActivityDetailUserBinding
import com.example.githubuser.ui.favorites.FavoriteActivity
import com.example.githubuser.ui.favorites.FavoriteViewModel
import com.example.githubuser.ui.follow.FragmentAdapter
import com.example.githubuser.ui.ViewModelFactory
import com.example.githubuser.ui.settings.SettingsActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailViewModel: DetailViewModel
    private val favoriteViewModel by viewModels<FavoriteViewModel>(){
        ViewModelFactory.getInstance(application)
    }
    private lateinit var favorite: Favorite
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail"

        val username = intent.getStringExtra("username") ?: ""

        val fragmentAdapter = FragmentAdapter(this)
        fragmentAdapter.username = username

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = fragmentAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLE[position])
        }.attach()

        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        if (username.isNotEmpty()) {
            detailViewModel.findDetailUserGithub(username)
        }

        detailViewModel.detailUserGithub.observe(this) { detailUserResponse ->
            val followerCount = detailUserResponse?.followers
            val login = detailUserResponse?.login
            val followingCount = detailUserResponse?.following
            val avatarUrl = detailUserResponse?.avatarUrl
            val nameUser = detailUserResponse?.name

            Glide.with(binding.root.context)
                .load(avatarUrl)
                .into(binding.imgView)
            binding.unameView.text = login
            binding.jmlFollower.text = "$followerCount Followers"
            binding.jmlFollowing.text = "$followingCount Following"
            binding.nameView.text = nameUser

            favorite = Favorite(
                username = login ?: "",
                avatarUrl = avatarUrl ?: "",
                isFavorited = isFavorite
            )
            Log.d("Data fav dalam" , "$favorite")

        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        favoriteViewModel.selectUserFavorite(username).observe(this) { favData ->
            isFavorite = favData != null
            updateFavoriteButtonIcon(isFavorite)
        }


        binding.favIc.setOnClickListener {
            if (!isFavorite) {
                favoriteViewModel.insertFavorite(favorite)
            } else {
                favoriteViewModel.deleteFavorite(favorite)
            }
            isFavorite = !isFavorite
            updateFavoriteButtonIcon(isFavorite)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.settings_menu -> {
                openSettingsActivity()
                return true
            }
            R.id.favorite_menu -> {
                openFavoriteActivity()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openSettingsActivity() {
        val moveProfile = Intent(this@DetailUserActivity, SettingsActivity::class.java)
        startActivity(moveProfile)
    }

    private fun openFavoriteActivity() {
        val moveProfile = Intent(this@DetailUserActivity, FavoriteActivity::class.java)
        startActivity(moveProfile)
    }

    private fun updateFavoriteButtonIcon(isFavorite: Boolean) {
        val favButton = findViewById<FloatingActionButton>(R.id.fav_ic)
        val drawableResId =
            if (isFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24
        favButton.setImageDrawable(ContextCompat.getDrawable(favButton.context, drawableResId))
    }

    companion object {
        @StringRes
        private val TAB_TITLE = intArrayOf(
            R.string.following_tab,
            R.string.follower_tab
        )
    }
}
