pipeline {
    agent any
    environment {
        GIT_URL = 'https://github.com/LeTanHai/Capstone-Project-K8S.git'
        WORKSPACE = 'SOURCE_CODE'
    }
    parameters {
        choice(
            name: 'BRANCH_BUILD',
            choices: ['staging', 'preproduction', 'production'],
            description: 'Branch build from git'
        )
        checkboxParameter(name: 'BUILD_SERVICES', format: 'JSON',
            pipelineSubmitContent: '{"CheckboxParameter": [{"key": "Cloud Config Server","value": "cloud-config-server"},{"key": "Cloud Gateway","value": "cloud-gateway"},{"key": "Department Service","value": "department-service"},{"key": "Hystrix Dashboard","value": "hystrix-dashboard"},{"key": "Service Registry","value": "service-registry"},{"key": "User Service","value": "user-service"}]}', description: '')
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
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("cloud-config-server")
                }
            }
            steps{
                build(job: 'BUILD_DOCKER_IMAGE', parameters: [
                    string(name: "BUILD_SERVICES", value: "${BUILD_SERVICES}")
                ])    
            }
        }
        stage('Build Cloud Gateway'){
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("cloud-gateway")
                }
            }
            steps{
                echo 'Build Cloud Config Server'
                echo "yyyyyyy:${BUILD_SERVICES}"
            }
        }
        stage('Build Department Service'){
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("department-service")
                }
            }
            steps{
                echo 'Build Cloud Config Server'
            }
        }
        stage('Build Hystrix Dashboard'){
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("hystrix-dashboard")
                }
            }
            steps{
                echo 'Build Cloud Config Server'
            }
        }
        stage('Build Service Registry'){
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("service-registry")
                }
            }
            steps{
                echo 'Build Cloud Config Server'
            }
        }
        stage('Build User Service'){
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("user-service")
                }
            }
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