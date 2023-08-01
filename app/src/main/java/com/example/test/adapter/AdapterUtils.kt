package com.example.test.adapter

interface AdapterUtils<T> {

    fun getItem(position: Int): T?

    fun set(list: List<T>)

    fun add(item: T)

    fun add(list: List<T>)

    fun removeAt(position: Int)

    fun clear()

}
