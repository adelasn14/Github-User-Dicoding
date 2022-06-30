package com.example.submission2_cobacoba.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2_cobacoba.adapter.UserAdapter
import com.example.submission2_cobacoba.api.FavoriteUser
import com.example.submission2_cobacoba.api.User
import com.example.submission2_cobacoba.databinding.ActivityFavBinding
import com.example.submission2_cobacoba.viewModel.DetailUserViewModel

private lateinit var binding: ActivityFavBinding
private lateinit var adapter: UserAdapter
private lateinit var viewModel: DetailUserViewModel

class FavActivity : AppCompatActivity() {
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Github User Favorite"

        viewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intentToDetail = Intent(this@FavActivity, DetailUserActivity::class.java)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_AVA, data.avatar_url)
                startActivity(intentToDetail)
            }
        })

        binding.apply {
            val layoutManager = LinearLayoutManager(this@FavActivity)
            rvUser.layoutManager = layoutManager
            rvUser.addItemDecoration(
                DividerItemDecoration(
                    this@FavActivity,
                    layoutManager.orientation
                )
            )
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }

        viewModel.getFavUser()?.observe(this) {
            if (it != null) {
                val list = mappingList(it)
                adapter.setListUser(list)
            }
        }
    }

    private fun mappingList(users: List<FavoriteUser>): ArrayList<User> {
        val listOfUser = ArrayList<User>()
        for (user in users) {
            val userMapped = User(
                user.login,
                user.id,
                user.avatar_url
            )
            listOfUser.add(userMapped)
        }
        return listOfUser
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}