package com.example.mycity.data

import androidx.annotation.StringRes

/**
 * A single recommendation. All textual information is kept in resources.
 */
data class Place(
    val id: Int,
    val category: Category,
    @StringRes val nameRes: Int,
    @StringRes val descriptionRes: Int,
    @StringRes val addressRes: Int,
    val rating: Float,
    val imageUrl: String,
)
