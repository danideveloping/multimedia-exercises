# Quick Install Guide (No Admin Rights Needed!)

Since Chocolatey requires administrator privileges, here's the easiest way to get started **without installing anything system-wide**:

## Option 1: Use Gradle Wrapper (Easiest - Recommended!)

The project includes a Gradle Wrapper that downloads Gradle automatically. **No installation needed!**

### Steps:

1. **Open PowerShell in the project directory:**
   ```powershell
   cd C:\Users\User\Desktop\multimedia-exercises\practicalex-2
   ```

2. **Run the wrapper (it will download Gradle automatically):**
   ```powershell
   .\gradlew.bat build
   ```

3. **Run the application:**
   ```powershell
   .\gradlew.bat run
   ```

That's it! The wrapper will download Gradle to your user directory (no admin rights needed).

---

## Option 2: Manual Gradle Installation (No Admin Rights)

If you prefer to install Gradle manually without admin rights:

1. **Download Gradle:**
   - Go to: https://gradle.org/releases/
   - Download the "binary-only" zip file (e.g., `gradle-8.5-bin.zip`)

2. **Extract to your user directory:**
   ```powershell
   # Extract to something like:
   C:\Users\User\gradle
   ```

3. **Add to your user PATH (no admin needed):**
   ```powershell
   # Open PowerShell and run:
   [Environment]::SetEnvironmentVariable("Path", $env:Path + ";C:\Users\User\gradle\gradle-8.5\bin", "User")
   ```

4. **Restart PowerShell** and verify:
   ```powershell
   gradle --version
   ```

---

## Option 3: Fix Chocolatey Installation (Requires Admin)

If you want to use Chocolatey, you need to:

1. **Close this PowerShell window**

2. **Open PowerShell as Administrator:**
   - Right-click on PowerShell
   - Select "Run as Administrator"

3. **Try installing again:**
   ```powershell
   choco install gradle
   ```

4. **If lock file error persists**, remove the lock file:
   ```powershell
   Remove-Item "C:\ProgramData\chocolatey\lib\63b8df6024465284aa96552779f0f0464e19793f" -Force
   choco install gradle
   ```

---

## Recommended: Use Gradle Wrapper!

Just run:
```powershell
.\gradlew.bat build
.\gradlew.bat run
```

No installation, no admin rights, no hassle! 🎉

