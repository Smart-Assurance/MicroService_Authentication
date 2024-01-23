pipeline {
    agent any
    
    environment {
        JENKINS_SSH_KEY = '/var/lib/jenkins/.ssh/id_rsa'
        REMOTE_USER = 'mohcineboudenjal'
        REMOTE_HOST = 'production-server'
        REMOTE_PATH = '/home/mohcineboudenjal/smartassurance/prod'
        JENKINS_HOME = '/home/mohcineboudenjal'
        JAR_NAME = 'microservice-authentication'  // Replace with your actual jar name
    }

    stages {
      stage('Connect SSH to remote and create directory of jar file with dockerfile') {
        steps {
            script {
                // Connect to the production server using SSH, create directory, and copy generate_dockerfile.sh

                sh "scp -i ${JENKINS_SSH_KEY} -o StrictHostKeyChecking=no ${JENKINS_HOME}/generate_dockerfile.sh ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}/${JAR_NAME} && chmod +x ${REMOTE_PATH}/${JAR_NAME}/generate_dockerfile.sh"

                sh """
                    ssh -i ${JENKINS_SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "
                        mkdir -p ${REMOTE_PATH}/${JAR_NAME} &&
                        cd ${REMOTE_PATH}/${JAR_NAME}
                    "
                    
                    # Connect again to execute the script
                    ssh -i ${JENKINS_SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "
                        cd ${REMOTE_PATH}/${JAR_NAME} &&
                        chmod +x generate_dockerfile.sh &&
                        ./generate_dockerfile.sh ${JAR_NAME}-0.0.1-SNAPSHOT.jar &&
                        rm generate_dockerfile.sh
                    "
                """
            }
        }
    }



        stage('Build and Copy JAR file into directory') {
            steps {
                script {
                    // Build the app locally
                    sh "mvn clean install"

                    // Copy the JAR to the remote path
                    sh "scp -i ${JENKINS_SSH_KEY} -o StrictHostKeyChecking=no /var/lib/jenkins/workspace/${JAR_NAME}/target/${JAR_NAME}-0.0.1-SNAPSHOT.jar ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_PATH}/${JAR_NAME}"
                }
            }
        }
// stage('reload docker images') {
//     steps {
//         script {
//             // Reconnect to SSH
//             sh "ssh -i ${JENKINS_SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} 'cd ${REMOTE_PATH}'"

//             // Remove existing containers
//             sh "ssh -i ${JENKINS_SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} 'cd ${REMOTE_PATH} && docker rm -f \$(docker ps -aq)'"

//             // Run Docker Compose
//             sh "ssh -i ${JENKINS_SSH_KEY} -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} 'cd ${REMOTE_PATH} && docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v \"/home/mohcineboudenjal/smartassurance/prod:/home/mohcineboudenjal/smartassurance/prod\" -w=\"/home/mohcineboudenjal/smartassurance/prod\" docker/compose:1.25.5 up'"
//         }
//     }
// }
}
}
