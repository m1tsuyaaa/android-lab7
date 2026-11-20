package com.example.mycity.ui

import androidx.annotation.StringRes
import com.example.mycity.R
import com.example.mycity.data.Category

/**
 * Navigation routes of the app. Every route has a resource title that is used
 * by the top app bar, so the title is always in sync with the current screen.
 */
enum class MyCityScreen(@StringRes val title: Int) {
    Home(R.string.title_home),
    Category(R.string.title_category),
    Place(R.string.title_place);

    companion object {
        const val CATEGORY_ARG = "category"
        const val PLACE_ARG = "placeId"
    }
}

fun categoryRoute(category: Category): String =
    "${MyCityScreen.Category.name}/${category.name}"

fun placeRoute(placeId: Int): String =
    "${MyCityScreen.Place.name}/$placeId"
