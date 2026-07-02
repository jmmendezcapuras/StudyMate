package edu.cit.capuras.studymate.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.cit.capuras.studymate.mobile.network.SessionManager
import edu.cit.capuras.studymate.mobile.ui.screens.DashboardScreen
import edu.cit.capuras.studymate.mobile.ui.screens.LoginScreen
import edu.cit.capuras.studymate.mobile.ui.screens.RegisterScreen
import edu.cit.capuras.studymate.mobile.ui.theme.StudyMateTheme

private object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val DASHBOARD = "dashboard/{username}"

    fun dashboard(username: String) = "dashboard/$username"
}

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudyMateTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    StudyMateApp(authViewModel = authViewModel)
                }
            }
        }
    }
}

@Composable
fun StudyMateApp(authViewModel: AuthViewModel) {
    val navController: NavHostController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current

    val startDestination = if (SessionManager.isLoggedIn(context)) {
        Routes.dashboard(SessionManager.getUsername(context) ?: "")
    } else {
        Routes.LOGIN
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = { id, username ->
                    SessionManager.saveSession(context, id, username)
                    navController.navigate(Routes.dashboard(username)) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { id, username ->
                    SessionManager.saveSession(context, id, username)
                    navController.navigate(Routes.dashboard(username)) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        composable(Routes.DASHBOARD) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            DashboardScreen(
                username = username,
                onLogout = {
                    SessionManager.clearSession(context)
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
