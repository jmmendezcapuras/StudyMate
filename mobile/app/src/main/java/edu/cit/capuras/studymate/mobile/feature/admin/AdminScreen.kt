package edu.cit.capuras.studymate.mobile.feature.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import edu.cit.capuras.studymate.mobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    username: String,
    viewModel: AdminViewModel,
    onLogout: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadUsers()
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
                        Spacer(modifier = Modifier.width(8.dp))
                        AdminBadge()
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
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.padding(top = 20.dp)) {
                Text("User Accounts", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "View every registered account and remove one if needed. Deleting a user also removes their subjects and study sessions.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = StudyMateInkSoft
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            viewModel.loadError?.let {
                ErrorBanner(it)
                Spacer(modifier = Modifier.height(12.dp))
            }
            viewModel.actionError?.let {
                ErrorBanner(it)
                Spacer(modifier = Modifier.height(12.dp))
            }

            CreateAdminPanel(viewModel)

            Spacer(modifier = Modifier.height(20.dp))

            UsersPanel(viewModel)

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun AdminBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(StudyMateHighlightSoft)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text("Admin", style = MaterialTheme.typography.labelSmall, color = StudyMateInk)
    }
}

@Composable
private fun CreateAdminPanel(viewModel: AdminViewModel) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    PanelCard {
        Text("Create Admin Account", style = MaterialTheme.typography.titleMedium)
        Text(
            "Use this to give someone else (e.g. your instructor) their own admin login, instead of sharing the seeded account's password.",
            style = MaterialTheme.typography.bodySmall,
            color = StudyMateInkSoft,
            modifier = Modifier.padding(bottom = 12.dp, top = 2.dp)
        )

        viewModel.createError?.let {
            ErrorBanner(it)
            Spacer(modifier = Modifier.height(8.dp))
        }
        viewModel.createSuccess?.let {
            SuccessBanner(it)
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password (min 8 characters)") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {
                viewModel.createAdmin(username, email, password)
                username = ""
                email = ""
                password = ""
            },
            enabled = !viewModel.isCreating,
            colors = ButtonDefaults.buttonColors(
                containerColor = StudyMateAccent,
                contentColor = StudyMateSurface
            )
        ) {
            Text(if (viewModel.isCreating) "Creating…" else "Create Admin")
        }
    }
}

@Composable
private fun UsersPanel(viewModel: AdminViewModel) {
    var confirmId by remember { mutableStateOf<Long?>(null) }

    PanelCard {
        Text("All Users", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        if (viewModel.isLoading) {
            Text("Loading users…", style = MaterialTheme.typography.bodySmall, color = StudyMateInkFaint)
        } else if (viewModel.users.isEmpty()) {
            Text("No users found.", style = MaterialTheme.typography.bodySmall, color = StudyMateInkFaint)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                viewModel.users.forEach { user ->
                    UserRow(
                        user = user,
                        isDeleting = viewModel.deletingUserId == user.id,
                        isConfirming = confirmId == user.id,
                        onRequestDelete = { confirmId = user.id },
                        onCancelDelete = { confirmId = null },
                        onConfirmDelete = {
                            viewModel.deleteUser(user.id)
                            confirmId = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun UserRow(
    user: UserSummaryResponse,
    isDeleting: Boolean,
    isConfirming: Boolean,
    onRequestDelete: () -> Unit,
    onCancelDelete: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StudyMateBg)
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(user.username, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(user.email, style = MaterialTheme.typography.bodySmall, color = StudyMateInkSoft)
            }
            RolePill(user.role)
        }

        if (user.role != "ADMIN") {
            Spacer(modifier = Modifier.height(8.dp))
            if (isConfirming) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onConfirmDelete,
                        enabled = !isDeleting,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = StudyMateDanger,
                            contentColor = StudyMateSurface
                        )
                    ) {
                        Text(if (isDeleting) "Deleting…" else "Confirm delete")
                    }
                    TextButton(onClick = onCancelDelete) {
                        Text("Cancel")
                    }
                }
            } else {
                TextButton(onClick = onRequestDelete) {
                    Text("Delete", color = StudyMateDanger)
                }
            }
        }
    }
}

@Composable
private fun RolePill(role: String) {
    val (bg, fg) = if (role == "ADMIN") {
        StudyMateHighlightSoft to StudyMateInk
    } else {
        StudyMateAccentSoft to StudyMateAccentHover
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(role, style = MaterialTheme.typography.labelSmall, color = fg)
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

@Composable
private fun SuccessBanner(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(StudyMateAccentSoft)
            .padding(10.dp)
    ) {
        Text(message, color = StudyMateAccentHover, style = MaterialTheme.typography.bodySmall)
    }
}
