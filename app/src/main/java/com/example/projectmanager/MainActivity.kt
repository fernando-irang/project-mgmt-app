package com.example.projectmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectmanager.data.ProjectDatabase
import com.example.projectmanager.ui.ProjectScreen
import com.example.projectmanager.ui.ProjectViewModel
import com.example.projectmanager.ui.ProjectViewModelFactory
import com.example.projectmanager.ui.theme.ProjectManagementAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = ProjectDatabase.getDatabase(applicationContext)
        val factory = ProjectViewModelFactory(database.projectDao())

        setContent {
            ProjectManagementAppTheme {
                val viewModel: ProjectViewModel = viewModel(factory = factory)
                ProjectScreen(viewModel = viewModel)
            }
        }
    }
}
