package com.example.projectmanager.ui

import com.example.projectmanager.data.Project

/**
 * ProjectState represents the single source of truth for the UI.
 */
data class ProjectState(
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * ProjectIntent represents the user actions.
 */
sealed class ProjectIntent {
    object LoadProjects : ProjectIntent()
    data class AddProject(val title: String, val description: String) : ProjectIntent()
    data class UpdateProject(val project: Project) : ProjectIntent()
    data class DeleteProject(val project: Project) : ProjectIntent()
}
