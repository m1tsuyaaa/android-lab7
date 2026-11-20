package com.example.mycity.data

import com.example.mycity.R

private const val PHOTO =
    "https://images.unsplash.com/%s?auto=format&fit=crop&w=1200&q=80"

/**
 * Static source of truth for the app. The UI never creates places itself.
 */
object PlacesDataProvider {

    val categories: List<Category> = Category.values().toList()

    private val places: List<Place> = listOf(
        // Restaurants
        Place(
            id = 1,
            category = Category.Restaurants,
            nameRes = R.string.place_01_name,
            descriptionRes = R.string.place_01_description,
            addressRes = R.string.place_01_address,
            rating = 4.7f,
            imageUrl = PHOTO.format("photo-1517248135467-4c7edcad34c4"),
        ),
        Place(
            id = 2,
            category = Category.Restaurants,
            nameRes = R.string.place_02_name,
            descriptionRes = R.string.place_02_description,
            addressRes = R.string.place_02_address,
            rating = 4.6f,
            imageUrl = PHOTO.format("photo-1552566626-52f8b828add9"),
        ),
        Place(
            id = 3,
            category = Category.Restaurants,
            nameRes = R.string.place_03_name,
            descriptionRes = R.string.place_03_description,
            addressRes = R.string.place_03_address,
            rating = 4.9f,
            imageUrl = PHOTO.format("photo-1414235077428-338989a2e8c0"),
        ),
        Place(
            id = 4,
            category = Category.Restaurants,
            nameRes = R.string.place_04_name,
            descriptionRes = R.string.place_04_description,
            addressRes = R.string.place_04_address,
            rating = 4.4f,
            imageUrl = PHOTO.format("photo-1559339352-11d035aa65de"),
        ),
        // Parks
        Place(
            id = 5,
            category = Category.Parks,
            nameRes = R.string.place_05_name,
            descriptionRes = R.string.place_05_description,
            addressRes = R.string.place_05_address,
            rating = 4.8f,
            imageUrl = PHOTO.format("photo-1500530855697-b586d89ba3ee"),
        ),
        Place(
            id = 6,
            category = Category.Parks,
            nameRes = R.string.place_06_name,
            descriptionRes = R.string.place_06_description,
            addressRes = R.string.place_06_address,
            rating = 4.9f,
            imageUrl = PHOTO.format("photo-1472396961693-142e6e269027"),
        ),
        Place(
            id = 7,
            category = Category.Parks,
            nameRes = R.string.place_07_name,
            descriptionRes = R.string.place_07_description,
            addressRes = R.string.place_07_address,
            rating = 4.7f,
            imageUrl = PHOTO.format("photo-1441974231531-c6227db76b6e"),
        ),
        Place(
            id = 8,
            category = Category.Parks,
            nameRes = R.string.place_08_name,
            descriptionRes = R.string.place_08_description,
            addressRes = R.string.place_08_address,
            rating = 4.8f,
            imageUrl = PHOTO.format("photo-1511497584788-876760111969"),
        ),
        // Cafes
        Place(
            id = 9,
            category = Category.Cafes,
            nameRes = R.string.place_09_name,
            descriptionRes = R.string.place_09_description,
            addressRes = R.string.place_09_address,
            rating = 4.6f,
            imageUrl = PHOTO.format("photo-1442512595331-e89e73853f31"),
        ),
        Place(
            id = 10,
            category = Category.Cafes,
            nameRes = R.string.place_10_name,
            descriptionRes = R.string.place_10_description,
            addressRes = R.string.place_10_address,
            rating = 4.7f,
            imageUrl = PHOTO.format("photo-1501339847302-ac426a4a7cbb"),
        ),
        Place(
            id = 11,
            category = Category.Cafes,
            nameRes = R.string.place_11_name,
            descriptionRes = R.string.place_11_description,
            addressRes = R.string.place_11_address,
            rating = 4.5f,
            imageUrl = PHOTO.format("photo-1495474472287-4d71bcdd2085"),
        ),
        Place(
            id = 12,
            category = Category.Cafes,
            nameRes = R.string.place_12_name,
            descriptionRes = R.string.place_12_description,
            addressRes = R.string.place_12_address,
            rating = 4.6f,
            imageUrl = PHOTO.format("photo-1554118811-1e0d58224f24"),
        ),
        // Cinemas
        Place(
            id = 13,
            category = Category.Cinemas,
            nameRes = R.string.place_13_name,
            descriptionRes = R.string.place_13_description,
            addressRes = R.string.place_13_address,
            rating = 4.7f,
            imageUrl = PHOTO.format("photo-1489599849927-2ee91cede3ba"),
        ),
        Place(
            id = 14,
            category = Category.Cinemas,
            nameRes = R.string.place_14_name,
            descriptionRes = R.string.place_14_description,
            addressRes = R.string.place_14_address,
            rating = 4.8f,
            imageUrl = PHOTO.format("photo-1517604931442-7e0c8ed2963c"),
        ),
        Place(
            id = 15,
            category = Category.Cinemas,
            nameRes = R.string.place_15_name,
            descriptionRes = R.string.place_15_description,
            addressRes = R.string.place_15_address,
            rating = 4.6f,
            imageUrl = PHOTO.format("photo-1478720568477-152d9b164e26"),
        ),
        Place(
            id = 16,
            category = Category.Cinemas,
            nameRes = R.string.place_16_name,
            descriptionRes = R.string.place_16_description,
            addressRes = R.string.place_16_address,
            rating = 4.5f,
            imageUrl = PHOTO.format("photo-1440404653325-ab127d49abc1"),
        ),
        // Shopping malls
        Place(
            id = 17,
            category = Category.ShoppingMalls,
            nameRes = R.string.place_17_name,
            descriptionRes = R.string.place_17_description,
            addressRes = R.string.place_17_address,
            rating = 4.7f,
            imageUrl = PHOTO.format("photo-1519567241046-7f570eee3ce6"),
        ),
        Place(
            id = 18,
            category = Category.ShoppingMalls,
            nameRes = R.string.place_18_name,
            descriptionRes = R.string.place_18_description,
            addressRes = R.string.place_18_address,
            rating = 4.6f,
            imageUrl = PHOTO.format("photo-1567958451986-2de427a4a0be"),
        ),
        Place(
            id = 19,
            category = Category.ShoppingMalls,
            nameRes = R.string.place_19_name,
            descriptionRes = R.string.place_19_description,
            addressRes = R.string.place_19_address,
            rating = 4.7f,
            imageUrl = PHOTO.format("photo-1441986300917-64674bd600d8"),
        ),
        Place(
            id = 20,
            category = Category.ShoppingMalls,
            nameRes = R.string.place_20_name,
            descriptionRes = R.string.place_20_description,
            addressRes = R.string.place_20_address,
            rating = 4.5f,
            imageUrl = PHOTO.format("photo-1472851294608-062f824d29cc"),
        ),
    )

    fun getPlaces(category: Category): List<Place> = places.filter { it.category == category }

    fun getPlace(id: Int): Place? = places.firstOrNull { it.id == id }
}
