package com.tcltv.sahne.ui.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tcltv.sahne.ui.LocalReduceMotion

/**
 * Fluid, hardware-layer D-pad focus motion inspired by tvOS.
 *
 * Focus changes remain immediate for remote navigation; only the rendering follows a
 * critically damped-ish spring, so repeated arrow presses do not queue transitions.
 * Reading state inside graphicsLayer avoids per-frame recomposition / layout.
 */
@Composable
internal fun Modifier.tvFocusMotion(
    active: Boolean,
    targetScale: Float,
    rise: Dp = 6.dp,
    cornerRadius: Dp = 15.dp,
): Modifier {
    val reduceMotion = LocalReduceMotion.current
    val scale = animateFloatAsState(
        targetValue = if (active) targetScale else 1f,
        animationSpec = if (reduceMotion) snap() else spring(
            dampingRatio = 0.78f,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "fluidFocusScale",
    )
    val translation = animateDpAsState(
        targetValue = if (active) -rise else 0.dp,
        animationSpec = if (reduceMotion) snap() else spring(
            dampingRatio = 0.84f,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "fluidFocusRise",
    )
    val elevation = animateDpAsState(
        targetValue = if (active) 14.dp else 0.dp,
        animationSpec = if (reduceMotion) snap() else spring(
            dampingRatio = 0.88f,
            stiffness = Spring.StiffnessMedium,
        ),
        label = "fluidFocusDepth",
    )
    return graphicsLayer {
        scaleX = scale.value
        scaleY = scale.value
        translationY = translation.value.toPx()
        shadowElevation = elevation.value.toPx()
        shape = RoundedCornerShape(cornerRadius)
    }
}
