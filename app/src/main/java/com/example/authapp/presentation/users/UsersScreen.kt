package com.example.authapp.presentation.users

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.authapp.presentation.users.UsersViewModel
import com.example.authapp.presentation.users.UsersViewModelFactory
import kotlin.collections.get

@Composable
fun UsersScreen(viewModel: UsersViewModel = viewModel(factory = UsersViewModelFactory())) {
    val usersByApp by viewModel.usersByApp.observeAsState(emptyMap())

    // Lanza la petición la primera vez que la pantalla se compone
    LaunchedEffect(Unit) {
        viewModel.fetchUsers()
    }

    var selectedTab by remember { mutableStateOf(0) }
    val apps = usersByApp.keys.toList()

    Column {
        if (apps.isNotEmpty()) {
            TabRow(selectedTabIndex = selectedTab) {
                apps.forEachIndexed { index, app ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(app) }
                    )
                }
            }

            val selectedApp = apps.getOrNull(selectedTab)
            val users = usersByApp[selectedApp] ?: emptyList()

            LazyColumn {
                items(users) { user ->
                    Text(text = user.name)
                }
            }
        } else {
            Text("No hay usuarios disponibles")
        }
    }
}
