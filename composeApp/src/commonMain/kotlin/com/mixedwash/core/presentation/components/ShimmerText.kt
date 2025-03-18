package com.mixedwash.core.presentation.components

import BrandTheme
import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.ui.theme.Typography

@Composable
fun ShimmerText(
    text: String,
    modifier: Modifier = Modifier,
    isShimmering: Boolean,
    style: TextStyle = LocalTextStyle.current,
    color: Color = LocalTextStyle.current.color,
    shimmerColor: Color = Color.White,
    durationMillis: Int = 2000,
    delayMillis: Int = 100,
) {
    val animationSpec: DurationBasedAnimationSpec<Float> =
        tween(durationMillis, delayMillis, EaseInOut)
    // Shimmer animation setup
    val infiniteTransition = rememberInfiniteTransition(label = "ShimmerTransition")
    val shimmerProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animationSpec),
        label = "Shimmer Text Progress"
    )

    // Shimmer brush
    val shimmerBrush = remember(shimmerProgress) {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val initialXOffset = -size.width
                val totalSweepDistance = size.width * 2
                val currentPosition = initialXOffset + totalSweepDistance * shimmerProgress

                return LinearGradientShader(
                    colors = listOf(color, shimmerColor, color),
                    from = Offset(currentPosition, 0f),
                    to = Offset(currentPosition + size.width, 0f)
                )
            }
        }
    }
    Text(
        text = text,
        modifier = modifier,
        style = if (isShimmering) {
            style.copy(brush = shimmerBrush) // Shimmer effect applied
        } else {
            style // No shimmer
        }
    )
}

//@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true, showSystemUi = true)
@Composable
private fun ShimmeringTextExample() {
    // Toggle shimmer on/off
    var isShimmering by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        ShimmerText(
            text = "Test Text",
            isShimmering = isShimmering,
            style = TextStyle(fontSize = 28.sp, fontFamily = Typography.Poppins()),
            shimmerColor = Color.White
        )
        Button(onClick = { isShimmering = !isShimmering }) {
            Text("Toggle Shimmer")
        }
    }
}