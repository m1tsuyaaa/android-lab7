package com.example.mycity.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mycity.R
import com.example.mycity.data.Category
import com.example.mycity.data.MyCityUiState
import com.example.mycity.data.Place
import com.example.mycity.data.PlacesDataProvider
import com.example.mycity.ui.components.PlaceDetails
import com.example.mycity.ui.components.PlaceList
import com.example.mycity.ui.theme.MyCityTheme

/**
 * Category screen: a plain list on compact screens and a list/detail two-pane
 * layout on medium and expanded screens.
 */
@Composable
fun CategoryScreen(
    category: Category,
    uiState: MyCityUiState,
    onCategorySelected: (Category) -> Unit,
    onPlaceClick: (Place) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    modifier: Modifier = Modifier,
    isTwoPane: Boolean = false,
) {
    LaunchedEffect(category) {
        onCategorySelected(category)
    }

    if (!isTwoPane) {
        PlaceList(
            places = uiState.places,
            selectedPlaceId = uiState.selectedPlace?.id,
            onPlaceClick = onPlaceClick,
            modifier = modifier.fillMaxSize(),
        )
        return
    }

    Row(modifier = modifier.fillMaxSize()) {
        PlaceList(
            places = uiState.places,
            selectedPlaceId = uiState.selectedPlace?.id,
            onPlaceClick = onPlaceClick,
            modifier = Modifier.weight(0.4f),
        )
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.outlineVariant),
        )
        val selectedPlace = uiState.selectedPlace
        if (selectedPlace != null) {
            PlaceDetails(
                place = selectedPlace,
                isFavorite = selectedPlace.id in uiState.favoritePlaceIds,
                onToggleFavorite = onToggleFavorite,
                modifier = Modifier.weight(0.6f),
            )
        } else {
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.select_place_hint),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryScreenCompactPreview() {
    val places = PlacesDataProvider.getPlaces(Category.Restaurants)
    MyCityTheme {
        CategoryScreen(
            category = Category.Restaurants,
            uiState = MyCityUiState(
                selectedCategory = Category.Restaurants,
                places = places,
            ),
            onCategorySelected = {},
            onPlaceClick = {},
            onToggleFavorite = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 900, heightDp = 700)
@Composable
private fun CategoryScreenTwoPanePreview() {
    val places = PlacesDataProvider.getPlaces(Category.Parks)
    MyCityTheme {
        CategoryScreen(
            category = Category.Parks,
            uiState = MyCityUiState(
                selectedCategory = Category.Parks,
                places = places,
                selectedPlace = places.firstOrNull(),
            ),
            onCategorySelected = {},
            onPlaceClick = {},
            onToggleFavorite = {},
            isTwoPane = true,
        )
    }
}
