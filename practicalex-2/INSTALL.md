# Installation Guide

## Option 1: Using Gradle (Recommended - Easier Setup)

Gradle is often easier to install and use on Windows.

### Install Gradle

**Method A: Using Chocolatey (if you have it)**
```powershell
choco install gradle
```

**Method B: Using Scoop (if you have it)**
```powershell
scoop install gradle
```

**Method C: Manual Installation**
1. Download Gradle from https://gradle.org/releases/
2. Extract to a folder (e.g., `C:\gradle`)
3. Add `C:\gradle\bin` to your PATH environment variable
4. Restart PowerShell

### Verify Installation
```powershell
gradle --version
```

### Build and Run with Gradle
```powershell
# Build the project
gradle build

# Run the application
gradle run

# Run with custom arguments
gradle run --args="../benchmark/data/movie_dataset.jsonl index"
```

---

## Option 2: Using Maven

### Install Maven

**Method A: Using Chocolatey**
```powershell
choco install maven
```

**Method B: Using Scoop**
```powershell
scoop install maven
```

**Method C: Manual Installation**
1. Download Maven from https://maven.apache.org/download.cgi
2. Extract to a folder (e.g., `C:\Program Files\Apache\maven`)
3. Add `C:\Program Files\Apache\maven\bin` to your PATH environment variable
4. Restart PowerShell

### Verify Installation
```powershell
mvn --version
```

### Build and Run with Maven
```powershell
# Build the project
mvn clean compile

# Run the application
mvn exec:java

# Run with custom arguments
mvn exec:java -Dexec.args="../benchmark/data/movie_dataset.jsonl index"
```

---

## Option 3: Manual Compilation (No Build Tool)

If you prefer not to use a build tool, you can compile manually:

### Step 1: Download Dependencies

Download these JAR files to a `lib` folder:
- `lucene-core-9.9.0.jar`
- `lucene-queryparser-9.9.0.jar`
- `lucene-analysis-common-9.9.0.jar`
- `gson-2.10.1.jar`
- `slf4j-api-1.7.36.jar`
- `slf4j-simple-1.7.36.jar`

You can download them from Maven Central:
https://mvnrepository.com/

### Step 2: Compile

```powershell
# Create lib folder and add JARs there
mkdir lib

# Compile (adjust paths as needed)
javac -cp "lib/*" -d build src/main/java/edu/multimedia/lucene/**/*.java
```

### Step 3: Run

```powershell
java -cp "build;lib/*" edu.multimedia.lucene.MovieSearchApp
```

---

## Quick Setup Script (PowerShell)

Save this as `setup.ps1` in the project root:

```powershell
# Check for Java
Write-Host "Checking Java..."
java -version
if ($LASTEXITCODE -ne 0) {
    Write-Host "Java not found! Please install Java 11+ first."
    exit 1
}

# Check for Gradle
Write-Host "Checking Gradle..."
gradle --version 2>$null
if ($LASTEXITCODE -eq 0) {
    Write-Host "Gradle found! Building project..."
    gradle build
    Write-Host "Done! Run with: gradle run"
    exit 0
}

# Check for Maven
Write-Host "Checking Maven..."
mvn --version 2>$null
if ($LASTEXITCODE -eq 0) {
    Write-Host "Maven found! Building project..."
    mvn clean compile
    Write-Host "Done! Run with: mvn exec:java"
    exit 0
}

Write-Host "Neither Gradle nor Maven found!"
Write-Host "Please install one of them (see INSTALL.md for instructions)"
```

Run it:
```powershell
.\setup.ps1
```

---

## Recommended: Use Gradle

Gradle is generally easier to set up on Windows and has better error messages. If you're new to Java build tools, start with Gradle.

