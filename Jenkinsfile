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
                        sh '''
                        /var/jenkins_home/tools/hudson.plugins.sonar.SonarRunnerInstallation/SonarScanner/bin/sonar-scanner \
                        -Dsonar.projectKey=car-rental \
                        -Dsonar.projectName=car-rental \
                        -Dsonar.sources=src \
                        -Dsonar.java.binaries=target
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

        stage('Docker Build & Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                    docker login -u $DOCKER_USER -p $DOCKER_PASS
                    docker build -t bhushan1044/spring-backend:1.0 .
                    docker push bhushan1044/spring-backend:1.0
                    '''
                }
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
