pipeline {
    agent any
    environment {
        GIT_URL = 'https://github.com/LeTanHai/Capstone-Project-K8S.git'
        WORKSPACE = 'SOURCE_CODE'
    }
    parameters {
        string(name: 'BRANCH_BUILD', choices: ['staging', 'preproduction', 'production'], description: 'Branch build from git')
    }
    stages{
        stage('Checkout'){
            steps{
                checkout([   $class: 'GitSCM',
                branches: [[name: "${BRANCH_BUILD}"]],
                doGenerateSubmoduleConfigurations: false,
                extensions: [[$class: 'CleanBeforeCheckout'],
                            [$class: 'SubmoduleOption',
                            disableSubmodules: false,
                            parentCredentials: true,
                            recursiveSubmodules: true,
                            reference: '',
                            trackingSubmodules: false],
                            [$class: 'RelativeTargetDirectory',
                            relativeTargetDir: "${WORKSPACE}"]],
                submoduleCfg: [],
                userRemoteConfigs: [[credentialsId: 'tanhai_github', url: "${GIT_URL}"]]
                ])
                sh 'pwd'
                sh 'ls'
            }
        }
        stage('Build Cloud Config Server'){
            steps{
                echo 'Build Cloud Config Server'
            }
        }
        stage('Build Cloud Gateway'){
            steps{
                
            }
        }
        stage('Build Department Service'){
            steps{
                
            }
        }
        stage('Build Hystrix Dashboard'){
            steps{
                
            }
        }
        stage('Build Service Registry'){
            steps{
                
            }
        }
        stage('Build User Service'){
            steps{
                
            }
        }
    }
}