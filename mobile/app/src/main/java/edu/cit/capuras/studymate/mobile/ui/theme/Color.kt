package edu.cit.capuras.studymate.mobile.ui.theme

import androidx.compose.ui.graphics.Color

// Mirrors the CSS custom properties in web/src/index.css so the mobile app
// shares the exact same visual identity as the web client.
val StudyMateBg = Color(0xFFF4F6F5)
val StudyMateSurface = Color(0xFFFFFFFF)
val StudyMateInk = Color(0xFF17241F)
val StudyMateInkSoft = Color(0xFF5B6D64)
val StudyMateInkFaint = Color(0xFF93A39C)
val StudyMateAccent = Color(0xFF10665A)
val StudyMateAccentHover = Color(0xFF0C4F45)
val StudyMateAccentSoft = Color(0xFFE4F1EE)
val StudyMateHighlight = Color(0xFFF2A93B)
val StudyMateHighlightSoft = Color(0xFFFCEBCC)
val StudyMateBorder = Color(0xFFDCE3E0)
val StudyMateDanger = Color(0xFFC1443B)
val StudyMateDangerSoft = Color(0xFFFBEAE8)

// Subject accent palette — same hex values used by colorForSubject() in
// web/src/pages/Dashboard.jsx, so a given subject shows the same color on
// both platforms.
val SubjectColors = listOf(
    Color(0xFF10665A),
    Color(0xFFF2A93B),
    Color(0xFF3454D1),
    Color(0xFFC1443B),
    Color(0xFF7A5AF8),
    Color(0xFF0E9488),
)

fun colorForSubject(id: Long): Color = SubjectColors[(id % SubjectColors.size).toInt()]
