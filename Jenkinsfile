pipeline {
    agent { 
        docker { image 'maven:3.9.8-eclipse-temurin-21-alpine' } 
    }
    stages {
        stage('build') {
            steps {
                 script {
                    def dockerHome = tool 'myDocker'
                    env.PATH = "${dockerHome}/bin:${env.PATH}"                
                     sh 'mvn --version'
                }                
            }
        }
    }
}
