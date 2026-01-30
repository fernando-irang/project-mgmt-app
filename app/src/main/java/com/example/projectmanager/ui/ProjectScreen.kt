package com.example.projectmanager.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.projectmanager.data.Project
import com.example.projectmanager.ui.components.AddEditProjectDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(viewModel: ProjectViewModel) {
    val state by viewModel.state.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var selectedProject by remember { mutableStateOf<Project?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Project Manager") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                selectedProject = null
                showDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Project")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                state.projects.isEmpty() -> {
                    Text(
                        text = "No projects found.\nTap + to add one!",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                            .align(Alignment.Center),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(state.projects) { project ->
                            ProjectItem(
                                project = project,
                                onEdit = {
                                    selectedProject = project
                                    showDialog = true
                                },
                                onDelete = {
                                    viewModel.handleIntent(ProjectIntent.DeleteProject(project))
                                }
                            )
                        }
                    }
                }
            }

            if (showDialog) {
                AddEditProjectDialog(
                    project = selectedProject,
                    onDismiss = { showDialog = false },
                    onConfirm = { title, description ->
                        if (selectedProject == null) {
                            viewModel.handleIntent(ProjectIntent.AddProject(title, description))
                        } else {
                            viewModel.handleIntent(
                                ProjectIntent.UpdateProject(
                                    selectedProject!!.copy(title = title, description = description)
                                )
                            )
                        }
                        showDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun ProjectItem(
    project: Project,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = project.title, style = MaterialTheme.typography.titleLarge)
                Text(text = project.description, style = MaterialTheme.typography.bodyMedium)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
