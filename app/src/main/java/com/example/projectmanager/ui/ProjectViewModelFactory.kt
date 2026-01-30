package com.example.projectmanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projectmanager.data.ProjectDao

class ProjectViewModelFactory(private val projectDao: ProjectDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectViewModel(projectDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
