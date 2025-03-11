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
               sh 'mvn test'
            }
        }
    }
    post {
        always {
            junit stdioRetention: 'ALL', testResults: '**/target/**/*.xml'
        }
        success {
          slackSend(channel: 'test-case', color: 'good', message: "Successful: Test case passed and report published. Job Details - Name: ${JOB_NAME}, Build Number: ${BUILD_NUMBER}, URL: ${BUILD_URL}")
        }
        failure {
          slackSend(channel: 'test-case', color: 'danger', message: "FAILURE:  Test case Failed. Check log and console output. Job Details - Name: ${JOB_NAME}, Build Number: ${BUILD_NUMBER}, URL: ${BUILD_URL}")
      }
    }
  }
}
