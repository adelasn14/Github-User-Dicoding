package com.example.submission2_cobacoba.activity

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2_cobacoba.R
import com.example.submission2_cobacoba.adapter.UserAdapter
import com.example.submission2_cobacoba.api.SettingPreferences
import com.example.submission2_cobacoba.api.User
import com.example.submission2_cobacoba.databinding.ActivityMainBinding
import com.example.submission2_cobacoba.viewModel.MainViewModel
import com.example.submission2_cobacoba.viewModel.ThemeViewModel
import com.example.submission2_cobacoba.viewModel.ViewModelFactory


class MainActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intentToDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_AVA, data.avatar_url)
                startActivity(intentToDetail)
            }

        })

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        viewModel.searchUser.observe(this) {
            if (it != null) {
                adapter.setListUser(it)
                showLoading(false)
            }
            if (it.isEmpty()) {
                binding.notFoundAnimation.visibility = View.VISIBLE
            }
        }

        binding.apply {
            val layoutManager = LinearLayoutManager(this@MainActivity)
            rvSearchUser.layoutManager = layoutManager
            rvSearchUser.addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    layoutManager.orientation
                )
            )
            rvSearchUser.setHasFixedSize(true)
            rvSearchUser.adapter = adapter
        }

        supportActionBar?.title = "Github User Navigation"
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val pref = SettingPreferences.getInstance(dataStore)
        val themeViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[ThemeViewModel::class.java]

        val switch =
            menu.findItem(R.id.toggle_mode).actionView.findViewById<ToggleButton>(R.id.toggle_theme_layout)

        themeViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switch.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switch.isChecked = false
            }
        }

        switch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeViewModel.saveThemeSetting(isChecked)
        }

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showLoading(true)
                searchView.clearFocus()
                viewModel.setSearchUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchView.setOnKeyListener { _, i, keyEvent ->
                    if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                        return@setOnKeyListener true
                    }
                    return@setOnKeyListener false
                }
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fav_menu -> {
                val intentToFav = Intent(this, FavActivity::class.java)
                startActivity(intentToFav)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}