package com.example.store.presentation.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.store.R

class MainListDetailFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_list_preferences, rootKey)
    }
}