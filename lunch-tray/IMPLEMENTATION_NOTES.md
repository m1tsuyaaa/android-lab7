# Отчёт по контрольному заданию 1 (навигация в Lunch Tray)

Реализована навигация Jetpack Navigation Compose в приложении Lunch Tray (стартовый код репозитория `google-developer-training/basic-android-kotlin-compose-training-lunch-tray`, ветка `starter`).

## Изменённые / созданные файлы

| Файл | Изменение |
| --- | --- |
| `app/src/main/java/com/example/lunchtray/LunchTrayScreen.kt` | Полностью реализован: enum экранов, AppBar, NavController, Scaffold + NavHost, все переходы и сброс заказа. |
| `IMPLEMENTATION_NOTES.md` | Создан: данный отчёт. |

Остальные файлы не менялись: в стартовом коде уже были готовые сигнатуры всех экранов с нужными колбэками (`onStartOrderButtonClicked`, `onNextButtonClicked`, `onCancelButtonClicked`, `onSelectionChanged`) и превью с пустыми лямбдами, а также все необходимые строковые ресурсы.

## Детали реализации

- `enum class LunchTrayScreen(@StringRes val title: Int)` с константами `Start`, `EntreeMenu`, `SideDishMenu`, `AccompanimentMenu`, `Checkout`. Имя константы используется как маршрут навигации. Заголовки берутся из существующих ресурсов: `app_name`, `choose_entree`, `choose_side_dish`, `choose_accompaniment`, `order_checkout` — новые строки добавлять не потребовалось (в задании указано, что строки уже есть в стартовом коде).
- `LunchTrayAppBar(currentScreen, canNavigateBack, navigateUp, modifier)` отображает `TopAppBar` с динамическим заголовком `stringResource(currentScreen.title)` и иконкой `Icons.Filled.ArrowBack` (с `contentDescription = back_button`) только тогда, когда `canNavigateBack == true`. На стартовом экране стрелки нет.
- В `LunchTrayApp()` создаются `rememberNavController()`, `currentBackStackEntryAsState()` и `currentScreen = LunchTrayScreen.valueOf(backStackEntry?.destination?.route ?: LunchTrayScreen.Start.name)`; `canNavigateBack = navController.previousBackStackEntry != null`, `navigateUp = { navController.navigateUp() }`.
- `Scaffold` с верхним баром и `NavHost(startDestination = LunchTrayScreen.Start.name, modifier = Modifier.padding(innerPadding))`. `OrderViewModel` создаётся один раз через `viewModel()` и используется всеми destination; `uiState` собирается через `collectAsState()`.
- Маршруты:
  - Start → «Start Order» → `EntreeMenu`;
  - EntreeMenu → «Next» → `SideDishMenu`;
  - SideDishMenu → «Next» → `AccompanimentMenu`;
  - AccompanimentMenu → «Next» → `Checkout`;
  - Checkout → «Send»/Submit → Start;
  - «Cancel» на любом меню и на Checkout → Start.
- Выбор блюд остаётся в UDF-стиле: `viewModel.updateEntree(item)`, `viewModel.updateSideDish(item)`, `viewModel.updateAccompaniment(item)` вызываются в лямбдах `onSelectionChanged` внутри `NavHost`.
- Для «Send» и «Cancel» используется общий хелпер `cancelOrderAndNavigateToStart(viewModel, navController)`: `viewModel.resetOrder()` + `navController.popBackStack(LunchTrayScreen.Start.name, inclusive = false)`.
- Зависимость `androidx.navigation:navigation-compose:2.8.5` уже присутствовала в `app/build.gradle.kts`; иконки Material (`Icons.Filled.ArrowBack`) доступны транзитивно через `material3`, поэтому файл сборки не изменялся.
- Экраны меню и Checkout получили `Modifier.verticalScroll(rememberScrollState())` (и горизонтальный padding для Checkout) для корректного отображения на небольших экранах.

Сборка Gradle не запускалась (по условию задачи отсутствуют Android SDK / Java 17); корректность проверена по исходникам и по эталонному решению codelab'а.
