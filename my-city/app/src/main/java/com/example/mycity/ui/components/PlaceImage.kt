package com.example.mycity.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.mycity.R
import com.example.mycity.data.Place

/**
 * Photo of a place. Remote images are loaded with Coil; while an image is
 * loading (or if the network is unavailable) a category emoji on a gradient
 * background is shown instead, so there is never an empty or broken picture.
 */
@Composable
fun PlaceImage(
    place: Place,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageRequest = ImageRequest.Builder(context)
        .data(place.imageUrl)
        .crossfade(true)
        .build()
    val transparentPainter = ColorPainter(Color.Transparent)
    val gradient = androidx.compose.ui.graphics.Brush.linearGradient(
        listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.secondaryContainer,
        )
    )

    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(gradient),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = place.category.emoji,
            fontSize = 56.sp,
        )
        AsyncImage(
            model = imageRequest,
            contentDescription = stringResource(
                R.string.place_image,
                stringResource(place.nameRes),
            ),
            placeholder = transparentPainter,
            error = transparentPainter,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
