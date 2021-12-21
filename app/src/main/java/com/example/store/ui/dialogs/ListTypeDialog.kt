package com.example.store.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.store.R
import java.lang.IllegalStateException

class ListTypeDialog(
    val positionButtonAction: (String) -> Unit
) : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val listType = it.resources.getStringArray(R.array.list_type)
            var selectedTypePosition = 0
            val builder = AlertDialog.Builder(it)

            builder.setTitle("List Type")
                .setSingleChoiceItems(listType, 0){ _,selectedPosition ->
                    selectedTypePosition = selectedPosition
                }
                .setPositiveButton("Yes") {_,_->
                    positionButtonAction(listType[selectedTypePosition])
                }
                .setNegativeButton("No") {_ , _ ->
                    dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}