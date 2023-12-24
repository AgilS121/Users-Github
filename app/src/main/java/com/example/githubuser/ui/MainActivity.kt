package com.example.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.details.DetailUserActivity
import com.example.githubuser.ui.favorites.FavoriteActivity
import com.example.githubuser.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        val layoutManager = LinearLayoutManager(this)
        binding.recycleview.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recycleview.addItemDecoration(itemDecoration)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.text = searchView.text
                    searchView.hide()
                    mainViewModel.findGithubUsers(searchBar.text.toString())
                    false
                }
        }

        mainViewModel.listDataGithub.observe(this) { lisDataGithub->
            setReviewData(lisDataGithub)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun setReviewData(consumerReviews: List<ItemsItem>) {
        adapter = ReviewAdapter(object : ReviewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                openDetail(data)
            }
        })
        adapter.submitList(consumerReviews)
        binding.recycleview.adapter = adapter
    }

    private fun openDetail(data: ItemsItem) {
        val moveIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
        moveIntent.putExtra("username", data.login)
        startActivity(moveIntent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_menu -> {
                openSettingsActivity()
                true
            }
            R.id.favorite_menu -> {
                openFavoriteActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openSettingsActivity() {
        val moveProfile = Intent(this@MainActivity, SettingsActivity::class.java)
        startActivity(moveProfile)
    }

    private fun openFavoriteActivity() {
        val moveProfile = Intent(this@MainActivity, FavoriteActivity::class.java)
        startActivity(moveProfile)
    }
}
