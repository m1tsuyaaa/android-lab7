# Implementation notes — Cupcake navigation (lab 7, part 1)

All changes are in the `starter` branch of the `cupcake` repository. No Gradle build was run
(no Android SDK / Java 17 available in this environment).

## Modified files

### `app/src/main/java/com/example/cupcake/CupcakeScreen.kt`
- Added `enum class CupcakeScreen(@StringRes val title: Int)` above `CupcakeAppBar` with
  `Start`, `Flavor`, `Pickup`, `Summary`. Routes are the enum names; titles reuse the existing
  string resources `app_name`, `choose_flavor`, `choose_pickup_date`, `order_summary`
  (no new strings were needed — all required resources already existed in `strings.xml`,
  including `new_cupcake_order`).
- `CupcakeAppBar` now takes `currentScreen: CupcakeScreen`; the `TopAppBar` title is
  `stringResource(currentScreen.title)`. The back icon is still shown only when
  `canNavigateBack` is true.
- `CupcakeApp` now reads `currentBackStackEntryAsState()` and derives the current screen with
  `CupcakeScreen.valueOf(route ?: CupcakeScreen.Start.name)`. It passes `currentScreen`,
  `canNavigateBack = navController.previousBackStackEntry != null` and
  `navigateUp = { navController.navigateUp() }` to the app bar.
- Inside `Scaffold` a `NavHost` was added with `startDestination = CupcakeScreen.Start.name`
  and `modifier = Modifier.padding(innerPadding)`, with four `composable()` destinations:
  - **Start** → `StartOrderScreen` with `DataSource.quantityOptions`; selecting a quantity calls
    `viewModel.setQuantity(it)` then navigates to `Flavor`.
  - **Flavor** → `SelectOptionScreen` with flavors mapped from resource IDs via
    `LocalContext.current.resources.getString(id)`, `uiState.price`,
    `viewModel.setFlavor(it)`, `Modifier.fillMaxHeight()`; Next → `Pickup`.
  - **Pickup** → `SelectOptionScreen` with `uiState.price`, `uiState.pickupOptions`,
    `viewModel.setDate(it)`, `Modifier.fillMaxHeight()`; Next → `Summary`.
  - **Summary** → `OrderSummaryScreen` with `uiState`; Send → `shareOrder(...)`,
    Cancel → `cancelOrderAndNavigateToStart(...)`.
- Added `private fun cancelOrderAndNavigateToStart(viewModel, navController)`:
  `viewModel.resetOrder()` + `popBackStack(CupcakeScreen.Start.name, inclusive = false)`;
  it is wired to all three Cancel handlers.
- Added `private fun shareOrder(context, subject, summary)`: `Intent(Intent.ACTION_SEND)`
  with `type = "text/plain"`, `EXTRA_SUBJECT` / `EXTRA_TEXT`, launched through
  `Intent.createChooser(intent, context.getString(R.string.new_cupcake_order))`.
  `LocalContext.current` is captured in the composable scope (not inside the click lambda).

### `app/src/main/java/com/example/cupcake/ui/StartOrderScreen.kt`
- Added `onNextButtonClicked: (Int) -> Unit` parameter; quantity buttons now call it with
  `item.second` (the actual quantity). Preview updated with an empty lambda.

### `app/src/main/java/com/example/cupcake/ui/SelectOptionScreen.kt`
- Added `onCancelButtonClicked: () -> Unit = {}` and `onNextButtonClicked: () -> Unit = {}`
  parameters; wired to the existing Cancel / Next buttons.

### `app/src/main/java/com/example/cupcake/ui/SummaryScreen.kt`
- Added `onCancelButtonClicked: () -> Unit` and `onSendButtonClicked: (String, String) -> Unit`.
  Send passes the already computed `newOrder` and `orderSummary` strings; Cancel is wired to
  the outlined button. Preview updated with stub lambdas.

### `app/build.gradle.kts`
- Added instrumentation-test dependencies:
  `androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))`,
  `androidTestImplementation("androidx.compose.ui:ui-test-junit4")`,
  `androidTestImplementation("androidx.navigation:navigation-testing:2.7.4")`,
  `androidTestImplementation("junit:junit:4.13.2")`.
  `debugImplementation("androidx.compose.ui:ui-test-manifest")` was already present.
  Navigation Testing is pinned to **2.7.4** to match the project's existing
  `navigation-compose:2.7.4` (the task text mentioned 2.7.7, but mixing versions would risk
  incompatibility at runtime).

## Created files (`app/src/androidTest/java/com/example/cupcake/test/`)

- **`ScreenAssertions.kt`** — `NavController.assertCurrentRouteName(expectedRouteName)`
  comparing `currentBackStackEntry?.destination?.route` with `assertEquals`.
- **`ComposeTestRuleExtensions.kt`** — `AndroidComposeTestRule<*, *>.onNodeWithStringId(id)`
  resolving the text with `activity.getString(id)` and calling `onNodeWithText`.
- **`CupcakeScreenNavigationTest.kt`** — `createAndroidComposeRule<ComponentActivity>()`,
  a `TestNavHostController` with `ComposeNavigator` set up in `@Before`, helpers
  `navigateToFlavorScreen()`, `navigateToPickupScreen()`, `navigateToSummaryScreen()`,
  `navigateBack()`, `getFormattedDate()` (`SimpleDateFormat("E MMM d", Locale.getDefault())`
  on tomorrow's date, exactly the format used by `OrderViewModel`), and 10 tests:
  start destination, no back button on Start, One Cupcake → Flavor, back from Flavor → Start,
  cancel from Flavor → Start, Next → Pickup, back from Pickup → Flavor, cancel from Pickup →
  Start, Next → Summary, cancel from Summary → Start.
