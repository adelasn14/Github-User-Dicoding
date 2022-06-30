package com.example.submission2_cobacoba.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submission2_cobacoba.R
import com.example.submission2_cobacoba.activity.DetailUserActivity
import com.example.submission2_cobacoba.adapter.UserAdapter
import com.example.submission2_cobacoba.api.User
import com.example.submission2_cobacoba.databinding.FragmentFollowBinding
import com.example.submission2_cobacoba.viewModel.DetailUserViewModel

class FollowersFragment : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: DetailUserViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowBinding.bind(view)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                val intentToDetail = Intent(activity, DetailUserActivity::class.java)
                intentToDetail.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                startActivity(intentToDetail)
            }
        })

        binding?.apply {
            rvUser.layoutManager = LinearLayoutManager(activity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter
        }

        viewModel =
            ViewModelProvider(this)[DetailUserViewModel::class.java]
        viewModel.setListFollowers(username)
        viewModel.listFollowers.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setListUser(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}