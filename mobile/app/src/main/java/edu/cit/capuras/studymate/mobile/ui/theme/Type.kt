package edu.cit.capuras.studymate.mobile.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Web uses "Fraunces" for headings, "Inter" for body copy, and
// "IBM Plex Mono" for numeric/duration values. Custom font files aren't
// bundled here to keep the module lightweight, so we approximate the same
// hierarchy (serif display / sans body / monospace data) with system
// font families — swap FontFamily.Serif / FontFamily.Default for the
// actual Fraunces/Inter font resources later if pixel-perfect parity
// with the web app is required.
val DisplayFontFamily = FontFamily.Serif
val BodyFontFamily = FontFamily.Default
val MonoFontFamily = FontFamily.Monospace

val StudyMateTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp
    ),
    titleLarge = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    titleMedium = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelLarge = TextStyle(
        fontFamily = BodyFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = MonoFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
)
