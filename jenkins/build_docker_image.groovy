pipeline {
    agent any
    environment {
        WORKSPACE = 'SOURCE_CODE'
    }
    parameters{
        string(name: 'BUILD_SERVICES', defaultValue: '', description: '') 
    }
    stages {
        stage("BUILD_DOCKER_IMAGE_${BUILD_SERVICES}") {
            step {
                echo "Build docker image ${BUILD_SERVICES}"
                "cd ${WORKSPACE} && ls"
            }
        }
    }
}