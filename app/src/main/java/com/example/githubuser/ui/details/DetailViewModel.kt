package com.example.githubuser.ui.details
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.DetailUserResponse
import com.example.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _detailUserGithub = MutableLiveData<DetailUserResponse?>()
    val detailUserGithub: LiveData<DetailUserResponse?> = _detailUserGithub

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "com.example.githubuser.ui.details.DetailViewModel"
    }

    fun findDetailUserGithub(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : retrofit2.Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailUserGithub.value = responseBody
                    } else {
                        Log.e(TAG, "onResponse: Response body is null")
                    }
                } else {
                    Log.e(TAG, "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}
