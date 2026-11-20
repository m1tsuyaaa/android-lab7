package com.example.mycity.data

import androidx.annotation.StringRes
import com.example.mycity.R

/**
 * Categories of city places. Titles are stored as string resources so that the
 * data layer stays independent from the UI while remaining localizable.
 */
enum class Category(
    @StringRes val titleRes: Int,
    val emoji: String,
) {
    Restaurants(R.string.category_restaurants, "\uD83C\uDF7D\uFE0F"),
    Parks(R.string.category_parks, "\uD83C\uDF33"),
    Cafes(R.string.category_cafes, "\u2615"),
    Cinemas(R.string.category_cinemas, "\uD83C\uDFAC"),
    ShoppingMalls(R.string.category_shopping, "\uD83D\uDECD\uFE0F"),
}
