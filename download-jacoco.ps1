# Script to download JaCoCo for code coverage
Write-Host "Downloading JaCoCo..." -ForegroundColor Green

$jacocoVersion = "0.8.11"
$jacocoUrl = "https://repo1.maven.org/maven2/org/jacoco/jacoco/$jacocoVersion/jacoco-$jacocoVersion.zip"
$zipFile = "jacoco.zip"
$libFolder = "lib"

# Create lib folder if it doesn't exist
if (!(Test-Path $libFolder)) {
    New-Item -ItemType Directory -Path $libFolder
}

# Download JaCoCo
Write-Host "Downloading from $jacocoUrl..." -ForegroundColor Yellow
Invoke-WebRequest -Uri $jacocoUrl -OutFile $zipFile

# Extract specific files we need
Write-Host "Extracting JaCoCo files..." -ForegroundColor Yellow
Expand-Archive -Path $zipFile -DestinationPath "jacoco-temp" -Force

# Copy the required JAR files to lib folder
Copy-Item "jacoco-temp\lib\jacocoagent.jar" -Destination "$libFolder\jacocoagent.jar" -Force
Copy-Item "jacoco-temp\lib\jacococli.jar" -Destination "$libFolder\jacococli.jar" -Force

# Clean up
Remove-Item $zipFile -Force
Remove-Item "jacoco-temp" -Recurse -Force

Write-Host "`nJaCoCo installed successfully!" -ForegroundColor Green
Write-Host "Files added to lib folder:" -ForegroundColor Cyan
Write-Host "  - jacocoagent.jar" -ForegroundColor White
Write-Host "  - jacococli.jar" -ForegroundColor White
Write-Host "`nYou can now run your Jenkins pipeline with code coverage!" -ForegroundColor Green
