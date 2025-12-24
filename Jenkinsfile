pipeline {
    agent any

    tools {
        maven 'maven'
        jdk 'jdk17'
    }`
    environment {
        BACKEND_IMAGE = "rent-it-backend"
        FRONTEND_IMAGE = "car-rental-frontend"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/bhushan7038/Rent_it.git'
            }
        }

        stage('Build Spring Boot Backend') {
            steps {
                dir('Rent_it_spring') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                dir('Rent_it_spring') {
                    sh 'docker build -t $BACKEND_IMAGE .'
                }
            }
        }

        stage('Build Frontend Docker Image') {
            steps {
                dir('Car_Rental_Frontend') {
                    sh 'docker build -t $FRONTEND_IMAGE .'
                }
            }
        }

        stage('Run Containers') {
            steps {
                sh '''
                docker stop rent-it-backend car-rental-frontend || true
                docker rm rent-it-backend car-rental-frontend || true

                docker run -d --name rent-it-backend -p 8080:8080 rent-it-backend
                docker run -d --name car-rental-frontend -p 4200:80 car-rental-frontend
                '''
            }
        }
    }

    post {
        success {    
            echo "✅ CI/CD Pipeline completed successfully!"
        }
        failure {
            echo "❌ CI/CD Pipeline failed. Check logs."
        }
    }
}
