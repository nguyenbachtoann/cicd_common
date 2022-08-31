/* groovylint-disable */
node {
    IMAGE_BASE_NAME = 'nguyenbachtoan/musiccard'
    COMMON_BRANCH = 'master'
    REPO = 'https://github.com/nguyenbachtoann/music-card.git'

    stage('Pull Code') {
        // checkout
        git url: REPO, branch: COMMON_BRANCH, credentialsId: 'github-nguyenbachtoann-credential'
        // create job name
        GIT_COMMIT = sh(script: 'git rev-parse HEAD | cut -c1-8', returnStdout: true).trim()
        currentBuild.displayName = "#${env.BUILD_NUMBER}_${GIT_COMMIT} (${COMMON_BRANCH})"
    }
    stage('Unit Test') {
        /*  if there are tests in the source, this stage will execute test command or
            frame work, and the result will be use at Code Scan
        */
        echo 'Tested'
    }
    stage('Build Image') {
        /*  set name for image: nguyenbachtoan/musiccard:abcxyz12
            simple format: account/image:git_commit */
        IMAGE = "${IMAGE_BASE_NAME}:${GIT_COMMIT}"
        sh "docker build -t ${IMAGE} . --build-arg NGINX_CONFIG_FILE=nginx.conf"
    }

    stage('Push To Artifactory (Docker Hub)') {
        // get the credential from Jenkins Credential, extract its USERNAME and PASSWORD
        withCredentials([usernamePassword(credentialsId: 'docker-hub-credential', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
            // inject variable from outside
            withEnv(["IMAGE=${IMAGE}"]) {
                sh '''
                    docker login -u ${USERNAME} -p ${PASSWORD}
                    docker push ${IMAGE}
                    # delete image for saving disk
                    docker rmi ${IMAGE}
                '''
            }
        }
    }

    stage('Deploy (Docker Compose)') {
        // run using docker compose, will serve IMAGE we just pushed to hub
        sh "IMAGE_NAME=${IMAGE} docker-compose up"
    }

    stage('Code Scan') {
        echo 'scanning'
    }
}
