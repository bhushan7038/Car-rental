pipeline {
    agent any

    tools {
        maven 'maven'
        jdk 'jdk17'
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/bhushan7038/Car-rental.git'
            }
        }

        stage('Build Spring Boot Backend') {
            steps {
                dir('Rent_it_spring') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }
stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv('SonarQube-2401044') {
            dir('Rent_it_spring') {
                script {
                    def scannerHome = tool 'SonarScanner'
                    sh """
                    ${scannerHome}/bin/sonar-scanner \
                    -Dsonar.projectKey=car-rental \
                    -Dsonar.projectName=car-rental \
                    -Dsonar.sources=src \
                    -Dsonar.java.binaries=target
                    """
                }
            }
        }
    }
}


       stage('Quality Gate') {
    steps {
        timeout(time: 5, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
        }
    }
}


    post {
        success {
            echo '✅ Pipeline SUCCESS'
        }
        failure {
            echo '❌ Pipeline FAILED'
        }
    }
}


