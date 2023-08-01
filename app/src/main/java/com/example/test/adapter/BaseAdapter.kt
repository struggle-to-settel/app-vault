package com.example.test.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.speedmatch.databinding.ProgressBarBinding


abstract class BaseAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    AdapterUtils<T> {

    protected val itemList = mutableListOf<T?>()

    override fun getItemCount() = itemList.count()

    override fun getItem(position: Int) = itemList[position]

    override fun clear() {
        itemList.clear()
        notifyDataSetChanged()
    }

    override fun removeAt(position: Int) {
        itemList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun add(list: List<T>) {
        val count = itemList.size
        itemList.addAll(list)
        notifyItemRangeInserted(count, list.count())
    }

    override fun add(item: T) {
        val count = itemList.size
        itemList.add(item)
        notifyItemInserted(count)
    }

    override fun set(list: List<T>) {
        val count = itemList.size
        itemList.clear()
        notifyItemRangeRemoved(0, count)
        itemList.addAll(list)
        notifyItemRangeInserted(0, list.count())
    }


    inner class LoadingViewHolder(binding: ProgressBarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {

        }
    }

    companion object {
        const val LOADING_VIEW = 0
        const val CONTENT_VIEW = 1
    }

}