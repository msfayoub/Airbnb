pipeline {
    agent any
    
    tools {
        jdk 'JDK-17'
    }
    
    environment {
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_PROJECT_KEY = 'airbnb-booking-app'
        SONAR_PROJECT_NAME = 'Airbnb Booking Application'
        SONAR_TOKEN = 'sqp_2a96ccebdce00d2859e5255c8bf9ff70a60716f6'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/master']],
                    extensions: [[$class: 'CloneOption', depth: 1, noTags: false, shallow: true]],
                    userRemoteConfigs: [[url: 'https://github.com/msfayoub/Airbnb.git']]
                ])
            }
        }
        
        stage('Compile') {
            steps {
                echo 'Compiling Java source files...'
                bat '''
                    if not exist "build\\classes" mkdir build\\classes
                    javac -encoding ISO-8859-1 -d build/classes -cp "lib/*" -sourcepath src src/controller/*.java src/dao/*.java src/model/*.java src/function/*.java
                '''
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                script {
                    def scannerHome = tool 'SonarScanner'
                    withSonarQubeEnv('SonarQube') {
                        bat """
                            "${scannerHome}\\bin\\sonar-scanner.bat" ^
                            -Dsonar.projectKey=${SONAR_PROJECT_KEY} ^
                            -Dsonar.projectName="${SONAR_PROJECT_NAME}" ^
                            -Dsonar.sources=src ^
                            -Dsonar.java.binaries=build/classes ^
                            -Dsonar.sourceEncoding=ISO-8859-1 ^
                            -Dsonar.java.libraries=lib/*.jar
                        """
                    }
                }
            }
        }
        
        stage('Quality Gate') {
            steps {
                echo 'Skipping Quality Gate for now - view results at http://localhost:9000/dashboard?id=airbnb-booking-app'
                 timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                 }
            }
        }
        
stage('Package WAR') {
    steps {
        echo 'Creating WAR file...'
        bat '''
            if not exist "dist" mkdir dist

            xcopy /E /I /Y WebContent dist\\Airbnb

            if not exist "dist\\Airbnb\\WEB-INF\\classes" mkdir dist\\Airbnb\\WEB-INF\\classes
            xcopy /E /I /Y build\\classes dist\\Airbnb\\WEB-INF\\classes

            xcopy /E /I /Y src\\META-INF dist\\Airbnb\\WEB-INF\\classes\\META-INF

            cd dist
            "%JAVA_HOME%\\bin\\jar" -cvf airbnb.war -C Airbnb .
        '''
    }
}

        
        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: 'dist/airbnb.war', fingerprint: true
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
        always {
            cleanWs()
        }
    }
}
