package com.example.store.ui.utils

fun emptyTextFieldCheck(vararg textFields: String): Boolean{
    for (item in textFields) {
        if(item.isEmpty()) {
            return true
        }
    }
    return false
}