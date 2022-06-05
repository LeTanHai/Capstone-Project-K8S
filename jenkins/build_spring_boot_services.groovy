pipeline {
    agent any
    environment {
        GIT_URL = 'https://github.com/LeTanHai/Capstone-Project-K8S.git'
        WORKSPACE = 'SOURCE_CODE'
    }
    parameters {
        // string(name: 'BRANCH_BUILD', choices: ['staging', 'preproduction', 'production'], description: 'Branch build from git')
        choice(
            name: 'BRANCH_BUILD',
            choices: ['staging', 'preproduction', 'production'],
            description: 'Branch build from git'
        )
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
            }
        }
        stage('Build Cloud Config Server'){
            steps{
                echo 'Build Cloud Config Server'
            }
        }
        stage('Build Cloud Gateway'){
            steps{
                echo 'Build Cloud Config Server'
            }
        }
        stage('Build Department Service'){
            steps{
                echo 'Build Cloud Config Server'
            }
        }
        stage('Build Hystrix Dashboard'){
            steps{
                echo 'Build Cloud Config Server'
            }
        }
        stage('Build Service Registry'){
            steps{
                echo 'Build Cloud Config Server'
            }
        }
        stage('Build User Service'){
            steps{
                echo 'Build Cloud Config Server'
            }
        }
    }
    post {
        always {
            deleteDir() /* clean up our workspace */
        }
    }
}