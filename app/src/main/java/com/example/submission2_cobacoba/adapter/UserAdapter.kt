package com.example.submission2_cobacoba.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.submission2_cobacoba.api.User
import com.example.submission2_cobacoba.databinding.ItemUserBinding

class UserAdapter : RecyclerView.Adapter<UserAdapter.SearchViewHolder>() {

    private val searchUser = ArrayList<User>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListUser(users: ArrayList<User>) {
        searchUser.clear()
        searchUser.addAll(users)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val (login, id, ava) = searchUser[position]
        holder.binding.tvSearchUsername.text = StringBuilder().append("@").append(login)
        holder.binding.tvSearchId.text = id.toString()
        Glide.with(holder.itemView.context)
            .load(ava)
            .circleCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.imgSearchAvatar)
        holder.binding.imgSearchAvatar.setOnClickListener {
            onItemClickCallback.onItemClicked(searchUser[holder.adapterPosition])
        }
    }

    override fun getItemCount() = searchUser.size

    class SearchViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}
