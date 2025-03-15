@Library ('JAVA-CIcd@scrum123') _

pipeline {
  agent any 
  tools {
        maven 'maven'
    }
    stages{
      stage('clean-workspace') {
        steps{
          script {
              cleanWorkspace()  
          }
        }
      }
      stage('clone repo') {
        steps{
          script {
              gitClone('main' , 'https://github.com/snaatak-Zero-Downtime-Crew/salary-api.git' , 'git-cred')
          }
        }
      }
      stage('unit test') {
        steps{
          script {
              runUnitTests()  
          }
        }
      }
  }
  post {
        always {
            junit stdioRetention: 'ALL', testResults: '**/target/**/*.xml'
        }
          success {
            postBuildSuccess(JOB_NAME, BUILD_NUMBER, BUILD_URL, 'aayush.verma@mygurukulam.co')
          }
        failure {
            postBuildFailure(JOB_NAME, BUILD_NUMBER, BUILD_URL, 'aayush.verma@mygurukulam.co')
          }
      }
}
