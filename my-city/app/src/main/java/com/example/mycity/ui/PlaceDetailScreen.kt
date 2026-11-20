package com.example.mycity.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.example.mycity.data.PlacesDataProvider
import com.example.mycity.ui.components.PlaceDetails
import com.example.mycity.ui.components.PlaceImage
import com.example.mycity.ui.theme.MyCityTheme

/** Full-screen detail destination used on compact screens. */
@Composable
fun PlaceDetailScreen(
    placeId: Int,
    uiState: MyCityUiState,
    onPlaceSelected: (Int) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(placeId) {
        onPlaceSelected(placeId)
    }

    val place = uiState.selectedPlace?.takeIf { it.id == placeId }
    if (place == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.place_not_found),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        return
    }

    Column(modifier = modifier.fillMaxSize()) {
        PlaceImage(
            place = place,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
        PlaceDetails(
            place = place,
            isFavorite = place.id in uiState.favoritePlaceIds,
            onToggleFavorite = onToggleFavorite,
            modifier = Modifier.weight(1f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaceDetailScreenPreview() {
    val place = PlacesDataProvider.getPlace(6)!!
    MyCityTheme {
        PlaceDetailScreen(
            placeId = place.id,
            uiState = MyCityUiState(
                selectedCategory = Category.Parks,
                places = PlacesDataProvider.getPlaces(Category.Parks),
                selectedPlace = place,
            ),
            onPlaceSelected = {},
            onToggleFavorite = {},
        )
    }
}
