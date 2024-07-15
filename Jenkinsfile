pipeline {
    agent { 
        docker { image 'maven:3.9.8-eclipse-temurin-21-alpine' } 
    }
    stages {
        stage('Initialize') {
            steps {
                def dockerHome = tool 'myDocker'
                env.PATH = "${dockerHome}/bin:${env.PATH}"                
            }
        }
        stage('build') {
            steps {
                sh 'mvn --version'
            }
        }
    }
}
