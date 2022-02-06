package com.example.store.repository.data.entities

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize


@Parcelize
data class MainListData(
    val listName: String = "",
    val listType: String = "",
    @get:Exclude
    var documentId: String = ""
): Parcelable
