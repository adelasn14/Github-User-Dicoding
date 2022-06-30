package com.example.submission2_cobacoba.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.submission2_cobacoba.adapter.SectionPagerAdapter
import com.example.submission2_cobacoba.databinding.ActivityDetailUserBinding
import com.example.submission2_cobacoba.viewModel.DetailUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVA = "extra_avatar"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avaUrl = intent.getStringExtra(EXTRA_AVA)
        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]

        showLoading(true)
        username?.let { viewModel.setDetailUser(it) }
        viewModel.detailUser.observe(this) {
            if (it != null) {
                showLoading(false)
                binding.apply {
                    tvItemName.text = it.name
                    tvItemUsername.text = StringBuilder().append("＠").append(it.login)
                    tvItemCompany.text = StringBuilder().append("🏢 ").append(it.company)
                    tvItemLocation.text = StringBuilder().append("📍 ").append(it.location)
                    tvItemFollowing.text = StringBuilder().append(it.following).append(" Following")
                    tvItemFollowers.text = StringBuilder().append(it.followers).append(" Followers")
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatar_url)
                        .circleCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imgItemAvatar)
                }
            }
        }

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        binding.toggleFavorite.isChecked = true
                        _isChecked = true
                    } else {
                        binding.toggleFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.toggleFavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked) {
                viewModel.addToFav(username.toString(), id, avaUrl.toString())
            } else {
                viewModel.removeFromFav(id)
            }
            binding.toggleFavorite.isChecked = _isChecked
        }

        binding.btnShare.setOnClickListener {
            val shareUser = Intent(Intent.ACTION_SEND)
            shareUser.type = "text/plain"
            val textOnShare = "Share this user profile via"
            shareUser.putExtra(Intent.EXTRA_TEXT, textOnShare)
            startActivity(Intent.createChooser(shareUser, "Share Via"))
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabFollow.setupWithViewPager(viewPager)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}
