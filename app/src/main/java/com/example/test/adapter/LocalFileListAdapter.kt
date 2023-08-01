package com.example.test.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.speedmatch.databinding.ItemListBinding
import java.io.File

class LocalFileListAdapter : BaseAdapter<File>() {

    inner class ListViewHolder(val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
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
                Log.d("FileListAdapter", "onBindViewHolder: $it")
                Glide.with(binding.ivImageView).load(it).into(binding.ivImageView)
            }
        }
    }
}