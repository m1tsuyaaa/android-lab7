package com.example.mycity.ui

import androidx.lifecycle.ViewModel
import com.example.mycity.data.Category
import com.example.mycity.data.MyCityUiState
import com.example.mycity.data.PlacesDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Holds the single [MyCityUiState] for the whole app.
 * The UI only renders state and sends events through the public methods below
 * (unidirectional data flow).
 */
class MyCityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MyCityUiState())
    val uiState: StateFlow<MyCityUiState> = _uiState.asStateFlow()

    fun onCategorySelected(category: Category) {
        if (_uiState.value.selectedCategory == category) return
        val places = PlacesDataProvider.getPlaces(category)
        _uiState.update {
            it.copy(
                selectedCategory = category,
                places = places,
                selectedPlace = places.firstOrNull(),
            )
        }
    }

    fun onPlaceSelected(placeId: Int) {
        val place = PlacesDataProvider.getPlace(placeId) ?: return
        _uiState.update { state ->
            if (state.selectedPlace?.id == place.id) state else state.copy(selectedPlace = place)
        }
    }

    fun onToggleFavorite(placeId: Int) {
        _uiState.update { state ->
            val favorites = if (placeId in state.favoritePlaceIds) {
                state.favoritePlaceIds - placeId
            } else {
                state.favoritePlaceIds + placeId
            }
            state.copy(favoritePlaceIds = favorites)
        }
    }
}
