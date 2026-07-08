package edu.cit.capuras.studymate.mobile.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import edu.cit.capuras.studymate.mobile.DashboardViewModel
import edu.cit.capuras.studymate.mobile.network.dto.SubjectResponse
import edu.cit.capuras.studymate.mobile.ui.theme.*

/**
 * Mobile counterpart of web/src/pages/Dashboard.jsx — Subject Management only.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    userId: Long,
    username: String,
    viewModel: DashboardViewModel,
    onLogout: () -> Unit
) {
    LaunchedEffect(userId) {
        viewModel.loadData(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(StudyMateAccent)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("StudyMate", style = MaterialTheme.typography.titleLarge)
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Filled.Logout, contentDescription = "Log out")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StudyMateSurface,
                    titleContentColor = StudyMateInk
                )
            )
        },
        containerColor = StudyMateBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            GreetingBlock(username)
            Spacer(modifier = Modifier.height(20.dp))

            viewModel.loadError?.let { error ->
                ErrorBanner(error)
                Spacer(modifier = Modifier.height(16.dp))
            }

            SubjectsPanel(userId = userId, viewModel = viewModel)
        }
    }
}

@Composable
private fun GreetingBlock(username: String) {
    Column(modifier = Modifier.padding(top = 20.dp)) {
        Text("Welcome back, $username", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            "Add the subjects you're studying to get started.",
            style = MaterialTheme.typography.bodyMedium,
            color = StudyMateInkSoft
        )
    }
}

@Composable
private fun SubjectsPanel(userId: Long, viewModel: DashboardViewModel) {
    var newSubjectName by remember { mutableStateOf("") }

    PanelCard {
        Text("Subjects", style = MaterialTheme.typography.titleMedium)
        Text(
            "Add each course you're tracking.",
            style = MaterialTheme.typography.bodySmall,
            color = StudyMateInkSoft,
            modifier = Modifier.padding(bottom = 12.dp, top = 2.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newSubjectName,
                onValueChange = { newSubjectName = it },
                placeholder = { Text("e.g. Data Structures") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    viewModel.addSubject(userId, newSubjectName)
                    newSubjectName = ""
                },
                enabled = !viewModel.isAddingSubject,
                colors = ButtonDefaults.buttonColors(
                    containerColor = StudyMateAccentSoft,
                    contentColor = StudyMateAccentHover
                )
            ) {
                Text(if (viewModel.isAddingSubject) "Adding…" else "+ Add")
            }
        }

        viewModel.subjectActionError?.let {
            Spacer(modifier = Modifier.height(8.dp))
            ErrorBanner(it)
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (viewModel.isLoading) {
            Text("Loading your subjects…", style = MaterialTheme.typography.bodySmall, color = StudyMateInkFaint)
        } else if (viewModel.subjects.isEmpty()) {
            Text(
                "No subjects yet — add your first one above.",
                style = MaterialTheme.typography.bodySmall,
                color = StudyMateInkFaint
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                viewModel.subjects.forEach { subject -> SubjectChip(subject) }
            }
        }
    }
}

@Composable
private fun SubjectChip(subject: SubjectResponse) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StudyMateBg)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(9.dp)
                .clip(RoundedCornerShape(50))
                .background(colorForSubject(subject.id))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(subject.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PanelCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = StudyMateSurface),
        border = BorderStroke(1.dp, StudyMateBorder),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
private fun ErrorBanner(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StudyMateDangerSoft)
            .padding(10.dp)
    ) {
        Text(message, color = StudyMateDanger, style = MaterialTheme.typography.bodySmall)
    }
}
