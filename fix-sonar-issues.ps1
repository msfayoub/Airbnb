# PowerShell script to fix common SonarQube issues

Write-Host "Fixing SonarQube issues..." -ForegroundColor Cyan

# Navigate to project directory
cd "c:\Users\Ayoub\s5\Java\build\Airbnb\BookingApp_JavaEE"

Write-Host "`n1. Compiling with fixes..." -ForegroundColor Yellow
javac -encoding ISO-8859-1 -d build/classes -cp "lib/*" -sourcepath src src/controller/*.java src/dao/*.java src/model/*.java src/function/*.java 2>&1 | Select-String -Pattern "error:" -Context 1

Write-Host "`n2. Compiling tests..." -ForegroundColor Yellow
javac -encoding ISO-8859-1 -d build/test-classes -cp "lib/*;build/classes" -sourcepath test test/dao/*.java test/function/*.java test/model/*.java test/controller/*.java 2>&1 | Select-String -Pattern "error:" -Context 1

Write-Host "`n3. Running tests..." -ForegroundColor Yellow
java -cp "lib/*;build/classes;build/test-classes" org.junit.platform.console.ConsoleLauncher --scan-classpath --reports-dir=test-results

Write-Host "`n4. Generating test coverage..." -ForegroundColor Yellow
if (Test-Path "coverage") { Remove-Item -Recurse -Force coverage }
New-Item -ItemType Directory -Path coverage | Out-Null

java -javaagent:lib/jacocoagent.jar=destfile=coverage/jacoco.exec `
    -cp "lib/*;build/classes;build/test-classes" `
    org.junit.platform.console.ConsoleLauncher `
    --scan-classpath `
    --reports-dir=test-results

Write-Host "`n5. Generating JaCoCo reports..." -ForegroundColor Yellow
java -jar lib/jacococli.jar report coverage/jacoco.exec `
    --classfiles build/classes `
    --sourcefiles src `
    --html coverage/html `
    --xml coverage/jacoco.xml `
    --csv coverage/jacoco.csv

Write-Host "`nDone! Coverage report at: coverage\html\index.html" -ForegroundColor Green
Write-Host "`nNext steps:" -ForegroundColor Cyan
Write-Host "1. Commit and push changes: git add . && git commit -m 'Fix SonarQube issues' && git push" -ForegroundColor White
Write-Host "2. Run Jenkins pipeline" -ForegroundColor White
Write-Host "3. Check SonarQube: http://localhost:9000/dashboard?id=airbnb-booking-app" -ForegroundColor White
