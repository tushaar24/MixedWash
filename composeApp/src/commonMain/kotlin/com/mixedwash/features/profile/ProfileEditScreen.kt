package com.mixedwash.features.profile

import BrandTheme
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.components.DialogPopup
import com.mixedwash.core.presentation.components.DialogPopupData
import com.mixedwash.core.presentation.components.HeadingAlign
import com.mixedwash.core.presentation.components.HeadingSize
import com.mixedwash.core.presentation.components.clearFocusOnKeyboardDismiss
import com.mixedwash.core.presentation.models.SnackbarHandler
import com.mixedwash.core.presentation.models.SnackbarPayload
import com.mixedwash.core.presentation.util.ObserveAsEvents
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.ui.theme.components.PrimaryButton
import com.mixedwash.ui.theme.headerContentSpacing
import com.mixedwash.ui.theme.screenHorizontalPadding
import kotlinx.coroutines.flow.Flow
import mixedwash.composeapp.generated.resources.Res
import mixedwash.composeapp.generated.resources.ic_badge
import mixedwash.composeapp.generated.resources.ic_email
import mixedwash.composeapp.generated.resources.ic_image
import mixedwash.composeapp.generated.resources.ic_phone
import org.jetbrains.compose.resources.vectorResource

data class ProfileEditScreenState(
    val imageUrl: String?,
    val saveEnabled: Boolean = true,
    val fields: List<_root_ide_package_.com.mixedwash.core.presentation.models.FormField>,
    val isLoading: Boolean = false
) {
    companion object {
        fun initialState() = ProfileEditScreenState(
            imageUrl = null,
            saveEnabled = false,
            fields = emptyList()
        )
    }
}

sealed interface ProfileEditScreenEvent {
    data object OnBackClicked : ProfileEditScreenEvent
    data class OnChangePicture(val url: String) : ProfileEditScreenEvent
    data object OnSave : ProfileEditScreenEvent
    data class OnFieldUpdate(val fieldId: _root_ide_package_.com.mixedwash.core.presentation.models.FieldID, val value: String) :
        ProfileEditScreenEvent
}

sealed interface ProfileEditScreenUiEvent {
    data object OnNavigateBack : ProfileEditScreenUiEvent
    data class ShowPopup(val dialogPopupData: DialogPopupData) : ProfileEditScreenUiEvent
    data object ClosePopup : ProfileEditScreenUiEvent
    data class ShowSnackbar(val snackbarPayload: SnackbarPayload) : ProfileEditScreenUiEvent
}

@Composable
fun ProfileEditScreen(
    modifier: Modifier = Modifier,
    state: ProfileEditScreenState,
    uiEventsFlow: Flow<ProfileEditScreenUiEvent>,
    snackbarHandler: SnackbarHandler,
    onEvent: (ProfileEditScreenEvent) -> Unit,
    navigateBack: () -> Unit
) {

    var dialogPopupData by remember { mutableStateOf<DialogPopupData?>(null) }

    dialogPopupData?.let { DialogPopup(data = it, onDismissRequest = { dialogPopupData = null }) }

    ObserveAsEvents(uiEventsFlow) { event ->
        when (event) {
            ProfileEditScreenUiEvent.OnNavigateBack -> {
                navigateBack()
            }

            is ProfileEditScreenUiEvent.ShowPopup -> {
                dialogPopupData = event.dialogPopupData
            }

            ProfileEditScreenUiEvent.ClosePopup -> {
                dialogPopupData = null
            }

            is ProfileEditScreenUiEvent.ShowSnackbar -> {
                snackbarHandler(event.snackbarPayload)
            }
        }
    }

    WindowInsetsContainer {
        Column(modifier) {
            DefaultHeader(
                title = "Edit Your Profile",
                headingSize = HeadingSize.Subtitle1,
                headingAlign = HeadingAlign.Start,
                navigationButton = {
                    HeaderIconButton(
                        imageVector = Icons.Rounded.KeyboardArrowLeft,
                        onClick = { onEvent(ProfileEditScreenEvent.OnBackClicked) }
                    )
                }
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = screenHorizontalPadding)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Spacer(modifier = Modifier.height(headerContentSpacing))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (state.imageUrl != null) {
                        AsyncImage(
                            model = state.imageUrl, contentDescription = "Profile Picture",
                            modifier = Modifier
                                .clip(shape = BrandTheme.shapes.circle)
                                .size(72.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            modifier = Modifier
                                .clip(shape = BrandTheme.shapes.circle)
                                .background(BrandTheme.colors.gray.c300)
                                .size(72.dp)
                                .padding(12.dp),
                            contentDescription = "Profile Picture",
                            tint = BrandTheme.colors.gray.normalDark
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Profile Picture",
                            style = BrandTheme.typography.subtitle2.copy(fontWeight = FontWeight.W500)
                        )
                        Row(
                            modifier = Modifier
                                .clip(shape = BrandTheme.shapes.button)
                                .border(
                                    width = 0.5.dp,
                                    color = BrandTheme.colors.gray.c500,
                                    shape = BrandTheme.shapes.button
                                ).clickable(enabled = !state.isLoading) {
                                    TODO("Implement return image url from activity result cross platform")
                                    onEvent(ProfileEditScreenEvent.OnChangePicture(""))
                                }.padding(vertical = 8.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = vectorResource(Res.drawable.ic_image),
                                tint = BrandTheme.colors.gray.darker,
                                contentDescription = null
                            )
                            Text(
                                text = "Change",
                                style = BrandTheme.typography.smallButton
                            )
                        }
                    }

                }

                Column(
                    modifier = Modifier.animateContentSize(),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    state.fields.forEach {
                        val field = it.asFieldState
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clearFocusOnKeyboardDismiss(),
                            value = field.value,
                            onValueChange = field.onValueChange,
                            prefix = {
                                val resource = when (it.id) {
                                    _root_ide_package_.com.mixedwash.core.presentation.models.FieldID.PHONE -> Res.drawable.ic_phone
                                    _root_ide_package_.com.mixedwash.core.presentation.models.FieldID.EMAIL -> Res.drawable.ic_email
                                    else -> Res.drawable.ic_badge
                                }
                                Row {
                                    Icon(
                                        modifier = Modifier.size(18.dp),
                                        imageVector = vectorResource(resource),
                                        contentDescription = "Prefix Icon",
                                        tint = BrandTheme.colors.gray.c500
                                    )
                                    Spacer(Modifier.width(12.dp))
                                }
                            },
                            placeholder = {
                                field.placeholder?.let {
                                    Text(
                                        text = it,
                                        style = BrandTheme.typography.body4.copy(color = Color.Unspecified)
                                    )
                                }
                            },
                            singleLine = field.singleLine,
                            colors = BrandTheme.colors.textFieldColors().copy(),
                            readOnly = field.readOnly,
                            shape = BrandTheme.shapes.textField,
                            supportingText = { field.supportingText?.let { Text(it) } },
                            keyboardOptions = field.keyboardOptions,
                            enabled = field.enabled && !state.isLoading,
                            isError = field.isError,
                            textStyle = BrandTheme.typography.body4.copy(
                                fontSize = 15.sp,
                                color = BrandTheme.colors.gray.darker
                            )
                        )

                    }
                }
                Row(
                    modifier = Modifier.padding(top = 18.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    PrimaryButton(
                        text = "Save Changes",
                        onClick = { onEvent(ProfileEditScreenEvent.OnSave) },
                        enabled = state.fields.all { !it.asFieldState.isError } && !state.isLoading)
                }

            }
        }
    }
}