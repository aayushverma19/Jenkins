@Library ('Golang-CI@SCRUM-196') _

pipeline {
  agent any 
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
              gitClone('main' , 'https://github.com/snaatak-Zero-Downtime-Crew/employee-api.git' , 'git-cred')  
          }
        }
      }
      stage('dependency install') {
        steps{
          script {
              dependencyInstall('8.4.0', '${WORKSPACE}/dependency-check')  
          }
        }
      }
      stage('check dependency') {
        steps{
          script {
              GolangDependencyCheck()  
          }
        }
      }
  }
  post {
          always{    
              publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, icon: '', keepAll: false, reportDir: 'dependency-reports', 
              reportFiles: 'dependency-check-report.html', reportName: 'HTML Report', reportTitles: '', useWrapperFileDirectly: true])
          }
          success {
            postBuildSuccess(JOB_NAME, BUILD_NUMBER, BUILD_URL, 'aayush.verma@mygurukulam.co')
          }
        failure {
            postBuildFailure(JOB_NAME, BUILD_NUMBER, BUILD_URL, 'aayush.verma@mygurukulam.co')
          }
      }
}
