package edu.cit.capuras.studymate.mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = StudyMatePurple,
    onPrimary = StudyMateSurface,
    primaryContainer = StudyMatePurpleDark,
    background = StudyMateBackground,
    surface = StudyMateSurface,
    error = StudyMateError
)

private val DarkColors = darkColorScheme(
    primary = StudyMatePurple,
    onPrimary = StudyMateSurface,
    primaryContainer = StudyMatePurpleDark,
    error = StudyMateError
)

@Composable
fun StudyMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = StudyMateTypography,
        content = content
    )
}
