# Setup Gradle in user directory (no admin rights needed)
# This script downloads Gradle and sets it up for this project

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Gradle Setup (No Admin Required)" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$gradleVersion = "8.5"
$gradleZip = "gradle-$gradleVersion-bin.zip"
$gradleUrl = "https://services.gradle.org/distributions/$gradleZip"
$userGradleDir = "$env:USERPROFILE\.gradle"
$gradleHome = "$userGradleDir\gradle-$gradleVersion"

# Check if Gradle is already installed
if (Test-Path "$gradleHome\bin\gradle.bat") {
    Write-Host "Gradle $gradleVersion already installed at: $gradleHome" -ForegroundColor Green
    Write-Host ""
    Write-Host "To use it, run:" -ForegroundColor Yellow
    Write-Host "  `$env:PATH = `"$gradleHome\bin;`$env:PATH`"" -ForegroundColor White
    Write-Host "  gradle build" -ForegroundColor White
    exit 0
}

# Create .gradle directory if it doesn't exist
if (-not (Test-Path $userGradleDir)) {
    New-Item -ItemType Directory -Path $userGradleDir -Force | Out-Null
}

$downloadPath = "$userGradleDir\$gradleZip"
$extractPath = "$userGradleDir"

Write-Host "Downloading Gradle $gradleVersion..." -ForegroundColor Yellow
Write-Host "This may take a few minutes..." -ForegroundColor Yellow
Write-Host ""

try {
    # Download Gradle
    Invoke-WebRequest -Uri $gradleUrl -OutFile $downloadPath -UseBasicParsing
    
    Write-Host "Download complete!" -ForegroundColor Green
    Write-Host "Extracting..." -ForegroundColor Yellow
    
    # Extract zip file
    Expand-Archive -Path $downloadPath -DestinationPath $extractPath -Force
    
    # Clean up zip file
    Remove-Item $downloadPath -Force
    
    Write-Host "Extraction complete!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Gradle installed to: $gradleHome" -ForegroundColor Green
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "  Next Steps" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "1. Add Gradle to PATH for this session:" -ForegroundColor Yellow
    Write-Host "   `$env:PATH = `"$gradleHome\bin;`$env:PATH`"" -ForegroundColor White
    Write-Host ""
    Write-Host "2. Verify installation:" -ForegroundColor Yellow
    Write-Host "   gradle --version" -ForegroundColor White
    Write-Host ""
    Write-Host "3. Build the project:" -ForegroundColor Yellow
    Write-Host "   gradle build" -ForegroundColor White
    Write-Host ""
    Write-Host "4. Run the application:" -ForegroundColor Yellow
    Write-Host "   gradle run" -ForegroundColor White
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "To make Gradle available permanently, add this to your PowerShell profile:" -ForegroundColor Yellow
    Write-Host "  `$env:PATH = `"$gradleHome\bin;`$env:PATH`"" -ForegroundColor White
    Write-Host ""
    Write-Host "Or run this command each time you open PowerShell." -ForegroundColor Gray
    
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "Manual installation:" -ForegroundColor Yellow
    Write-Host "1. Download from: $gradleUrl" -ForegroundColor White
    Write-Host "2. Extract to: $userGradleDir" -ForegroundColor White
    Write-Host "3. Add to PATH: `$env:PATH = `"$userGradleDir\gradle-$gradleVersion\bin;`$env:PATH`"" -ForegroundColor White
    exit 1
}

