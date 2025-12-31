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


                    sh 'mvn clean package -DskipTests -Dmaven.javadoc.skip=true'
             
                }
            }
            stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv('SonarQube-2401044') {
            dir('Rent_it_spring') {
                sh '''
                mvn sonar:sonar \
                -Dsonar.projectKey=car-rental \
                -Dsonar.projectName=car-rental
                '''
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


        }
    }
}
