package com.example.store.repository.data.entities

data class CustomListData(
    val listName: String = "",
    val weightList: List<String> = emptyList(),
    val priceList: List<String> = emptyList()
)
