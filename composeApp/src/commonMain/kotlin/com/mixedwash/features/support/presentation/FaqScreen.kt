package com.mixedwash.features.support.presentation

import BrandTheme
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mixedwash.WindowInsetsContainer
import com.mixedwash.core.presentation.components.noRippleClickable
import com.mixedwash.features.support.domain.model.FaqItemDTO

@Composable
fun FaqScreen(
    state: FaqScreenState,
    onEvent: (FaqScreenEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    WindowInsetsContainer {
        Box(modifier = modifier.fillMaxSize().padding(16.dp)) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "How can we help you?",
                    style = BrandTheme.typography.h5.copy(fontSize = 20.sp)
                )

                SearchBarWithIcon(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.searchString,
                    onValueChange = { onEvent(FaqScreenEvent.OnSearchStringValueChanged(it)) },
                    leadingIcon = Icons.Default.Search,
                    placeholder = "Search for FAQs",
                )

                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
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

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                                        modifier = Modifier.clip(CircleShape)
                                            .background(BrandTheme.colors.gray.light)
                                            .clickable { onEvent(FaqScreenEvent.OnFaqTagClicked(it.displayTag)) }
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(12.dp),
                                            text = it.displayTag,
                                            style = BrandTheme.typography.label.copy(
                                                fontWeight = FontWeight.W600, lineHeight = 20.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(Modifier.height(4.dp))
                        }

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

                    items(state.faqItems) {
                        FaqItemCard(
                            modifier = Modifier.padding(vertical = 8.dp),
                            item = it
                        )
                    }

                    // to avoid the content being hidden by the bottom button
                    item {
                        Spacer(Modifier.height(64.dp))
                    }
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
                onClick = { onEvent(FaqScreenEvent.OnCallButtonClicked) },
                shape = BrandTheme.shapes.button,
                colors = ButtonDefaults.buttonColors(containerColor = BrandTheme.colors.gray.darker)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call"
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "Call Us",
                        style = BrandTheme.typography.mediumButton
                    )
                }
            }
        }
    }
}

@Composable
fun FaqItemCard(item: FaqItemDTO, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.clip(BrandTheme.shapes.card).background(BrandTheme.colors.gray.light),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().noRippleClickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.question,
                    style = BrandTheme.typography.body2.copy(fontWeight = FontWeight.W500),
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    modifier = Modifier.padding(start = 16.dp),
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand"
                )
            }

            if (expanded) {
                HorizontalDivider(Modifier.padding(vertical = 8.dp))

                Text(
                    text = item.answer,
                    style = BrandTheme.typography.body3.copy(color = BrandTheme.colors.gray.c600),
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
        modifier = modifier.clip(CircleShape)
            .widthIn(min = 80.dp)
            .background(if (selected) BrandTheme.colors.gray.darker else BrandTheme.colors.background)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            color = if (selected) BrandTheme.colors.background else BrandTheme.colors.gray.dark,
            text = text,
            style = BrandTheme.typography.mediumButton.copy(fontSize = 12.sp)
        )
    }
}

@Composable
fun SearchBarWithIcon(
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector,
    placeholder: String,
    modifier: Modifier = Modifier
) {

    TextField(
        modifier = modifier
            .border(1.dp, BrandTheme.colors.gray.normal, BrandTheme.shapes.textField)
            .clip(BrandTheme.shapes.textField),
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                color = BrandTheme.colors.gray.normalDark
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = "Search Icon",
                tint = BrandTheme.colors.gray.dark
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
                    modifier = Modifier.size(16.dp).clickable { onValueChange("") },
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear Icon",
                    tint = BrandTheme.colors.gray.dark,
                )
            }
        },
        shape = BrandTheme.shapes.textField,
    )
}