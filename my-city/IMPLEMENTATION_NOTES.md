# My City («Мой город») — заметки по реализации

Новый проект Android Studio (Jetpack Compose) создан с нуля в этой папке.
Другие проекты (`cupcake`, `reply`, `lunch-tray`) не изменялись; из `cupcake`
скопированы только файлы Gradle Wrapper (`gradlew`, `gradlew.bat`,
`gradle/wrapper/gradle-wrapper.jar`) и `app/proguard-rules.pro`.

## Что сделано

- Приложение-подборка мест города: **5 категорий** (рестораны, парки,
  кофейни, кинотеатры, торговые центры) и **20 мест** (по 4 в каждой
  категории) с названием, описанием, адресом, рейтингом и фотографией.
- Все тексты мест и категорий хранятся в ресурсах
  (`res/values/places.xml`, `res/values/strings.xml`).
- Несколько экранов с навигацией Jetpack Navigation Compose:
  главный экран категорий, экран категории со списком мест, экран подробностей
  места. В верхней панели — динамический заголовок из `enum class MyCityScreen`
  (`@StringRes title`) и кнопка «Назад» при наличии предыдущего экрана.
- Нижняя навигация (`NavigationBar`) на компактных экранах и боковая
  (`NavigationRail`) на средних/больших. Выбор размера экрана — через
  `WindowSizeClass` (`calculateWindowSizeClass` в `MainActivity`).
- Адаптивная раскладка: на компактных экранах список мест и переход на
  отдельный экран деталей; на medium/expanded — два окна (список + детали)
  с выделением выбранного места. Главный экран тоже адаптируется: список
  на компактных и сетка из двух колонок на больших.
- Чёткое разделение слоёв: `data/` (модели, провайдер, UI-состояние) и
  `ui/` (экраны, компоненты, ViewModel, тема Material 3).
- UDF: `MyCityViewModel` хранит `StateFlow<MyCityUiState>`, UI подписывается
  через `collectAsState()` и отправляет события методами ViewModel
  (выбор категории/места, переключение избранного).
- Фотографии загружаются Coil с URL `images.unsplash.com`. Пока изображение
  грузится или если сеть недоступна, показывается градиентный плейсхолдер
  с эмодзи категории (плюс прозрачный `ColorPainter` в `AsyncImage`), поэтому
  битых/отсутствующих картинок не бывает. Добавлено разрешение INTERNET.
- Previews для главного экрана (компактного и расширенного), экрана категории
  (список и два окна), деталей и карточки места.

## Структура файлов

```text
settings.gradle.kts, build.gradle.kts, gradle.properties
gradle/wrapper/*                     (скопировано из cupcake, Gradle 8.7 в properties)
app/build.gradle.kts, app/proguard-rules.pro
app/src/main/AndroidManifest.xml
app/src/main/res/
  values/strings.xml                 строки UI и заголовки экранов
  values/places.xml                  названия/описания/адреса 20 мест
  values/colors.xml, values/themes.xml
  drawable/ic_launcher_city.xml      векторная иконка приложения
app/src/main/java/com/example/mycity/
  MainActivity.kt                    точка входа, WindowSizeClass, MyCityTheme
  data/Category.kt                   enum категорий (@StringRes + эмодзи)
  data/Place.kt                      data class места
  data/PlacesDataProvider.kt         20 мест, getPlaces()/getPlace()
  data/MyCityUiState.kt              неизменяемое состояние UI
  ui/MyCityScreen.kt                 enum маршрутов + функции построения route
  ui/MyCityViewModel.kt              StateFlow + события
  ui/MyCityApp.kt                    NavHost, Scaffold, TopAppBar, NavigationBar/Rail
  ui/HomeScreen.kt                   список/сетка категорий
  ui/CategoryScreen.kt               список или «список + детали»
  ui/PlaceDetailScreen.kt            полноэкранные детали (compact)
  ui/components/PlaceCard.kt, PlaceList.kt, PlaceDetails.kt, PlaceImage.kt
  ui/theme/Color.kt, Theme.kt, Type.kt
IMPLEMENTATION_NOTES.md
```

## Версии и зависимости

AGP **8.5.2**, Kotlin **2.0.20** + `org.jetbrains.kotlin.plugin.compose`
(обязательно для Kotlin 2.x), Gradle **8.7**, JDK **17**,
Compose BOM **2024.09.03**, `activity-compose 1.9.2`,
`navigation-compose 2.7.7`, `material3-window-size-class 1.2.1`,
`coil-compose 2.6.0`, `lifecycle-viewmodel-compose 2.8.6`,
`material-icons-extended`. Версии заданы напрямую в `build.gradle.kts`
(взаимосовместимый набор в стиле codelab).

## Проверка

Сборка Gradle **не выполнялась** (в среде нет Android SDK / Java 17), как и
требовалось в задании. Выполнена статическая проверка: все используемые
`R.string.*` определены, дубликатов имён строк нет. Проект готов к открытию
в Android Studio и сборке при наличии SDK.
