package com.example.store.repository.data.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomListData(
    val listName: String = "",
    val weightList: List<Int> = emptyList(),
    val priceList: List<Int> = emptyList()
): Parcelable
