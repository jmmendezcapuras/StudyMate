package edu.cit.capuras.studymate.mobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val StudyMateColors = lightColorScheme(
    primary = StudyMateAccent,
    onPrimary = StudyMateSurface,
    primaryContainer = StudyMateAccentSoft,
    onPrimaryContainer = StudyMateAccentHover,
    secondary = StudyMateHighlight,
    onSecondary = StudyMateInk,
    secondaryContainer = StudyMateHighlightSoft,
    background = StudyMateBg,
    onBackground = StudyMateInk,
    surface = StudyMateSurface,
    onSurface = StudyMateInk,
    surfaceVariant = StudyMateBg,
    onSurfaceVariant = StudyMateInkSoft,
    outline = StudyMateBorder,
    error = StudyMateDanger,
    errorContainer = StudyMateDangerSoft,
    onErrorContainer = StudyMateDanger
)

@Composable
fun StudyMateTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = StudyMateColors,
        typography = StudyMateTypography,
        content = content
    )
}
