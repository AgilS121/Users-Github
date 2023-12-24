package com.example.githubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.GithubResponse
import com.example.githubuser.data.response.ItemsItem
import com.example.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listDataGithub = MutableLiveData<List<ItemsItem>>()
    val listDataGithub : LiveData<List<ItemsItem>> = _listDataGithub

    private val _listFollowing = MutableLiveData<List<ItemsItem>?>()
    val listFollowing : LiveData<List<ItemsItem>?> = _listFollowing

    private val _listFollower = MutableLiveData<List<ItemsItem>?>()
    val listFollower : LiveData<List<ItemsItem>?> = _listFollower

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading



    init {
        findGithubUsers("Agils")
    }

    fun findGithubUsers(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDataGithub(query)
        client.enqueue(object : retrofit2.Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listDataGithub.value = responseBody.items
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()} ")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowers(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : retrofit2.Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollower.value = responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()} ")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowings(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : retrofit2.Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowing.value = responseBody
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()} ")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}