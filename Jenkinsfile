pipeline {
    agent any

    tools {
        maven 'maven'
        jdk 'jdk17'
    }

    environment {
        MAVEN_OPTS = "-Xmx512m"
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
                    sh 'mvn clean package -DskipTests -Dmaven.javadoc.skip=true'
                }
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                dir('Rent_it_spring') {
                    sh 'docker build -t rent-it-backend .'
                }
            }
        }

        stage('Build Frontend Docker Image') {
            steps {
                dir('Car_Rental_Frontend') {
                    sh 'docker build -t car-rental-frontend .'
                }
            }
        }
    }

    post {
        success {
            echo '✅ CI/CD Pipeline executed successfully'
        }
        failure {
            echo '❌ CI/CD Pipeline failed'
        }
    }
}
