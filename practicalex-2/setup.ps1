# Setup script for Lucene Movie Search Project
# Checks for Java and build tools, then builds the project

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Lucene Movie Search - Setup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check for Java
Write-Host "Checking Java installation..." -ForegroundColor Yellow
$javaCheck = Get-Command java -ErrorAction SilentlyContinue
if ($javaCheck) {
    $javaVersion = java -version 2>&1 | Select-Object -First 1
    Write-Host "✓ Java found: $javaVersion" -ForegroundColor Green
} else {
    Write-Host "✗ Java not found!" -ForegroundColor Red
    Write-Host "  Please install Java 11 or higher from https://adoptium.net/" -ForegroundColor Yellow
    exit 1
}

Write-Host ""

# Check for Gradle
Write-Host "Checking for Gradle..." -ForegroundColor Yellow
$gradleFound = $false
$gradleCheck = Get-Command gradle -ErrorAction SilentlyContinue
if ($gradleCheck) {
    $gradleVersion = gradle --version 2>&1 | Select-String "Gradle" | Select-Object -First 1
    if ($gradleVersion) {
        Write-Host "✓ Gradle found: $gradleVersion" -ForegroundColor Green
        $gradleFound = $true
    }
}

# Check for Maven
Write-Host "Checking for Maven..." -ForegroundColor Yellow
$mavenFound = $false
$mavenCheck = Get-Command mvn -ErrorAction SilentlyContinue
if ($mavenCheck) {
    $mavenVersion = mvn --version 2>&1 | Select-String "Apache Maven" | Select-Object -First 1
    if ($mavenVersion) {
        Write-Host "✓ Maven found: $mavenVersion" -ForegroundColor Green
        $mavenFound = $true
    }
}

Write-Host ""

# Build with available tool
if ($gradleFound) {
    Write-Host "Building with Gradle..." -ForegroundColor Cyan
    gradle build
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✓ Build successful!" -ForegroundColor Green
        Write-Host ""
        Write-Host "To run the application:" -ForegroundColor Yellow
        Write-Host "  gradle run" -ForegroundColor White
        Write-Host ""
    } else {
        Write-Host "✗ Build failed!" -ForegroundColor Red
        exit 1
    }
} elseif ($mavenFound) {
    Write-Host "Building with Maven..." -ForegroundColor Cyan
    mvn clean compile
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✓ Build successful!" -ForegroundColor Green
        Write-Host ""
        Write-Host "To run the application:" -ForegroundColor Yellow
        Write-Host "  mvn exec:java" -ForegroundColor White
        Write-Host ""
    } else {
        Write-Host "✗ Build failed!" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "✗ No build tool found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install either Gradle or Maven:" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Gradle (recommended):" -ForegroundColor Cyan
    Write-Host "  choco install gradle" -ForegroundColor White
    Write-Host "  OR download from https://gradle.org/releases/" -ForegroundColor White
    Write-Host ""
    Write-Host "Maven:" -ForegroundColor Cyan
    Write-Host "  choco install maven" -ForegroundColor White
    Write-Host "  OR download from https://maven.apache.org/download.cgi" -ForegroundColor White
    Write-Host ""
    Write-Host "See INSTALL.md for detailed instructions." -ForegroundColor Yellow
    exit 1
}
