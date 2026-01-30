package com.example.projectmanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectmanager.data.Project
import com.example.projectmanager.data.ProjectDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProjectViewModel(private val projectDao: ProjectDao) : ViewModel() {

    private val _state = MutableStateFlow(ProjectState())
    val state: StateFlow<ProjectState> = _state.asStateFlow()

    init {
        handleIntent(ProjectIntent.LoadProjects)
    }

    fun handleIntent(intent: ProjectIntent) {
        when (intent) {
            is ProjectIntent.LoadProjects -> loadProjects()
            is ProjectIntent.AddProject -> addProject(intent.title, intent.description)
            is ProjectIntent.UpdateProject -> updateProject(intent.project)
            is ProjectIntent.DeleteProject -> deleteProject(intent.project)
        }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            projectDao.getAllProjects()
                .catch { e ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = e.message ?: "An unknown error occurred"
                    )
                }
                .collect { projects ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        projects = projects,
                        error = null
                    )
                }
        }
    }

    private fun addProject(title: String, description: String) {
        viewModelScope.launch {
            try {
                val newProject = Project(title = title, description = description)
                projectDao.insertProject(newProject)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Could not add project")
            }
        }
    }

    private fun updateProject(project: Project) {
        viewModelScope.launch {
            try {
                projectDao.updateProject(project)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Could not update project")
            }
        }
    }

    private fun deleteProject(project: Project) {
        viewModelScope.launch {
            try {
                projectDao.deleteProject(project)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message ?: "Could not delete project")
            }
        }
    }
}
