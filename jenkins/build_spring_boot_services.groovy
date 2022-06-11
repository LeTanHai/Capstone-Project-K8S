/* groovylint-disable NglParseError */
pipeline {
    agent any
    environment {
        GIT_URL = 'https://github.com/LeTanHai/Capstone-Project-K8S.git'
        WORKSPACE = 'SOURCE_CODE'
        AWS_ACCOUNT_ID="248155485793"
        AWS_DEFAULT_REGION="us-east-1" 
        IMAGE_TAG="1.0_latest"
        REPOSITORY_URI = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
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
        stage('Login into AWS ECR') {
            steps {
                sh "aws configure list"
                sh "aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"  
            }
        }
        stage('Build Cloud Config Server'){
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("cloud-config-server")
                }
            }
            environment {
                IMAGE_REPO_NAME="cloud-config-server-repo"
            }
            steps{
                // Build jar file via maven
                sh "cd ${WORKSPACE}/${BUILD_SERVICES} && pwd && mvn clean install -DskipTests=true"
                // Build docker image
                sh "cd ${WORKSPACE}/${BUILD_SERVICES} && docker build -t ${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG} ."
                // Tag docker image
                sh "docker tag ${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG} ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG}"
                // Push image to ECR repository
                sh "docker push ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG}"
            }
        }
        stage('Build Cloud Gateway'){
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("cloud-gateway")
                }
            }
            environment {
                IMAGE_REPO_NAME="cloud-gateway-repo"
            }
            steps{
                // Build jar file via maven
                sh "cd ${WORKSPACE}/${BUILD_SERVICES} && pwd && mvn clean install -DskipTests=true"
                // Build docker image
                sh "cd ${WORKSPACE}/${BUILD_SERVICES} && docker build -t ${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG} ."
                // Tag docker image
                sh "docker tag ${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG} ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG}"
                // Push image to ECR repository
                sh "docker push ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG}"
            }
        }
        stage('Build Department Service'){
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("department-service")
                }
            }
            environment {
                IMAGE_REPO_NAME="department-service-repo"
            }
            steps{
                // Build jar file via maven
                sh "cd ${WORKSPACE}/${BUILD_SERVICES} && pwd && mvn clean install -DskipTests=true"
                // Build docker image
                sh "cd ${WORKSPACE}/${BUILD_SERVICES} && docker build -t ${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG} ."
                // Tag docker image
                sh "docker tag ${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG} ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG}"
                // Push image to ECR repository
                sh "docker push ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG}"
            }
        }
        stage('Build Hystrix Dashboard'){
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("hystrix-dashboard")
                }
            }
            environment {
                IMAGE_REPO_NAME="hystrix-dashboard-repo"
            }
            steps{
                // Build jar file via maven
                sh "cd ${WORKSPACE}/${BUILD_SERVICES} && pwd && mvn clean install -DskipTests=true"
                // Build docker image
                sh "cd ${WORKSPACE}/${BUILD_SERVICES} && docker build -t ${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG} ."
                // Tag docker image
                sh "docker tag ${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG} ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG}"
                // Push image to ECR repository
                sh "docker push ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG}"
            }
        }
        stage('Build Service Registry'){
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("service-registry")
                }
            }
            environment {
                IMAGE_REPO_NAME="service-registry-repo"
            }
            steps{
                // Build jar file via maven
                sh "cd ${WORKSPACE}/${BUILD_SERVICES} && pwd && mvn clean install -DskipTests=true"
                // Build docker image
                sh "cd ${WORKSPACE}/${BUILD_SERVICES} && docker build -t ${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG} ."
                // Tag docker image
                sh "docker tag ${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG} ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG}"
                // Push image to ECR repository
                sh "docker push ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG}"
            }
        }
        stage('Build User Service'){
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("user-service")
                }
            }
            environment {
                IMAGE_REPO_NAME="user-service-repo"
            }
            steps{
                // Build jar file via maven
                sh "cd ${WORKSPACE}/${BUILD_SERVICES} && pwd && mvn clean install -DskipTests=true"
                // Build docker image
                sh "cd ${WORKSPACE}/${BUILD_SERVICES} && docker build -t ${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG} ."
                // Tag docker image
                sh "docker tag ${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG} ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG}"
                // Push image to ECR repository
                sh "docker push ${REPOSITORY_URI}/${IMAGE_REPO_NAME}:${BRANCH_BUILD}_${IMAGE_TAG}"
            }
        }
        stage('Delete docker images') {
            steps {
                /* groovylint-disable-next-line NglParseError */
                sh "docker rmi -f \$(docker images -aq)"
            }
        }
    }
    post {
        always {
            deleteDir() /* clean up our workspace */
        }
    }
}