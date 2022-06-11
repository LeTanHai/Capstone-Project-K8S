pipeline {
    agent any
    parameters{
        string(name: 'BRANCH_BUILD', defaultValue: 'staging', description: 'The branch of git')
        string(name: 'BUILD_SERVICES', defaultValue: '', description: 'List of build services')
    }
    stages {
        stage('Deploy Cloud Config Server') {
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("cloud-config-server")
                }
            }
            steps {
                sh 'sudo chmod +x /home/ec2-user/bin/kubectl && /home/ec2-user/bin/kubectl get nodes -o wide'
                sh 'sudo /home/ec2-user/bin/kubectl apply -f kubernetes/config-server.yml'
            }
        }
        stage('Deploy Cloud Gateway') {
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("cloud-gateway")
                }
            }
            steps {
                sh 'kubectl apply -f kubernetes/cloud-gateway.yml'
            }
        }
        stage('Deploy Department Service') {
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("department-service")
                }
            }
            steps {
                sh 'kubectl apply -f kubernetes/department-service-deploy.yml'
            }
        }
        stage('Deploy Hystrix Dashboard') {
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("hystrix-dashboard")
                }
            }
            steps {
                sh 'kubectl apply -f kubernetes/hystrix-dashboard.yml'
            }
        }
        stage('Deploy Service Registry') {
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("service-registry")
                }
            }
            steps {
                sh 'kubectl apply -f kubernetes/service-registry.yml'
            }
        }
        stage('Deploy User Service') {
            when{
                expression {
                    return "${BUILD_SERVICES}".contains("user-service")
                }
            }
            steps {
                sh 'kubectl apply -f kubernetes/user-service-deploy.yml'
            }
        }
        stage('Deployment status') {
            steps {
                sh 'kubectl get nodes'
                sh 'kubectl get pods'
            }
        }
    }
    // post {
    //     always {
    //         deleteDir() /* clean up our workspace */
    //     }
    // }
}