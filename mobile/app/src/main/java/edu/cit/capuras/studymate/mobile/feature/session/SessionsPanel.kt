package edu.cit.capuras.studymate.mobile.feature.session

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import edu.cit.capuras.studymate.mobile.feature.subject.SubjectResponse
import edu.cit.capuras.studymate.mobile.ui.theme.*
import java.time.LocalDate

// SessionsPanel is the session slice's UI. It takes the list of subjects
// (owned by the subject slice) purely to populate the subject picker — the
// session slice's one intentional cross-slice dependency, mirroring the
// backend's session -> subject dependency.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionsPanel(userId: Long, subjects: List<SubjectResponse>, viewModel: SessionViewModel) {
    LaunchedEffect(userId) {
        viewModel.loadData(userId)
    }

    var selectedSubjectId by remember(subjects) { mutableStateOf(subjects.firstOrNull()?.id) }
    var duration by remember { mutableStateOf("") }
    var sessionDate by remember { mutableStateOf(LocalDate.now().toString()) }
    var notes by remember { mutableStateOf("") }
    var subjectMenuExpanded by remember { mutableStateOf(false) }

    PanelCard {
        Text("Study Sessions", style = MaterialTheme.typography.titleMedium)
        Text(
            "Log time you've spent studying each subject.",
            style = MaterialTheme.typography.bodySmall,
            color = StudyMateInkSoft,
            modifier = Modifier.padding(bottom = 12.dp, top = 2.dp)
        )

        viewModel.loadError?.let {
            ErrorBanner(it)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Box {
            OutlinedTextField(
                value = subjects.firstOrNull { it.id == selectedSubjectId }?.name ?: "Add a subject first",
                onValueChange = {},
                readOnly = true,
                label = { Text("Subject") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = subjects.isNotEmpty()) { subjectMenuExpanded = true }
            )
            DropdownMenu(expanded = subjectMenuExpanded, onDismissRequest = { subjectMenuExpanded = false }) {
                subjects.forEach { subject ->
                    DropdownMenuItem(
                        text = { Text(subject.name) },
                        onClick = {
                            selectedSubjectId = subject.id
                            subjectMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            OutlinedTextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Minutes") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = sessionDate,
                onValueChange = { sessionDate = it },
                label = { Text("Date (YYYY-MM-DD)") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes (optional)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                viewModel.addSession(userId, selectedSubjectId, duration, sessionDate, notes)
                duration = ""
                notes = ""
            },
            enabled = !viewModel.isSaving && subjects.isNotEmpty(),
            colors = ButtonDefaults.buttonColors(
                containerColor = StudyMateAccentSoft,
                contentColor = StudyMateAccentHover
            )
        ) {
            Text(if (viewModel.isSaving) "Saving…" else "+ Log Session")
        }

        viewModel.sessionActionError?.let {
            Spacer(modifier = Modifier.height(8.dp))
            ErrorBanner(it)
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (viewModel.isLoading) {
            Text("Loading your sessions…", style = MaterialTheme.typography.bodySmall, color = StudyMateInkFaint)
        } else if (viewModel.sessions.isEmpty()) {
            Text(
                "No study sessions logged yet.",
                style = MaterialTheme.typography.bodySmall,
                color = StudyMateInkFaint
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                viewModel.sessions.forEach { session ->
                    SessionRow(session) { viewModel.deleteSession(userId, session.id) }
                }
            }
        }
    }
}

@Composable
private fun SessionRow(session: SessionResponse, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StudyMateBg)
            .padding(horizontal = 10.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                "${session.subject.name} — ${session.durationMinutes} min on ${session.sessionDate}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (!session.notes.isNullOrBlank()) {
                Text(session.notes, style = MaterialTheme.typography.bodySmall, color = StudyMateInkSoft)
            }
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Close, contentDescription = "Delete session")
        }
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
