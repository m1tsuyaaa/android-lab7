package com.example.mycity.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mycity.R
import com.example.mycity.data.Category

private data class MyCityNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val category: Category? = null,
)

/**
 * Root composable: single NavHost, dynamic top app bar and navigation UI that
 * adapts to the available width (bottom bar on compact, rail on larger screens).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCityApp(
    windowSizeClass: WindowSizeClass,
    viewModel: MyCityViewModel = viewModel(),
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val isTwoPane = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    val navItems = listOf(
        MyCityNavItem(MyCityScreen.Home.name, stringResource(R.string.nav_home), Icons.Filled.Home),
        MyCityNavItem(
            categoryRoute(Category.Restaurants),
            stringResource(Category.Restaurants.titleRes),
            Icons.Filled.Restaurant,
            Category.Restaurants,
        ),
        MyCityNavItem(
            categoryRoute(Category.Parks),
            stringResource(Category.Parks.titleRes),
            Icons.Filled.Park,
            Category.Parks,
        ),
        MyCityNavItem(
            categoryRoute(Category.Cafes),
            stringResource(Category.Cafes.titleRes),
            Icons.Filled.LocalCafe,
            Category.Cafes,
        ),
        MyCityNavItem(
            categoryRoute(Category.Cinemas),
            stringResource(Category.Cinemas.titleRes),
            Icons.Filled.Movie,
            Category.Cinemas,
        ),
        MyCityNavItem(
            categoryRoute(Category.ShoppingMalls),
            stringResource(Category.ShoppingMalls.titleRes),
            Icons.Filled.ShoppingCart,
            Category.ShoppingMalls,
        ),
    )

    val currentCategoryName = backStackEntry?.arguments?.getString(MyCityScreen.CATEGORY_ARG)
    val isSelected: (MyCityNavItem) -> Boolean = { item ->
        if (item.category != null) {
            item.category.name == currentCategoryName &&
                currentRoute?.startsWith(MyCityScreen.Category.name) == true
        } else {
            currentRoute == MyCityScreen.Home.name
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = screenTitle(backStackEntry)) },
                navigationIcon = {
                    if (navController.previousBackStackEntry != null) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
        bottomBar = {
            if (!isTwoPane && currentRoute?.startsWith(MyCityScreen.Place.name) != true) {
                MyCityNavigationBar(
                    items = navItems,
                    isSelected = isSelected,
                    onItemClick = { route -> navController.navigateToTopLevel(route) },
                )
            }
        },
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            if (isTwoPane) {
                MyCityNavigationRail(
                    items = navItems,
                    isSelected = isSelected,
                    onItemClick = { route -> navController.navigateToTopLevel(route) },
                )
            }
            NavHost(
                navController = navController,
                startDestination = MyCityScreen.Home.name,
                modifier = Modifier.weight(1f),
            ) {
                composable(route = MyCityScreen.Home.name) {
                    HomeScreen(
                        uiState = uiState,
                        isTwoPane = isTwoPane,
                        onCategoryClick = { category ->
                            navController.navigate(categoryRoute(category))
                        },
                    )
                }
                composable(
                    route = "${MyCityScreen.Category.name}/{${MyCityScreen.CATEGORY_ARG}}",
                    arguments = listOf(
                        navArgument(MyCityScreen.CATEGORY_ARG) { type = NavType.StringType },
                    ),
                ) { entry ->
                    val categoryName = entry.arguments?.getString(MyCityScreen.CATEGORY_ARG)
                    val category = Category.values()
                        .firstOrNull { it.name == categoryName }
                        ?: Category.Restaurants

                    CategoryScreen(
                        category = category,
                        uiState = uiState,
                        isTwoPane = isTwoPane,
                        onCategorySelected = viewModel::onCategorySelected,
                        onPlaceClick = { place ->
                            viewModel.onPlaceSelected(place.id)
                            if (!isTwoPane) {
                                navController.navigate(placeRoute(place.id))
                            }
                        },
                        onToggleFavorite = viewModel::onToggleFavorite,
                    )
                }
                composable(
                    route = "${MyCityScreen.Place.name}/{${MyCityScreen.PLACE_ARG}}",
                    arguments = listOf(
                        navArgument(MyCityScreen.PLACE_ARG) { type = NavType.IntType },
                    ),
                ) { entry ->
                    PlaceDetailScreen(
                        placeId = entry.arguments?.getInt(MyCityScreen.PLACE_ARG) ?: -1,
                        uiState = uiState,
                        onPlaceSelected = viewModel::onPlaceSelected,
                        onToggleFavorite = viewModel::onToggleFavorite,
                    )
                }
            }
        }
    }
}

@Composable
private fun screenTitle(backStackEntry: NavBackStackEntry?): String {
    val route = backStackEntry?.destination?.route
        ?: return stringResource(MyCityScreen.Home.title)
    return when {
        route.startsWith(MyCityScreen.Place.name) -> stringResource(MyCityScreen.Place.title)
        route.startsWith(MyCityScreen.Category.name) -> {
            val categoryName = backStackEntry.arguments?.getString(MyCityScreen.CATEGORY_ARG)
            val category = Category.values().firstOrNull { it.name == categoryName }
            category?.let { stringResource(it.titleRes) }
                ?: stringResource(MyCityScreen.Category.title)
        }
        else -> stringResource(MyCityScreen.Home.title)
    }
}

private fun NavHostController.navigateToTopLevel(route: String) {
    navigate(route) {
        launchSingleTop = true
        popUpTo(MyCityScreen.Home.name) { inclusive = false }
    }
}

@Composable
private fun MyCityNavigationBar(
    items: List<MyCityNavItem>,
    isSelected: (MyCityNavItem) -> Boolean,
    onItemClick: (String) -> Unit,
) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = isSelected(item),
                onClick = { onItemClick(item.route) },
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.label, maxLines = 1) },
            )
        }
    }
}

@Composable
private fun MyCityNavigationRail(
    items: List<MyCityNavItem>,
    isSelected: (MyCityNavItem) -> Boolean,
    onItemClick: (String) -> Unit,
) {
    NavigationRail {
        items.forEach { item ->
            NavigationRailItem(
                selected = isSelected(item),
                onClick = { onItemClick(item.route) },
                icon = { Icon(item.icon, contentDescription = null) },
                label = { Text(item.label, maxLines = 2) },
            )
        }
    }
}
