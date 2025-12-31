pipeline {
    agent any

    environment {
        MAVEN_OPTS = "-Xmx512m"
    }

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
                    sh 'mvn -B -T 1C clean package -DskipTests -Dmaven.javadoc.skip=true -Dmaven.repo.local=.m2'

                }
            }
        }
    }
}
