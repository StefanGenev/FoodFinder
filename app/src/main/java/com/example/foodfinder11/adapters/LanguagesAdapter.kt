package com.example.foodfinder11.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodfinder11.R
import com.example.foodfinder11.databinding.LanguageCardItemBinding
import com.example.foodfinder11.dataObjects.Languages
import com.example.foodfinder11.utils.SessionManager

class LanguagesAdapter: RecyclerView.Adapter<LanguagesAdapter.LanguagesViewHolder>(){

    private var onClickListener: OnClickListener? = null

    inner class LanguagesViewHolder(val binding: LanguageCardItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Languages>() {

        override fun areItemsTheSame(oldItem: Languages, newItem: Languages): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Languages, newItem: Languages): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguagesAdapter.LanguagesViewHolder {
        return LanguagesViewHolder(
            LanguageCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false) )
    }

    override fun onBindViewHolder(holder: LanguagesAdapter.LanguagesViewHolder, position: Int) {

        val language = differ.currentList[position]
        holder.binding.tvName.text = language.getName(holder.binding.tvName.context)

        holder.binding.container.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(language)
            }
        }

        if (SessionManager.fetchLanguageLocale() == language.getLocale())
            holder.binding.container.setBackgroundColor(ContextCompat.getColor(holder.binding.container.context, R.color.light_forest))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(language: Languages)
    }

}