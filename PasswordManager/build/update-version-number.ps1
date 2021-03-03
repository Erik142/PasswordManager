. .\version-number.ps1

$UPDATE_MAJOR=0
$UPDATE_MINOR=1
$UPDATE_REVISION=2

$UPDATE=$UPDATE_REVISION

$VERSION_FILE = Get-Content .\version-number.ps1

Write-Output "Current version number: $VERSION"

if ($UPDATE -eq $UPDATE_MAJOR) {
    Write-Output "Incrementing major version..."
    $OLD_VER = "$$MAJOR=$MAJOR"
    $NEW_VER = "$$MAJOR=$($MAJOR + 1)"

    $VERSION_FILE = $VERSION_FILE -replace $OLD_VER,$NEW_VER
}

if ($UPDATE -le $UPDATE_MINOR) {
    $OLD_VER = "$$MINOR=$MINOR"

    if ($UPDATE -eq $UPDATE_MINOR) {
        Write-Output "Incrementing minor version..."
        $NEW_VER = "$$MINOR=$($MINOR + 1)"
    }
    else {
        Write-Output "Resetting minor version..."
        $NEW_VER = "$$MINOR=0"
    }

    $VERSION_FILE = $VERSION_FILE -replace $OLD_VER,$NEW_VER
}

if ($UPDATE -le $UPDATE_REVISION) {
    $OLD_VER = "$$REVISION=$REVISION"

    if ($UPDATE -eq $UPDATE_REVISION) {
        Write-Output "Incrementing revision..."
        $NEW_VER = "$$REVISION=$($REVISION + 1)"
    }
    else {
        Write-Output "Resetting revision..."
        $NEW_VER = "$$REVISION=0"
    }

    $VERSION_FILE = $VERSION_FILE -replace $OLD_VER,$NEW_VER
}

if (($UPDATE -gt $UPDATE_REVISION) -or ($UPDATE -lt $UPDATE_MAJOR)) {
    Write-Output "$UPDATE is not a valid version update option, exiting...";
    return;
}

Set-Content -Value $VERSION_FILE -Path .\version-number.ps1

. .\version-number.ps1

Write-Output "New version number: $VERSION"