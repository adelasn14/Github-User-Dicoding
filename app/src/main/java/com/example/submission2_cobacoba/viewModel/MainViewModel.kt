package com.example.submission2_cobacoba.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.submission2_cobacoba.api.ApiConfig
import com.example.submission2_cobacoba.api.User
import com.example.submission2_cobacoba.api.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val _searchUser = MutableLiveData<ArrayList<User>>()
    val searchUser: LiveData<ArrayList<User>> = _searchUser

    companion object {
        const val TAG = "MainViewModel"
    }

    fun setSearchUser(query: String) {
        val client = ApiConfig.getApiService().getSearchUser(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    _searchUser.value = response.body()?.users
                    Log.d(TAG, response.toString())
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }
}
