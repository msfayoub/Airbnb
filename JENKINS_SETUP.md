# Jenkins & SonarQube Setup Guide

## Prerequisites
- ✅ Jenkins installed and running
- ✅ SonarQube installed and running
- ✅ JDK 17 installed
- ✅ Git installed

## Jenkins Configuration Steps

### 1. Install Required Jenkins Plugins
Go to **Manage Jenkins** → **Manage Plugins** → **Available**:
- SonarQube Scanner
- Pipeline
- Git Plugin

### 2. Configure JDK in Jenkins
**Manage Jenkins** → **Global Tool Configuration**:
- Add JDK:
  - Name: `JDK17`
  - JAVA_HOME: `C:\Program Files\Java\jdk-17`
  - Uncheck "Install automatically"

### 3. Configure SonarQube Scanner
**Manage Jenkins** → **Global Tool Configuration** → **SonarQube Scanner**:
- Name: `SonarScanner`
- Install automatically OR point to existing installation

### 4. Configure SonarQube Server
**Manage Jenkins** → **Configure System** → **SonarQube servers**:
- Name: `SonarQube`
- Server URL: `http://localhost:9000`
- Server authentication token: (Generate from SonarQube)

## SonarQube Configuration Steps

### 1. Generate Authentication Token
1. Login to SonarQube: http://localhost:9000
2. Default credentials: `admin` / `admin` (change on first login)
3. Go to **My Account** → **Security** → **Generate Token**
4. Copy the token and add it to Jenkins credentials

### 2. Add Token to Jenkins
**Manage Jenkins** → **Manage Credentials** → **System** → **Global credentials**:
- Kind: Secret text
- Secret: (paste SonarQube token)
- ID: `sonarqube-token`
- Description: SonarQube Authentication Token

### 3. Configure Quality Gate (Optional)
In SonarQube:
- **Quality Gates** → Create or modify existing gate
- Set thresholds for code coverage, bugs, vulnerabilities, etc.

## Create Jenkins Pipeline

### Option 1: Pipeline from SCM (Recommended)
1. **New Item** → **Pipeline**
2. Name: `Airbnb-Booking-App`
3. **Pipeline** section:
   - Definition: **Pipeline script from SCM**
   - SCM: **Git**
   - Repository URL: Your GitHub repository URL
   - Branch: `*/master`
   - Script Path: `Jenkinsfile`

### Option 2: Direct Pipeline Script
1. **New Item** → **Pipeline**
2. Name: `Airbnb-Booking-App`
3. **Pipeline** section:
   - Definition: **Pipeline script**
   - Paste the contents of `Jenkinsfile`

## Running the Pipeline

1. Click **Build Now**
2. Monitor the pipeline stages:
   - ✓ Checkout
   - ✓ Compile
   - ✓ SonarQube Analysis
   - ✓ Quality Gate
   - ✓ Package WAR
   - ✓ Archive Artifacts

## Accessing Results

### Jenkins
- **Build History**: View past builds
- **Artifacts**: Download generated WAR file
- **Console Output**: View detailed logs

### SonarQube
- Dashboard: http://localhost:9000/dashboard?id=airbnb-booking-app
- View:
  - Code quality metrics
  - Bugs and vulnerabilities
  - Code smells
  - Security hotspots
  - Code coverage (if tests added)

## Troubleshooting

### Common Issues

**1. SonarQube connection failed**
- Check SonarQube is running: http://localhost:9000
- Verify authentication token in Jenkins credentials
- Check firewall settings

**2. Compilation errors**
- Ensure JDK 17 is configured correctly
- Check all JAR files are in `lib` folder
- Verify source encoding (ISO-8859-1)

**3. Quality Gate timeout**
- Increase timeout in Jenkinsfile
- Check SonarQube webhook configuration
- Verify Jenkins can reach SonarQube

**4. Permission errors**
- Run Jenkins as administrator
- Check file/folder permissions

## Next Steps

### Add to Your GitHub Repository

```powershell
cd "C:\Users\Ayoub\s5\Java\build\Airbnb\BookingApp_JavaEE"

# Remove old remote
git remote remove origin

# Add your new GitHub repository
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git

# Add new files
git add Jenkinsfile sonar-project.properties JENKINS_SETUP.md

# Commit
git commit -m "Add Jenkins and SonarQube configuration"

# Push
git push -u origin master
```

### Webhook for Automatic Builds (Optional)
1. In GitHub repository: **Settings** → **Webhooks** → **Add webhook**
2. Payload URL: `http://YOUR_JENKINS_URL/github-webhook/`
3. Content type: `application/json`
4. Events: **Just the push event**

This will trigger Jenkins builds automatically on every git push!

## Project Structure
```
BookingApp_JavaEE/
├── Jenkinsfile              # Jenkins pipeline configuration
├── sonar-project.properties # SonarQube project configuration
├── src/                     # Source code
├── WebContent/              # Web resources
├── lib/                     # Libraries
└── build/                   # Compiled classes (generated)
```

## Useful Commands

**Manual SonarQube scan:**
```powershell
sonar-scanner.bat -Dproject.settings=sonar-project.properties
```

**Manual compile:**
```powershell
javac -encoding ISO-8859-1 -d build/classes -cp "lib/*" -sourcepath src src/**/*.java
```

## Support
- Jenkins Documentation: https://www.jenkins.io/doc/
- SonarQube Documentation: https://docs.sonarqube.org/
