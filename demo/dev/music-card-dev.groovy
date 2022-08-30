node {
    stage('Pull Code') {
        echo 'pulling...'
    }
    stage('Unit Test') {
        echo 'testing...'
    }
    stage('Build Image & Push To Docker Hub') {
        echo 'building & pushing'
    }
    stage('Deploy') {
        echo 'deploying'
    }
    stage('Code Scan') {
        echo 'scanning'
    }
}
