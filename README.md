# Лабораторная работа № 7 — Навигация. Адаптив под разный формат экрана

Репозиторий: **https://github.com/m1tsuyaaa/android-lab7**

## Состав поставки

| Папка / файл | Содержание |
| --- | --- |
| `Отчёт_по_лабораторной_работе_7.docx` | Готовый отчёт по работе (цель, теория, ход выполнения, листинги кода, тестирование, выводы) |
| `Задание_ЛР7.docx` | Исходное задание (копия вложения) |
| `cupcake/` | Задание 1 — навигация в приложении Cupcake + инструментальные тесты |
| `reply/` | Задание 2 — адаптивные макеты почтового клиента Reply |
| `lunch-tray/` | Задание 3 — навигация в Lunch Tray (контрольное задание) |
| `my-city/` | Задание 4 — приложение «Мой город» с нуля (контрольное задание) |
| `report_content.txt`, `build_report.ps1` | Исходники для пересборки отчёта в DOCX |
| `report_screenshots/` | 11 скриншотов эмулятора, встроенных в отчёт (рисунки 4.1–4.6) |
| `verify_lab7.ps1`, `verify_results.txt` | Статическая проверка ключевых элементов реализации |

## Открытие в Android Studio (пошагово)

1. Установить/использовать **JDK 17** (в этом окружении он распакован в `C:\Users\Mvideo\jdk-17.0.21+9`).
2. В Android Studio: **File → Settings → Build, Execution, Deployment → Build Tools → Gradle → Gradle JDK** → выбрать JDK 17 (не JBR 25, он слишком нов для Gradle 8.4/8.7/8.12).
3. Проверить путь SDK: **File → Settings → Android SDK → SDK Location** — должен быть `C:\Users\Mvideo\AppData\Local\Android\Sdk`. В каждого проекта уже добавлен `local.properties` с этим путём (файл не коммитится в Git).
4. **File → Open** → выбрать папку нужного проекта (`cupcake`, `reply`, `lunch-tray` или `my-city`) → дождаться Sync Gradle. В `gradle.properties` всех проектов добавлен `android.overridePathCheck=true`, поэтому кириллический путь к папке не мешает сборке на Windows.
5. Запустить приложение на эмуляторе/устройстве (Run ▶). Минимальный API 24–26 в зависимости от проекта.
6. Для Cupcake запустить инструментальные тесты: `app/src/androidTest/java/com/example/cupcake/test/CupcakeScreenNavigationTest.kt` (Run на классе теста).
7. Для проверки адаптивности использовать **Running Devices → Device Manager → Resizable emulator** в режимах Phone / Unfolded foldable / Tablet.

## Результаты проверки сборки

Все четыре проекта успешно собраны с JDK 17 и Android SDK 34 (`assembleDebug`), дополнительно собраны инструментальные тесты Cupcake (`assembleDebugAndroidTest`):

| Проект | APK |
| --- | --- |
| Cupcake | `cupcake/app/build/outputs/apk/debug/app-debug.apk` (7.6 МБ) |
| Reply | `reply/app/build/outputs/apk/debug/app-debug.apk` (15.1 МБ) |
| Lunch Tray | `lunch-tray/app/build/outputs/apk/debug/app-debug.apk` (9.45 МБ) |
| «Мой город» | `my-city/app/build/outputs/apk/debug/app-debug.apk` (16.78 МБ) |
| Тесты Cupcake | `cupcake/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk` |

APK-файлы не добавляются в Git (они исключены через `.gitignore`), но остаются на диске — их можно сразу поставить на устройство через `adb install -r <path>`.

## Сборка из командной строки

```powershell
$env:JAVA_HOME = "C:\Users\Mvideo\jdk-17.0.21+9"
$env:ANDROID_HOME = "C:\Users\Mvideo\AppData\Local\Android\Sdk"
cd cupcake        # reply / lunch-tray / my-city
.\gradlew.bat assembleDebug
```

## Скриншоты в отчёте

Скриншоты уже встроены в DOCX (раздел 4.3, рисунки 4.1–4.6, всего 11 изображений), ничего вручную вставлять не нужно. Они получены в эмуляторе Pixel 4 (API 30) после установки APK через `adb install -r`; для Reply размер окна изменялся через `adb shell wm size` / `wm density`, чтобы показать режимы COMPACT, MEDIUM и EXPANDED. Экраны Cupcake для рисунка 4.1 сняты инструментальным тестом `cupcake/app/src/androidTest/java/com/example/cupcake/test/ScreenshotCaptureTest.kt`. Исходные PNG-файлы лежат в `report_screenshots/`.

## Пересборка отчёта

```powershell
powershell -ExecutionPolicy Bypass -File build_report.ps1 `
  -ContentFile report_content.txt -OutputFile "Отчёт_по_лабораторной_работе_7.docx" -RootDir .
```

Скрипт собирает DOCX напрямую из OOXML (без Word COM), поэтому работает даже в окружении без доступной автоматизации Office. Все листинги вставляются из реальных файлов проектов, указанных в `report_content.txt`.
