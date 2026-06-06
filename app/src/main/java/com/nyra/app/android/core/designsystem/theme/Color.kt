package com.nyra.app.android.core.designsystem.theme

import androidx.compose.ui.graphics.Color

// --- Base System Colors ---
val NyraDeepBlue = Color(0xFF263B4A)
val NyraMutedBlue = Color(0xFF6F8FA3)
val NyraSoftMist = Color(0xFFE8E2D8)
val NyraWarmGlow = Color(0xFFE8B982)
val NyraSurface = Color(0x33FFFFFF)
val NyraTextPrimary = Color(0xFFF5F0E8)
val NyraTextSecondary = Color(0xCCF5F0E8)

// --- Time of Day Gradients (3 Colors) ---

// MORNING: #223043 -> #162231
val NyraMorningTop = Color(0xFF223043)
val NyraMorningMiddle = Color(0xFF1C293A)
val NyraMorningBottom = Color(0xFF162231)

// AFTERNOON
val NyraAfternoonTop = Color(0xFF1D3325)
val NyraAfternoonMiddle = Color(0xFF293F2B)
val NyraAfternoonBottom = Color(0xFF14231C)

// EVENING: #2B313B -> #1A1523
val NyraEveningTop = Color(0xFF2B313B)
val NyraEveningMiddle = Color(0xFF22232F)
val NyraEveningBottom = Color(0xFF1A1523)

// NIGHT / QUIET: #0F1726 -> #081120
val NyraQuietTop = Color(0xFF0F1726)
val NyraQuietMiddle = Color(0xFF0B1423)
val NyraQuietBottom = Color(0xFF081120)

// --- Emotional States Gradients ---
val NyraCalmTop = Color(0xFF1C2431)
val NyraCalmMiddle = Color(0xFF181F2A)
val NyraCalmBottom = Color(0xFF141A24)

val NyraTiredTop = Color(0xFF2A1F26)
val NyraTiredMiddle = Color(0xFF201A21)
val NyraTiredBottom = Color(0xFF17161C)

val NyraRestlessTop = Color(0xFF241127)
val NyraRestlessMiddle = Color(0xFF1C0820)
val NyraRestlessBottom = Color(0xFF16001A)

val NyraGroundedTop = Color(0xFF1B211C)
val NyraGroundedMiddle = Color(0xFF171C18)
val NyraGroundedBottom = Color(0xFF131814)

val NyraNightTop = NyraQuietTop
val NyraNightMiddle = NyraQuietMiddle
val NyraNightBottom = NyraQuietBottom

// --- Gradients ---
val EveningGradient = listOf(NyraEveningTop, NyraEveningMiddle, NyraEveningBottom)
val MorningGradient = listOf(NyraMorningTop, NyraMorningMiddle, NyraMorningBottom)
val NightGradient = listOf(NyraQuietTop, NyraQuietMiddle, NyraQuietBottom)
val AfternoonGradient = listOf(NyraAfternoonTop, NyraAfternoonMiddle, NyraAfternoonBottom)

// --- UI Components ---
val CardWhiteOverlay = Color(0x1FFFFFFF)
val CardStrokeLight = Color(0x22FFFFFF)

val CardNight = Color(0x3323374F)
val CardMorning = Color(0x335B7071)
val CardEvening = Color(0x334C3557)
val CardQuiet = Color(0x33203858)
val CardAfternoon = Color(0x33344A35)

val BottomBarDark = Color(0x66101524)
val BottomBarLightStroke = Color(0x18FFFFFF)
val ActiveIconAmber = Color(0xFFFFC27A)
val InactiveIcon = Color(0x99FFFFFF)
val ButtonCircle = Color(0x22FFFFFF)
val ButtonCircleActive = Color(0x33FFC27A)

// --- Gem & Presence Visuals ---
val GemAmberStrong = Color(0xFFFFA53D)
val GemAmberSoft = Color(0x66FFB357)
val GemBlueGlow = Color(0xFF7DAEFF)
val GemGreenGlow = Color(0xFFB7C978)
val GemPeachGlow = Color(0xFFFF8F6A)
val GemPinkGlow = Color(0xFFD889FF)
val GemPurpleGlow = Color(0xFF9B6CFF)

val ShadowStrong = Color(0x99000000)
val HighlightWhite = Color(0x66FFFFFF)
val HighlightCool = Color(0x667DAEFF)

// Aliases for shorter usage if needed
val TextPrimary = NyraTextPrimary
val TextSecondary = NyraTextSecondary

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
