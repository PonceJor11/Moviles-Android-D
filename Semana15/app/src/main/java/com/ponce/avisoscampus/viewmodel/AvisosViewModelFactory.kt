package com.ponce.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ponce.avisoscampus.data.AvisosRepository

class AvisosViewModelFactory(
    private val repo: AvisosRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = AvisosViewModel(repo) as T
}
