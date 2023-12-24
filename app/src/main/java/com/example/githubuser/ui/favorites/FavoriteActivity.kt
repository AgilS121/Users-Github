package com.example.githubuser.ui.favorites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.ui.ReviewAdapter
import com.example.githubuser.ui.ViewModelFactory
import com.example.githubuser.ui.details.DetailUserActivity

class FavoriteActivity : AppCompatActivity() {

    private val favoriteViewModel by viewModels<FavoriteViewModel>(){
        ViewModelFactory.getInstance(application)
    }
    private lateinit var binding : ActivityFavoriteBinding
    private lateinit var adapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Favorite"

        val layoutManager = LinearLayoutManager(this)
        binding.recycleviewFavorite.layoutManager = layoutManager

        adapter = ReviewAdapter(object : ReviewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                openDetail(data)
            }
        })
        binding.recycleviewFavorite.adapter = adapter

        favoriteViewModel.getAllFavorite().observe(this) { users ->
            val items = arrayListOf<ItemsItem>()
            users.map {
                val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl)
                items.add(item)
            }
            adapter.submitList(items)
            showLoading(false)
        }

        showLoading(true)

    }

    private fun openDetail(data: ItemsItem) {
        val moveIntent = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
        moveIntent.putExtra("username", data.login)
        startActivity(moveIntent)
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
            else -> super.onOptionsItemSelected(item)
        }
    }
}