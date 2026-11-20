/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.reply.ui.utils

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

/**
 * Different type of navigation supported by app depending on size and state.
 */
enum class ReplyNavigationType {
    BOTTOM_NAVIGATION, NAVIGATION_RAIL, PERMANENT_NAVIGATION_DRAWER
}

/**
 * Content shown depending on size and state of device.
 */
enum class ReplyContentType {
    LIST_ONLY, LIST_AND_DETAIL
}

/**
 * Returns the navigation element that fits the given window width size class:
 * bottom navigation for compact screens, navigation rail for medium screens and
 * a permanent navigation drawer for expanded screens.
 */
fun ReplyNavigationType(widthSizeClass: WindowWidthSizeClass): ReplyNavigationType {
    return when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> ReplyNavigationType.BOTTOM_NAVIGATION
        WindowWidthSizeClass.Medium -> ReplyNavigationType.NAVIGATION_RAIL
        WindowWidthSizeClass.Expanded -> ReplyNavigationType.PERMANENT_NAVIGATION_DRAWER
        else -> ReplyNavigationType.BOTTOM_NAVIGATION
    }
}

/**
 * Returns the content layout that fits the given window width size class:
 * a list-detail layout for expanded screens and a list-only layout otherwise.
 */
fun ReplyContentType(widthSizeClass: WindowWidthSizeClass): ReplyContentType {
    return when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> ReplyContentType.LIST_ONLY
        WindowWidthSizeClass.Medium -> ReplyContentType.LIST_ONLY
        WindowWidthSizeClass.Expanded -> ReplyContentType.LIST_AND_DETAIL
        else -> ReplyContentType.LIST_ONLY
    }
}
