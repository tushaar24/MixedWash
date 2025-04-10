package com.mixedwash.features.support.presentation

import BrandTheme
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.presentation.components.ElevatedBox
import com.mixedwash.core.presentation.components.DefaultHeader
import com.mixedwash.core.presentation.components.HeadingAlign
import com.mixedwash.core.presentation.components.HeadingSize
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.features.support.domain.model.FaqItemDTO
import com.mixedwash.ui.theme.Typography
import com.mixedwash.ui.theme.components.HeaderIconButton
import com.mixedwash.ui.theme.dividerBlack

@Composable
fun FaqScreen(
    state: FaqScreenState,
    onEvent: (FaqScreenEvent) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val isScrolled by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 }
    }
    val footerElevation by animateDpAsState(
        if (isScrolled) 0.dp else 4.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    WindowInsetsContainer {
        Column {
            DefaultHeader(
                title = "Help Center",
                headingSize = HeadingSize.Subtitle1,
                headingAlign = HeadingAlign.Start,
                navigationButton = {
                    HeaderIconButton(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        onClick = { navController.navigateUp() }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        value = state.searchString,
                        onValueChange = { onEvent(FaqScreenEvent.OnSearchStringValueChanged(it)) },
                        placeholder = "Search for FAQs",
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        for (label in state.faqCategories) {
                            LabelChip(
                                selected = state.currentCategory == label,
                                onClick = { onEvent(FaqScreenEvent.OnFaqCategoryChipClicked(label)) },
                                text = label.name
                            )
                        }
                    }

                    Box {
                        LazyColumn(
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            item { Spacer(Modifier.height(2.dp)) }

                            if (state.searchString.isEmpty()) {
                                item {
                                    Text(
                                        text = "Most Searched Questions",
                                        style = BrandTheme.typography.subtitle3.copy(
                                            fontWeight = FontWeight.W500,
                                            fontSize = 13.sp,
                                            color = BrandTheme.colors.gray.normalDark
                                        ),
                                    )
                                }

                                item {
                                    LazyRow(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(state.faqTags) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(BrandTheme.colors.gray.light)
                                                    .clickable {
                                                        onEvent(
                                                            FaqScreenEvent.OnFaqTagClicked(
                                                                it.displayTag
                                                            )
                                                        )
                                                    }
                                            ) {
                                                Text(
                                                    modifier = Modifier.padding(12.dp),
                                                    text = it.displayTag,
                                                    style = BrandTheme.typography.label.copy(
                                                        fontWeight = FontWeight.W600,
                                                        lineHeight = 20.sp
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }

                                item { Spacer(Modifier.height(2.dp)) }

                                item {
                                    Text(
                                        text = "All Questions",
                                        style = BrandTheme.typography.subtitle3.copy(
                                            fontWeight = FontWeight.W500,
                                            fontSize = 13.sp,
                                            color = BrandTheme.colors.gray.normalDark
                                        ),
                                    )
                                }
                            } else {
                                item {
                                    Text(
                                        text = "Search Results for \"${state.searchString}\"",
                                        style = BrandTheme.typography.subtitle3.copy(
                                            fontWeight = FontWeight.W500,
                                            color = BrandTheme.colors.gray.normalDark
                                        ),
                                    )
                                }
                            }

                            items(state.faqData.faqItemDtos) {
                                FaqItemCard(
                                    modifier = Modifier.padding(bottom = 8.dp),
                                    item = it
                                )
                            }

                            // Avoid the content being hidden by the bottom button
                            item { Spacer(Modifier.height(64.dp)) }
                        }

                        if (isScrolled) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .align(Alignment.TopCenter)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color.DarkGray.copy(alpha = 0.05f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                            )
                        }
                    }
                }

                ElevatedBox(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    elevation = footerElevation
                ) {
                    Box(
                        modifier = Modifier.height(52.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(BrandTheme.colors.gray.darker)
                            .noRippleClickable {
                                onEvent(FaqScreenEvent.OnCallButtonClicked(state.faqData.phoneNumber))
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Contact Support",
                            lineHeight = 18.sp,
                            fontSize = 14.sp,
                            color = BrandTheme.colors.gray.light,
                            fontWeight = FontWeight.Medium,
                            style = BrandTheme.typography.mediumButton,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun FaqItemCard(item: FaqItemDTO, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(BrandTheme.shapes.card)
            .background(BrandTheme.colors.gray.light)
            .animateContentSize() // This automatically animates any size changes in the card.
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.question,
                    style = BrandTheme.typography.body2.copy(fontWeight = FontWeight.W500),
                    color = BrandTheme.colors.gray.dark,
                    modifier = Modifier.weight(1f),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    modifier = Modifier.size( 16.dp),
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = BrandTheme.colors.gray.normalDark
                )
            }

            if (expanded) {
                HorizontalDivider(
                    Modifier.padding(vertical = 8.dp),
                    color = dividerBlack
                )

                Text(
                    text = item.answer,
                    style = BrandTheme.typography.body3.copy(color = BrandTheme.colors.gray.c600)
                )
            }
        }
    }
}

@Composable
fun LabelChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clip(RoundedCornerShape(12.dp))
            .widthIn(min = 80.dp)
            .background(if (selected) BrandTheme.colors.gray.darker else BrandTheme.colors.background)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp, horizontal = 8.dp),
            color = if (selected) BrandTheme.colors.background else BrandTheme.colors.gray.c700,
            text = text,
            style = BrandTheme.typography.mediumButton.copy(fontSize = 12.sp)
        )
    }
}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {

    TextField(
        modifier = modifier
            .border(1.dp, BrandTheme.colors.gray.c300, BrandTheme.shapes.textField)
            .clip(BrandTheme.shapes.textField)
            .height(52.dp),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                color = BrandTheme.colors.gray.normalDark,
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = BrandTheme.colors.gray.light,
            unfocusedContainerColor = BrandTheme.colors.gray.light,
        ),
        trailingIcon = {
            if (value.isNotEmpty()) {
                Icon(
                    modifier = Modifier.size(20.dp).noRippleClickable { onValueChange("") },
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Icon",
                    tint = BrandTheme.colors.gray.dark,
                )
            }
        },
        shape = BrandTheme.shapes.textField,
        textStyle = TextStyle(fontSize = 12.sp, fontFamily = Typography.Poppins())
    )
}