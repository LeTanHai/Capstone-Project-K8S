pipeline {
    agent any
    parameters {
        choice(
            name: 'BRANCH_BUILD',
            choices: ['staging', 'preproduction', 'production'],
            description: 'Branch build from git'
        )
        checkboxParameter(name: 'BUILD_SERVICES', format: 'JSON',
            pipelineSubmitContent: '{"CheckboxParameter": [{"key": "Cloud Config Server","value": "cloud-config-server"},{"key": "Cloud Gateway","value": "cloud-gateway"},{"key": "Department Service","value": "department-service"},{"key": "Hystrix Dashboard","value": "hystrix-dashboard"},{"key": "Service Registry","value": "service-registry"},{"key": "User Service","value": "user-service"}]}', description: '')
    }
    stages {
        stage('BUILD_SPRING_BOOT_SERVICES') {
            steps {
                build(job: 'BUILD_SPRING_BOOT_SERVICES', parameters: [
                    string(name: 'BRANCH_BUILD', value: String.valueOf(BRANCH_BUILD)),
                    string(name: 'BUILD_SERVICES', value: String.valueOf(BUILD_SERVICES))
                ])
            }
        }

        stage('CREATE_INFRATRUCTURE') {
            steps {
                sh 'cd cloudformation && aws cloudformation create-stack --stack-name capstone_stack --template-body file://infrastructure.yml  --parameters file://parameters.json --capabilities "CAPABILITY_IAM" "CAPABILITY_NAMED_IAM" --region=us-east-1'
            }
        }

        stage('DEPLOY_SERVERS') {
            steps {
                echo 'deploy services'
            }
        }
    }
    post {
        always {
            deleteDir() /* clean up our workspace */
        }
    }
}