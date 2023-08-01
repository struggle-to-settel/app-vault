package com.example.test.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmatch.databinding.ItemListBinding
import com.example.test.Common.loadRoundCorners
import com.example.test.User

class FileListAdapter(private val handle: (Int) -> Unit) : BaseAdapter<User>() {


    inner class ListViewHolder(val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                getItem(adapterPosition)?.let {
                    if (it.userId.startsWith("http")) handle.invoke(adapterPosition)
                    else if (it.profile?.startsWith("http") == true) {
                        handle.invoke(adapterPosition)
                    }

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ListViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            (holder as ListViewHolder).apply {
//                Log.d("ListAdapter", "onBindViewHolder: ${it.sources[0]}")
                if (it.userId.startsWith("http")) binding.ivImageView.loadRoundCorners(it.userId)
                else if (it.profile?.startsWith("http") == true) binding.ivImageView.loadRoundCorners(
                    it.profile
                )
                else binding.ivImageView.setBackgroundColor(Color.GRAY)
            }
        }
    }
}