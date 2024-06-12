package com.example.foodfinder11.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfinder11.databinding.UserCardBinding
import com.example.foodfinder11.model.User

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    private lateinit var onItemClick: UsersAdapter.OnItemClicked

    inner class UsersViewHolder(val binding: UserCardBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersAdapter.UsersViewHolder {
        return UsersViewHolder(
            UserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: UsersAdapter.UsersViewHolder, position: Int) {

        val user = differ.currentList[position]

        holder.binding.name.text = user.name
        holder.binding.email.text = user.email
        holder.binding.role.text = "${user.role.name}"


        holder.binding.cardView.setOnClickListener {
            onItemClick.onClickListener(user)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun onItemClicked(onItemClick: OnItemClicked) {
        this.onItemClick = onItemClick
    }

    interface OnItemClicked {
        fun onClickListener(user: User);
    }

}