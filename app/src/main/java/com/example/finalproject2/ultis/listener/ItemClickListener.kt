package com.example.finalproject2.ultis.listener

interface ItemClickListener<in T> {
    fun onClick(model: T)
    fun onLongClick(model: T)
}
