$ErrorActionPreference = "SilentlyContinue"
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$report = @()

function Check-Pattern($label, $path, $pattern) {
    if (-not (Test-Path -LiteralPath $path)) {
        $script:report += "MISSING FILE: $label -> $path"
        return
    }
    $text = Get-Content -LiteralPath $path -Raw -Encoding UTF8
    if ($text -match $pattern) {
        $script:report += "OK   : $label"
    } else {
        $script:report += "FAIL : $label (pattern '$pattern' not found)"
    }
}

# Cupcake
$c = Join-Path $root "cupcake\app\src\main\java\com\example\cupcake\CupcakeScreen.kt"
Check-Pattern "Cupcake enum routes" $c "enum class CupcakeScreen"
Check-Pattern "Cupcake NavHost" $c "NavHost"
Check-Pattern "Cupcake rememberNavController" $c "rememberNavController"
Check-Pattern "Cupcake popBackStack" $c "popBackStack"
Check-Pattern "Cupcake shareOrder Intent" $c "ACTION_SEND"
Check-Pattern "Cupcake AppBar title" $c "currentScreen.title"
Check-Pattern "Cupcake backStackEntry" $c "currentBackStackEntryAsState"
Check-Pattern "Cupcake tests" (Join-Path $root "cupcake\app\src\androidTest\java\com\example\cupcake\test\CupcakeScreenNavigationTest.kt") "TestNavHostController"

# Reply
Check-Pattern "Reply WindowSizeClass" (Join-Path $root "reply\app\src\main\java\com\example\reply\ui\ReplyApp.kt") "WindowSizeClass"
Check-Pattern "Reply NavigationType utils" (Join-Path $root "reply\app\src\main\java\com\example\reply\ui\utils\WindowStateUtils.kt") "ReplyNavigationType"
Check-Pattern "Reply Detail conditional" (Join-Path $root "reply\app\src\main\java\com\example\reply\ui\ReplyHomeScreen.kt") "isShowingHomepage"
Check-Pattern "Reply BackHandler" (Join-Path $root "reply\app\src\main\java\com\example\reply\ui\ReplyDetailsScreen.kt") "BackHandler"
Check-Pattern "Reply MainActivity size class" (Join-Path $root "reply\app\src\main\java\com\example\reply\MainActivity.kt") "calculateWindowSizeClass"
Check-Pattern "Reply dependency" (Join-Path $root "reply\app\build.gradle.kts") "material3-window-size-class"

# Lunch Tray
Check-Pattern "LunchTray enum routes" (Join-Path $root "lunch-tray\app\src\main\java\com\example\lunchtray\LunchTrayScreen.kt") "enum class LunchTrayScreen"
Check-Pattern "LunchTray NavHost" (Join-Path $root "lunch-tray\app\src\main\java\com\example\lunchtray\LunchTrayScreen.kt") "NavHost"
Check-Pattern "LunchTray cancel" (Join-Path $root "lunch-tray\app\src\main\java\com\example\lunchtray\LunchTrayScreen.kt") "popBackStack"

# My City
Check-Pattern "MyCity settings" (Join-Path $root "my-city\settings.gradle.kts") "My City"
Check-Pattern "MyCity manifest" (Join-Path $root "my-city\app\src\main\AndroidManifest.xml") "MyCity"
Check-Pattern "MyCity NavHost" (Join-Path $root "my-city\app\src\main\java\com\example\mycity\ui\MyCityApp.kt") "NavHost"
Check-Pattern "MyCity ViewModel" (Join-Path $root "my-city\app\src\main\java\com\example\mycity\ui\MyCityViewModel.kt") "UiState"
Check-Pattern "MyCity data provider" (Join-Path $root "my-city\app\src\main\java\com\example\mycity\data\PlacesDataProvider.kt") "Place"
Check-Pattern "MyCity adaptive" (Join-Path $root "my-city\app\src\main\java\com\example\mycity\ui\MyCityApp.kt") "WindowWidthSizeClass"
Check-Pattern "MyCity Coil" (Join-Path $root "my-city\app\src\main\java\com\example\mycity\ui\components\PlaceImage.kt") "AsyncImage"

$report | Out-File (Join-Path $root "verify_results.txt") -Encoding UTF8
$report
