param(
    [string]$ContentFile = "C:\Users\Mvideo\Documents\Проект по умолчанию\lab7\report_content.txt",
    [string]$OutputFile  = "C:\Users\Mvideo\Documents\Проект по умолчанию\lab7\report.docx",
    [string]$RootDir     = "C:\Users\Mvideo\Documents\Проект по умолчанию\lab7"
)

$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.IO.Compression
Add-Type -AssemblyName System.IO.Compression.FileSystem

$script:sb = New-Object System.Text.StringBuilder
$script:imageRels = @()
function W([string]$s) { [void]$script:sb.Append($s) }
function E([string]$s) { return [System.Security.SecurityElement]::Escape($s) }
Add-Type -AssemblyName System.Drawing

function Add-Para {
    param(
        [string]$Text = "",
        [string]$Font = "Calibri",
        [int]$Size = 24,
        [bool]$Bold = $false,
        [bool]$Italic = $false,
        [string]$Align = "",
        [int]$Before = 0,
        [int]$After = 120,
        [int]$Indent = 0,
        [string]$Shade = "",
        [bool]$Keep = $false
    )
    $rpr = "<w:rPr><w:rFonts w:ascii='$Font' w:hAnsi='$Font' w:cs='$Font' w:eastAsia='$Font'/>"
    if ($Bold) { $rpr += "<w:b/><w:bCs/>" }
    if ($Italic) { $rpr += "<w:i/><w:iCs/>" }
    $rpr += "<w:sz w:val='$Size'/><w:szCs w:val='$Size'/></w:rPr>"
    $ppr = "<w:pPr>"
    if ($Keep) { $ppr += "<w:keepNext/>" }
    if ($Shade -ne "") { $ppr += "<w:shd w:val='clear' w:color='auto' w:fill='$Shade'/>" }
    $ppr += "<w:spacing w:before='$Before' w:after='$After' w:line='240' w:lineRule='auto'/>"
    if ($Align -ne "") { $ppr += "<w:jc w:val='$Align'/>" }
    if ($Indent -gt 0) { $ppr += "<w:ind w:left='$Indent' w:hanging='0'/>" }
    $ppr += "</w:pPr>"
    W ("<w:p>" + $ppr + "<w:r>" + $rpr + "<w:t xml:space='preserve'>" + (E $Text) + "</w:t></w:r></w:p>")
}

function Add-CodeLine([string]$Text) {
    Add-Para -Text $Text -Font "Consolas" -Size 16 -Before 0 -After 0 -Shade "F4F4F4" -Indent 120
}

function Add-CodeFile([string]$RelPath) {
    $full = Join-Path $RootDir $RelPath
    Add-Para -Text ("Listing: " + $RelPath) -Font "Calibri" -Size 19 -Bold $true -Italic $true -Before 160 -After 60 -Keep $true
    if ($RelPath.StartsWith("@")) { $RelPath = $RelPath.Substring(1).Trim(); $full = Join-Path $RootDir $RelPath }
    if (Test-Path -LiteralPath $full) {
        foreach ($codeLine in (Get-Content -LiteralPath $full -Encoding UTF8)) {
            Add-CodeLine $codeLine.TrimEnd("`r")
        }
    } else {
        Add-CodeLine "// FILE NOT FOUND: $RelPath"
    }
    Add-Para -Text "" -Font "Calibri" -Size 12 -After 80
}

function Add-Image([string]$RelName) {
    $shotDir = Join-Path $RootDir "report_screenshots"
    $full = Join-Path $shotDir $RelName
    if (-not (Test-Path -LiteralPath $full)) {
        Add-Para -Text ("[IMAGE NOT FOUND: " + $RelName + "]") -Font "Calibri" -Size 18 -Italic $true -Align "center"
        return
    }
    $bytes = [System.IO.File]::ReadAllBytes($full)
    $img = [System.Drawing.Image]::FromFile($full)
    $wpx = $img.Width
    $hpx = $img.Height
    $img.Dispose()
    $maxW = 5486400
    $maxH = 6400800
    if ($wpx -le 0) { $wpx = 1080 }
    if ($hpx -le 0) { $hpx = 1920 }
    $scale = [Math]::Min(($maxW / $wpx), ($maxH / $hpx))
    if ($scale -gt 1) { $scale = 1 }
    $cx = [int]($wpx * $scale)
    $cy = [int]($hpx * $scale)
    $rid = "rId" + (3 + $script:imageRels.Count)
    $script:imageRels += [pscustomobject]@{ Id = $rid; Name = $RelName; Bytes = $bytes }
    $id = $script:imageRels.Count
    $xml = "<w:p><w:pPr><w:jc w:val='center'/><w:spacing w:before='60' w:after='160'/></w:pPr><w:r><w:drawing>"
    $xml += "<wp:inline distT='0' distB='0' distL='0' distR='0' xmlns:wp='http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing'>"
    $xml += "<wp:extent cx='$cx' cy='$cy'/><wp:effectExtent l='0' t='0' r='0' b='0'/>"
    $xml += "<wp:docPr id='$id' name='Picture $id'/><wp:cNvGraphicFramePr><a:graphicFrameLocks xmlns:a='http://schemas.openxmlformats.org/drawingml/2006/main' noChangeAspect='1'/></wp:cNvGraphicFramePr>"
    $xml += "<a:graphic xmlns:a='http://schemas.openxmlformats.org/drawingml/2006/main'><a:graphicData uri='http://schemas.openxmlformats.org/drawingml/2006/picture'>"
    $xml += "<pic:pic xmlns:pic='http://schemas.openxmlformats.org/drawingml/2006/picture'><pic:nvPicPr><pic:cNvPr id='$id' name='$RelName'/><pic:cNvPicPr/></pic:nvPicPr>"
    $xml += "<pic:blipFill><a:blip r:embed='$rid'/><a:stretch><a:fillRect/></a:stretch></pic:blipFill>"
    $xml += "<pic:spPr><a:xfrm><a:off x='0' y='0'/><a:ext cx='$cx' cy='$cy'/></a:xfrm><a:prstGeom prst='rect'><a:avLst/></a:prstGeom></pic:spPr>"
    $xml += "</pic:pic></a:graphicData></a:graphic></wp:inline></w:drawing></w:r></w:p>"
    W $xml
}

$lines = Get-Content -LiteralPath $ContentFile -Encoding UTF8
W '<?xml version="1.0" encoding="UTF-8" standalone="yes"?>'
W '<w:document xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"><w:body>'

foreach ($raw in $lines) {
    $line = $raw.TrimEnd("`r")
    if ($line -eq "@@PAGE@@") { W '<w:p><w:r><w:br w:type="page"/></w:r></w:p>'; continue }
    if ($line -like "@@TITLE@@*")     { Add-Para ($line.Substring(9).Trim())  "Calibri" 44 $true  $false "center" 1000 200; continue }
    if ($line -like "@@SUBTITLE@@*")  { Add-Para ($line.Substring(12).Trim()) "Calibri" 30 $false $true  "center" 0    400; continue }
    if ($line -like "@@CENTER@@*")    { Add-Para ($line.Substring(10).Trim()) "Calibri" 26 $false $false "center" 120  120; continue }
    if ($line -like "@@COVERINFO@@*") { Add-Para ($line.Substring(13).Trim()) "Calibri" 24 $false $false "center" 80   80; continue }
    if ($line -like "@@H1@@*")        { Add-Para ($line.Substring(6).Trim())  "Calibri" 32 $true  $false ""       360  160 0 "" $true; continue }
    if ($line -like "@@H2@@*")        { Add-Para ($line.Substring(6).Trim())  "Calibri" 28 $true  $false ""       280  120 0 "" $true; continue }
    if ($line -like "@@H3@@*")        { Add-Para ($line.Substring(6).Trim())  "Calibri" 24 $true  $false ""       200  100 0 "" $true; continue }
    if ($line -like "@@CAPTION@@*")   { Add-Para ($line.Substring(11).Trim()) "Calibri" 20 $false $true  "center" 80   160; continue }
    if ($line -like "@@NOTE@@*")      { Add-Para ($line.Substring(8).Trim())  "Calibri" 20 $false $true  ""       80   160 240; continue }
    if ($line -like "@@BULLET@@*")    { Add-Para ("*  " + $line.Substring(10).Trim()) "Calibri" 24 $false $false "" 0 60 360; continue }
    if ($line -like "@@P@@*")         { Add-Para ($line.Substring(5).Trim())  "Calibri" 24 $false $false "both"  0   140; continue }
    if ($line -like "@@CODE@@*")      { Add-CodeFile $line.Substring(8).Trim(); continue }
    if ($line -like "@@IMG@@*")       { Add-Image $line.Substring(7).Trim(); continue }
    if ($line -like "@@RAWCODE@@*")   { Add-CodeLine $line.Substring(11); continue }
    if ($line -eq "")                 { Add-Para "" "Calibri" 12 -After 80; continue }
    Add-Para $line "Calibri" 24 $false $false "both" 0 140
}

W '<w:sectPr>'
W '<w:pgSz w:w="11906" w:h="16838"/>'
W '<w:pgMar w:top="1134" w:right="1134" w:bottom="1134" w:left="1417" w:header="709" w:footer="709" w:gutter="0"/>'
W '<w:footerReference w:type="default" r:id="rId1"/>'
W '<w:cols w:space="708"/>'
W '<w:docGrid w:linePitch="360"/>'
W '</w:sectPr></w:body></w:document>'

$documentXml = $script:sb.ToString()

$contentTypes = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
<Default Extension="rels" ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
<Default Extension="xml" ContentType="application/xml"/>
<Default Extension="png" ContentType="image/png"/>
<Override PartName="/word/document.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
<Override PartName="/word/styles.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml"/>
<Override PartName="/word/footer1.xml" ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.footer+xml"/>
<Override PartName="/docProps/core.xml" ContentType="application/vnd.openxmlformats-package.core-properties+xml"/>
<Override PartName="/docProps/app.xml" ContentType="application/vnd.openxmlformats-officedocument.extended-properties+xml"/>
</Types>
'@

$rootRels = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument" Target="word/document.xml"/>
<Relationship Id="rId2" Type="http://schemas.openxmlformats.org/package/2006/relationships/metadata/core-properties" Target="docProps/core.xml"/>
<Relationship Id="rId3" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/extended-properties" Target="docProps/app.xml"/>
</Relationships>
'@

$docRels = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
<Relationship Id="rId1" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/footer" Target="footer1.xml"/>
<Relationship Id="rId2" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles" Target="styles.xml"/>
'@
foreach ($ir in $script:imageRels) {
    $docRels += "<Relationship Id='$($ir.Id)' Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/image' Target='media/$($ir.Name)'/>`n"
}
$docRels += "</Relationships>"

$stylesXml = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:styles xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
<w:docDefaults><w:rPrDefault><w:rPr><w:rFonts w:ascii="Calibri" w:hAnsi="Calibri" w:cs="Calibri"/><w:sz w:val="24"/><w:szCs w:val="24"/></w:rPr></w:rPrDefault>
<w:pPrDefault><w:pPr><w:spacing w:after="120" w:line="240" w:lineRule="auto"/></w:pPr></w:pPrDefault></w:docDefaults>
<w:style w:type="paragraph" w:default="1" w:styleId="Normal"><w:name w:val="Normal"/><w:qFormat/></w:style>
</w:styles>
'@

$footerXml = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<w:ftr xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main">
<w:p><w:pPr><w:jc w:val="center"/></w:pPr><w:r><w:fldChar w:fldCharType="begin"/></w:r><w:r><w:instrText xml:space="preserve"> PAGE </w:instrText></w:r><w:r><w:fldChar w:fldCharType="separate"/></w:r><w:r><w:t>1</w:t></w:r><w:r><w:fldChar w:fldCharType="end"/></w:r></w:p>
</w:ftr>
'@

$now = (Get-Date).ToString("yyyy-MM-ddTHH:mm:ssZ")
$coreXml = @"
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<cp:coreProperties xmlns:cp="http://schemas.openxmlformats.org/package/2006/metadata/core-properties" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<dc:title>Lab Work 7 - Navigation and Adaptive Layouts</dc:title>
<dc:creator>Student</dc:creator><cp:lastModifiedBy>Student</cp:lastModifiedBy>
<dcterms:created xsi:type="dcterms:W3CDTF">$now</dcterms:created>
<dcterms:modified xsi:type="dcterms:W3CDTF">$now</dcterms:modified>
</cp:coreProperties>
"@

$appXml = @'
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Properties xmlns="http://schemas.openxmlformats.org/officeDocument/2006/extended-properties" xmlns:vt="http://schemas.openxmlformats.org/officeDocument/2006/docPropsVTypes">
<Application>Microsoft Office Word</Application><AppVersion>16.0000</AppVersion>
</Properties>
'@

$parts = [ordered]@{
    "[Content_Types].xml"          = $contentTypes
    "_rels/.rels"                  = $rootRels
    "word/document.xml"            = $documentXml
    "word/_rels/document.xml.rels" = $docRels
    "word/styles.xml"              = $stylesXml
    "word/footer1.xml"             = $footerXml
    "docProps/core.xml"            = $coreXml
    "docProps/app.xml"             = $appXml
}

if (Test-Path -LiteralPath $OutputFile) { Remove-Item -LiteralPath $OutputFile -Force }
$fs = [System.IO.File]::Open($OutputFile, [System.IO.FileMode]::CreateNew)
try {
    $zip = New-Object System.IO.Compression.ZipArchive($fs, [System.IO.Compression.ZipArchiveMode]::Create, $false)
    try {
        $enc = New-Object System.Text.UTF8Encoding($false)
        foreach ($name in $parts.Keys) {
            $entry = $zip.CreateEntry($name, [System.IO.Compression.CompressionLevel]::Optimal)
            $stream = $entry.Open()
            $writer = New-Object System.IO.StreamWriter($stream, $enc)
            try { $writer.Write($parts[$name]) } finally { $writer.Dispose() }
        }
        foreach ($ir in $script:imageRels) {
            $entry = $zip.CreateEntry("word/media/$($ir.Name)", [System.IO.Compression.CompressionLevel]::Optimal)
            $stream = $entry.Open()
            try { $stream.Write($ir.Bytes, 0, $ir.Bytes.Length) } finally { $stream.Dispose() }
        }
    } finally { $zip.Dispose() }
} finally { $fs.Dispose() }

Write-Output ("REPORT_SAVED: " + $OutputFile)
