pipeline {
    stages {
        stage('Pull Code') {
            steps {
                echo 'Pulling..'
            }
        }

        stage('Test') {
            steps {
                echo 'Testing..'
            }
        }

        stage('Build') {
            steps {
                echo 'Building..'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }

        stage('Code Scan') {
            steps {
                echo 'Scanning....'
            }
        }
    }
}
