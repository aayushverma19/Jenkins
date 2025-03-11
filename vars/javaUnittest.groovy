def call(String branch, String repoUrl) {

pipeline {
  agent any
    stages{
        stage('clean workspace'){
            steps{
              script{
                deleteDir()
              }
            }
        }
        stage('clone repo'){
            steps{
                checkout([$class: 'GitSCM',
                              branches: [[name: "*/${branch}"]],
                              userRemoteConfigs: [[url: repoUrl]]])
            }
        }
        stage ('unit test'){
            steps{
                script {
                    def exitCode = sh(script: 'mvn test', returnStatus: true)
                    if (exitCode != 0) {
                        echo "Tests failed."
                    }
                }
            }
        }
    }
    post {
        always {
            junit stdioRetention: 'ALL', testResults: '**/target/**/*.xml'
        }
    }
  }
}
