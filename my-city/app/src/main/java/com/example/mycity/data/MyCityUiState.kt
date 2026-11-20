package com.example.mycity.data

/**
 * Immutable UI state exposed by [com.example.mycity.ui.MyCityViewModel].
 */
data class MyCityUiState(
    val categories: List<Category> = PlacesDataProvider.categories,
    val places: List<Place> = emptyList(),
    val selectedCategory: Category? = null,
    val selectedPlace: Place? = null,
    val favoritePlaceIds: Set<Int> = emptySet(),
)
