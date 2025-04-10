package com.mixedwash.ui.theme

import BrandTheme
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class ColorScheme(
    val primary: MaterialColors,
    val secondary: MaterialColors,
    val gray: MaterialColors,
    val body: Color,
    val background: Color,
    private val outlinedTextFieldColors: @Composable () -> TextFieldColors,
    private val outlinedButtonColors: @Composable () -> ButtonColors,
    private val buttonColors: @Composable () -> ButtonColors,
    private val iconButtonColors: @Composable () -> ButtonColors,
    private val textFieldColors: @Composable () -> TextFieldColors
) {
    private var outlinedTextFieldColorsCache: TextFieldColors? = null
    private var outlinedButtonColorsCache: ButtonColors? = null
    private var buttonColorsCache: ButtonColors? = null
    private var iconButtonColorsCache: ButtonColors? = null
    private var textFieldColorsCache: TextFieldColors? = null

    @Composable
    fun outlinedTextFieldColors() = outlinedTextFieldColorsCache ?: outlinedTextFieldColors.invoke()
        .also { outlinedTextFieldColorsCache = it }

    @Composable
    fun outlinedButtonColors() = outlinedButtonColorsCache ?: outlinedButtonColors.invoke()
        .also { outlinedButtonColorsCache = it }

    @Composable
    fun buttonColors() = buttonColorsCache ?: buttonColors.invoke()
        .also { buttonColorsCache = it }

    @Composable
    fun iconButtonColors() = iconButtonColorsCache ?: iconButtonColors.invoke()
        .also { iconButtonColorsCache = it }

    @Composable
    fun textFieldColors() = textFieldColorsCache ?: textFieldColors.invoke()
        .also { textFieldColorsCache = it }

    companion object {
        val lightScheme = ColorScheme(
            primary = MaterialColors(
                c50 = Blue50,
                c100 = Blue100,
                c200 = Blue200,
                c300 = Blue300,
                c400 = Blue400,
                c500 = Blue500,
                c600 = Blue600,
                c700 = Blue700,
                c800 = Blue800,
                c900 = Blue900
            ),
            secondary = MaterialColors(
                c50 = Yellow50,
                c100 = Yellow100,
                c200 = Yellow200,
                c300 = Yellow300,
                c400 = Yellow400,
                c500 = Yellow500,
                c600 = Yellow600,
                c700 = Yellow700,
                c800 = Yellow800,
                c900 = Yellow900
            ),
            gray = MaterialColors(
                c50 = Gray50,
                c100 = Gray100,
                c200 = Gray200,
                c300 = Gray300,
                c400 = Gray400,
                c500 = Gray500,
                c600 = Gray600,
                c700 = Gray700,
                c800 = Gray800,
                c900 = Gray900
            ),
            background = Gray50,
            body = Gray900,
            outlinedTextFieldColors = {
                OutlinedTextFieldDefaults.colors().copy(
                    focusedIndicatorColor = BrandTheme.colors.primary.normalDark,
                    focusedTextColor = BrandTheme.colors.gray.darker,
                    focusedLabelColor = BrandTheme.colors.primary.normalDark,
                    unfocusedLabelColor = BrandTheme.colors.gray.c600,
                    unfocusedIndicatorColor = BrandTheme.colors.gray.dark,
                    unfocusedPlaceholderColor = BrandTheme.colors.gray.normalDark,
                    focusedPlaceholderColor = BrandTheme.colors.gray.normalDark,
                    cursorColor = BrandTheme.colors.gray.darker,
                    disabledTextColor = BrandTheme.colors.gray.normalDark,
                    disabledIndicatorColor = BrandTheme.colors.gray.normal,
                    disabledLabelColor = BrandTheme.colors.gray.normalDark,
                    disabledPlaceholderColor = BrandTheme.colors.gray.normal,
                )
            },
            outlinedButtonColors = {
                ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = BrandTheme.colors.gray.darker,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = BrandTheme.colors.gray.normalDark
                )
            },
            buttonColors = {
                ButtonColors(
                    containerColor = BrandTheme.colors.primary.normalDark,
                    contentColor = BrandTheme.colors.gray.lighter,
                    disabledContainerColor = BrandTheme.colors.gray.normal,
                    disabledContentColor = BrandTheme.colors.gray.dark
                )
            },
            iconButtonColors = {
                ButtonColors(
                    containerColor = BrandTheme.colors.secondary.light,
                    contentColor = BrandTheme.colors.gray.dark,
                    disabledContainerColor = BrandTheme.colors.gray.lighter,
                    disabledContentColor = BrandTheme.colors.gray.normalDark
                )
            },
            textFieldColors = {
                TextFieldDefaults.colors().copy(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledTextColor = BrandTheme.colors.gray.normalDark,
                    disabledLabelColor = BrandTheme.colors.gray.normalDark,
                    errorIndicatorColor = Color.Transparent,
                    unfocusedPrefixColor = BrandTheme.colors.gray.darker,
                    unfocusedLabelColor = BrandTheme.colors.gray.dark,
                    focusedPrefixColor = BrandTheme.colors.gray.darker,
                    focusedLabelColor = BrandTheme.colors.primary.dark,
                    focusedPlaceholderColor = BrandTheme.colors.gray.normal,
                    unfocusedPlaceholderColor = BrandTheme.colors.gray.normal,
                    unfocusedContainerColor = BrandTheme.colors.gray.light,
                    focusedContainerColor = BrandTheme.colors.gray.light,
                    disabledContainerColor = BrandTheme.colors.gray.light,
                    errorContainerColor = BrandTheme.colors.gray.light,
                    errorPlaceholderColor = RedDark,
                    errorPrefixColor = RedDark,
                    errorSupportingTextColor = RedDark,
                    errorTextColor = RedDark,
                    focusedTextColor = BrandTheme.colors.gray.darker
                )
            }
        )
    }
}

