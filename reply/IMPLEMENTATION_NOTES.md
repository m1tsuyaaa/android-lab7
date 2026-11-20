# Part 2 — Reply adaptive layout: implementation notes

All work was done inside `lab7/reply` (starter branch of
`google-developer-training/basic-android-kotlin-compose-training-reply-app`).
No Gradle build was run, as requested (no Android SDK / Java 17 in this environment).
Verification was static: all call sites were checked, and the used APIs were checked
against the actual `material3-window-size-class:1.2.1` and `material3:1.2.1` sources
jars from Google Maven (the versions resolved by the project's Compose BOM
`2024.02.01`).

## Changed / created files

### `app/build.gradle.kts` (changed)
- Added `implementation("androidx.compose.material3:material3-window-size-class:1.2.1")`
  next to the existing `material3` dependency. There is no version-catalog file in
  this project; version `1.2.1` is the Material3 version pinned by the existing
  Compose BOM `2024.02.01` (the BOM would also supply it without an explicit version).

### `app/src/main/java/com/example/reply/ui/utils/WindowStateUtils.kt` (created)
- New `ui.utils` package.
- `enum class ReplyNavigationType { BOTTOM_NAVIGATION, NAVIGATION_RAIL, PERMANENT_NAVIGATION_DRAWER }`
- `enum class ReplyContentType { LIST_ONLY, LIST_AND_DETAIL }`
- `fun ReplyNavigationType(widthSizeClass: WindowWidthSizeClass)`: Compact → bottom
  navigation, Medium → navigation rail, Expanded → permanent navigation drawer
  (`else` → bottom navigation).
- `fun ReplyContentType(widthSizeClass: WindowWidthSizeClass)`: Compact/Medium →
  list only, Expanded → list and detail (`else` → list only).
- The width-size-class `when` with compact/medium/expanded branches lives here: it is
  the single source of truth for the mapping requested in the codelab steps, and
  `ReplyApp` calls these helpers instead of duplicating the `when` logic.

### `app/src/main/java/com/example/reply/MainActivity.kt` (changed)
- `onCreate` is annotated with `@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)`
  and computes `val windowSize = calculateWindowSizeClass(this)` inside `setContent`
  (this API is a `@Composable`, so it must be called in composition).
- Passes `ReplyApp(windowSize = windowSize)`.
- Previews updated: compact (`widthDp = 400`, 400×900 dp), medium (`widthDp = 700`,
  700×900 dp) and expanded (`widthDp = 1000`, 1000×900 dp); each preview provides
  a `WindowSizeClass` via `WindowSizeClass.calculateFromSize(DpSize(...))` and opts in
  to `ExperimentalMaterial3WindowSizeClassApi`. In 1.2.1 `calculateFromSize` is a
  public (but experimental) companion function, so no dependency upgrade was needed.

### `app/src/main/java/com/example/reply/ui/ReplyApp.kt` (changed)
- New `windowSize: WindowSizeClass` parameter (first parameter, `modifier` last).
- Computes
  `val navigationType: ReplyNavigationType = ReplyNavigationType(windowSize.widthSizeClass)`
  and
  `val contentType: ReplyContentType = ReplyContentType(windowSize.widthSizeClass)`,
  and passes both to `ReplyHomeScreen` (in addition to the existing state/lambdas).
- All existing UI state, lambdas and `modifier` forwarding are preserved.

### `app/src/main/java/com/example/reply/ui/ReplyHomeScreen.kt` (changed)
- `ReplyHomeScreen` takes `navigationType` and `contentType`.
- Rendering logic:
  - `PERMANENT_NAVIGATION_DRAWER` → `PermanentNavigationDrawer` with a
    `PermanentDrawerSheet` (width `R.dimen.drawer_width`) containing
    `NavigationDrawerContent`, and `ReplyAppContent` as the drawer's content. The
    `isShowingHomepage` condition is intentionally **not** used here (on wide screens
    there is no separate details screen).
  - otherwise → `if (replyUiState.isShowingHomepage)` renders `ReplyAppContent`,
    `else` renders standalone `ReplyDetailsScreen(..., isFullScreen = true)`.
- `ReplyAppContent` takes `navigationType`/`contentType`; its body is now a `Row`:
  `ReplyNavigationRail` wrapped in
  `AnimatedVisibility(visible = navigationType == NAVIGATION_RAIL)`, then the main
  `Column`. Inside that `Column`, `LIST_AND_DETAIL` renders
  `ReplyListAndDetailContent`, otherwise `ReplyListOnlyContent`;
  `ReplyBottomNavigationBar` is wrapped in
  `AnimatedVisibility(visible = navigationType == BOTTOM_NAVIGATION)`.
- All four `ReplyAppContent` call sites pass both new parameters, and navigation
  items keep their existing `testTag`s.

### `app/src/main/java/com/example/reply/ui/ReplyDetailsScreen.kt` (changed)
- `BackHandler { onBackPressed() }` added as the first statement of
  `ReplyDetailsScreen`.
- New `isFullScreen: Boolean = false` parameter on `ReplyDetailsScreen` and on
  `ReplyEmailDetailsCard`.
- `ReplyDetailsScreenTopBar` is rendered only when `isFullScreen`.
- Card padding: horizontal `R.dimen.detail_card_outer_padding_horizontal` when full
  screen, end-only padding with the same dimension when embedded, applied on top of
  the existing `navigationBarsPadding()` via `Modifier.then(...)`.
- Inside `ReplyEmailDetailsCard`, the subject `Text` is shown only when
  `!isFullScreen`; in full-screen mode a `Spacer`/`height` of
  `R.dimen.detail_content_padding_top` is used instead (the subject is already the
  top-bar title there).

### `app/src/main/java/com/example/reply/ui/ReplyHomeContent.kt` (changed)
- In `ReplyListAndDetailContent`, the embedded `ReplyDetailsScreen` now receives
  `isFullScreen = false` and `onBackPressed = { activity.finish() }` using the
  existing `val activity = LocalContext.current as Activity` (no new utility was
  needed). This makes the system back button close the app on wide screens instead
  of trying to navigate to a separate details screen.
