package radios

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.E
import kotlin.math.cos
import kotlin.math.pow

/**
 * Why these values?
 *
 * fraction: represent current point in animation timeline 0..1
 *
 * frequency: how fast the oscillation occurs. Higher means more oscillation in the same timeframe
 *
 * amplitude: This variable controls the height of the oscillation. A higher amplitude means the oscillation swings are larger
 *
 * E.pow(-fraction / amplitude): creates an exponential decay based on the fraction of the animation completed.
 * As the animation progresses (fraction increases), this term decreases,
 * causing the amplitude of the oscillation to decrease over time.
 *
 * cos(frequency * fraction):  generates the oscillation effect. As the fraction increases,
 * the cosine function cycles through its values, creating peaks
 * and troughs that represent the oscillations of the elastic animation.
 *
 * The -1 * at the beginning inverts the oscillation, so it starts in the opposite direction.
 *
 * Adding 1 at the end offsets the entire function so that it starts at 0 when fraction is 0.
 */
val bounceEasing = Easing { fraction ->
    val amplitude = 0.4
    val frequency = 14.5
    (-1 * E.pow(-fraction / amplitude) * cos(frequency * fraction) + 1).toFloat()
}

private val SwitchMinWidth = 82.dp
private val SwitchMinHeight = 36.dp
private const val SwitchDuration = 400
private const val BounceDuration = 600

@Composable
fun Switcher01(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    thumbHeight: Dp = 24.dp,
    onColor: Color = Color(0xFF62C462),
    offColor: Color = Color(0xFFD32F2F),
) {
    val currentChecked by rememberUpdatedState(newValue = isChecked)

    val switchColor by animateColorAsState(
        targetValue = if (currentChecked) onColor else offColor,
        label = "switch_color_animation"
    )

    val switchScale = remember { Animatable(1f) }

    val thumbWidth by animateDpAsState(
        targetValue = if (currentChecked) 8.dp else thumbHeight,
        animationSpec = tween(
            durationMillis = BounceDuration,
            easing = bounceEasing,
        ),
        label = "thumb_width_animation"
    )

    val animProgress by animateFloatAsState(
        if (currentChecked) 1f else 0f,
        animationSpec = tween(
            durationMillis = SwitchDuration,
        ),
        label = "thumb_offset_animation"
    )

    Canvas(
        modifier
            .defaultMinSize(
                minWidth = SwitchMinWidth,
                minHeight = SwitchMinHeight
            )
            .graphicsLayer {
                scaleX = switchScale.value
                scaleY = switchScale.value
            }
            .shadow(
                elevation = 16.dp,
                shape = CircleShape,
                ambientColor = switchColor,
                spotColor = switchColor,
                clip = true,
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        switchScale.animateTo(0.9f)
                        tryAwaitRelease()
                        switchScale.animateTo(1f)
                    },
                    onTap = {
                        onCheckedChange(!currentChecked)
                    },
                )
            },
    ) {

        val thumbXOffset = lerp(
            thumbHeight / 2,
            size.width.toDp() - thumbWidth - (thumbHeight / 2) - 4.dp,
            animProgress,
        ).toPx()

        val thumbPos = Offset(
            x = thumbXOffset,
            y = (size.height - thumbHeight.toPx()) / 2,
        )

        drawRoundRect(
            topLeft = Offset(0f, 0f),
            size = size,
            cornerRadius = CornerRadius(size.height),
            color = switchColor,
        )


        drawRoundRect(
            topLeft = thumbPos,
            size = Size(
                width = thumbWidth.toPx(),
                height = thumbHeight.toPx()
            ),
            cornerRadius = CornerRadius(thumbWidth.toPx()),
            color = Color.White,
        )

        val strokeForZero = 6.dp.toPx()

        if (isChecked.not()) {
            drawRoundRect(
                topLeft = Offset(
                    thumbPos.x + strokeForZero,
                    thumbPos.y + strokeForZero
                ),
                size = Size(
                    width = thumbWidth.toPx() - strokeForZero * 2,
                    height = thumbHeight.toPx() - strokeForZero * 2,
                ),
                cornerRadius = CornerRadius(thumbWidth.toPx()),
                color = if (currentChecked) onColor else offColor,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSwitcher() {
    var isChecked by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Switcher01(
            isChecked = isChecked,
            onCheckedChange = { isChecked = it },
            modifier = Modifier
                .size(SwitchMinWidth, SwitchMinHeight)
                .align(Alignment.Center)
        )
    }
}

class Switcher01 : Screen {

    @Composable
    override fun Content() {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            val navigator = LocalNavigator.currentOrThrow

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = {
                        navigator.pop()
                    },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back Press",
                    )
                }

                PreviewSwitcher()
            }

        }
    }
}