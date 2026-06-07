package com.nyra.app.android.core.splash.runtime

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

/**
 * Cinematic HORIZON splash screen.
 *
 * Hosts [NyraSplashScene] full-bleed for [holdMillis] before crossfading into
 * the [next] composable. Defaults give a calm, generous reveal — the spec
 * insists on "extremely slow, meditative" motion, so the splash sits visible
 * for a moment before yielding to the app surface.
 *
 * The eclipse halo continues to breathe and the parallax layers continue to
 * drift through the entire hold + crossfade, so the transition feels like the
 * atmosphere *handing off* to the app rather than the splash *dismissing*.
 *
 * Wire from `MainActivity`:
 *
 * ```kotlin
 * NyraSplashScreen {
 *     NyraRootRoute(modifier = Modifier.fillMaxSize())
 * }
 * ```
 *
 * @param holdMillis time the splash stays in front before starting the
 *   crossfade. Default 2.4s.
 * @param crossfadeMillis crossfade duration. Default 1.4s — long enough that
 *   you can still see the eclipse ring fading underneath the new surface.
 */
@Composable
fun NyraSplashScreen(
    modifier: Modifier = Modifier,
    holdMillis: Long = 2_400L,
    crossfadeMillis: Int = 1_400,
    next: @Composable () -> Unit
) {
    var splashVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(holdMillis)
        splashVisible = false
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Next surface mounts immediately so its own warm-up motion can begin
        // before the splash starts crossing out. Eye reads them as overlapping
        // emotional climates rather than two screens.
        next()

        AnimatedVisibility(
            visible = splashVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 200)),
            exit = fadeOut(animationSpec = tween(durationMillis = crossfadeMillis))
        ) {
            NyraSplashScene(modifier = Modifier.fillMaxSize())
        }
    }
}
