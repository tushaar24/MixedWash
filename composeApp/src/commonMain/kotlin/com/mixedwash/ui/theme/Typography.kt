package com.mixedwash.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mixedwash.composeapp.generated.resources.Poppins_Bold
import mixedwash.composeapp.generated.resources.Poppins_Medium
import mixedwash.composeapp.generated.resources.Poppins_Regular
import mixedwash.composeapp.generated.resources.Poppins_SemiBold
import mixedwash.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

data class Typography(
    val giantButton: TextStyle,
    val largeButton: TextStyle,
    val smallButton: TextStyle,
    val tinyButton: TextStyle,
    val mediumButton: TextStyle,
    val label: TextStyle,
    val caption1: TextStyle,
    val caption2: TextStyle,
    val caption3: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val body3: TextStyle,
    val body4: TextStyle,
    val subtitle1: TextStyle,
    val subtitle2: TextStyle,
    val subtitle3: TextStyle,
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
    val h5: TextStyle,
) {
    companion object {

        @Composable
        fun Poppins() = FontFamily(
            Font(resource = Res.font.Poppins_Regular, FontWeight.Normal),
            Font(resource = Res.font.Poppins_Medium, FontWeight.Medium),
            Font(resource = Res.font.Poppins_SemiBold, FontWeight.SemiBold),
            Font(resource = Res.font.Poppins_Bold, FontWeight.Bold),
        )

        @Composable
        fun defaultTypography() = Typography(
            giantButton = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W600,
                fontSize = 18.sp,
                lineHeight = 24.sp
            ),
            largeButton = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W600,
                fontSize = 16.sp,
                lineHeight = 20.sp
            ),
            smallButton = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W600,
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
            tinyButton = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                lineHeight = 12.sp
            ),
            mediumButton = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W600,
                fontSize = 14.sp,
                lineHeight = 16.sp
            ),
            label = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W500,
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
            caption1 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
            caption2 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W500,
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
            caption3 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W500,
                fontSize = 10.sp,
                lineHeight = 14.sp
            ),
            body1 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            body2 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                lineHeight = 24.sp
            ),
            body3 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                lineHeight = 20.sp
            ),
            body4 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            subtitle1 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W600,
                fontSize = 18.sp,
                lineHeight = 28.sp
            ),
            subtitle2 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W600,
                fontSize = 16.sp,
                lineHeight = 18.sp
            ),
            subtitle3 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W600,
                fontSize = 14.sp,
                lineHeight = 16.sp
            ),

            h1 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W600,
                fontSize = 48.sp,
                lineHeight = 58.sp
            ),
            h2 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W600,
                fontSize = 40.sp,
                lineHeight = 48.sp
            ),
            h3 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W600,
                fontSize = 32.sp,
                lineHeight = 38.sp
            ),
            h4 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W600,
                fontSize = 28.sp,
                lineHeight = 34.sp
            ),
            h5 = TextStyle(
                color = Color.Unspecified,
                fontFamily = Poppins(),
                fontWeight = FontWeight.W600,
                fontSize = 24.sp,
                lineHeight = 28.sp
            )
        )
    }
}

