@Library ('React-CI@SCRUM-217') _

pipeline {
      agent any 
    tools {
        nodejs 'nodejs'  
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
              gitClone('main' , 'https://github.com/snaatak-Zero-Downtime-Crew/frontend.git' , 'git-cred')
          }
        }
      }
      stage('check dependency') {
        steps{
          script {
              reactDependency('dependency_report.txt')  
          }
        }
      }
  }
  post {
          always{    
              publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, icon: '', keepAll: false, reportDir: '', 
              reportFiles: 'dependency_report.txt', reportName: 'HTML Report', reportTitles: '', useWrapperFileDirectly: true])
          }
          success {
            postBuildSuccess(JOB_NAME, BUILD_NUMBER, BUILD_URL, 'aayush.verma@mygurukulam.co')
          }
        failure {
            postBuildFailure(JOB_NAME, BUILD_NUMBER, BUILD_URL, 'aayush.verma@mygurukulam.co')
          }
      }
}
