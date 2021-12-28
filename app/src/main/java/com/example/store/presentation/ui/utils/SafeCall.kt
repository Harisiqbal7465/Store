package com.example.store.presentation.ui.utils

import com.example.store.utils.Resource

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T>{
    return try {
        action()
    } catch (e: Exception) {
        Resource.Error(e.message ?: "An unknown error occured")
    }
}