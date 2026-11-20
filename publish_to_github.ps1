$ErrorActionPreference = "Stop"
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$log = Join-Path $env:TEMP "opencode\publish_github.log"
function L($m) { Add-Content -LiteralPath $log -Value $m -Encoding UTF8 }
Set-Content -LiteralPath $log -Value "start" -Encoding UTF8

$existing = git -C $root remote 2>$null
if ($existing -contains "origin") { git -C $root remote remove origin 2>$null }
git -C $root remote add origin https://github.com/m1tsuyaaa/android-lab7.git
git -C $root add -A
$status = git -C $root status --short
L ("STAGED_FILES=" + ($status | Measure-Object).Count)
git -C $root commit -m "Lab 7: navigation and adaptive layouts (Cupcake, Reply, Lunch Tray, My City) + report"
L ("COMMIT=" + (git -C $root rev-parse --short HEAD))
git -C $root branch -M main
git -C $root push -u origin main 2>&1 | ForEach-Object { L $_ }
L ("PUSH_EXIT=" + $LASTEXITCODE)
