@Library ('Golang-CI@SCRUM-196') _

pipeline {
  agent any 
    stages{
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
              dependencyInstall('8.4.0')  
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
            slackSend(channel: 'notification', color: 'good', message: "Successful: Dependency Check passed and report published. Job Details - Name: ${JOB_NAME}, Build Number: ${BUILD_NUMBER}, URL: ${BUILD_URL}")
                   emailext(
                  attachmentsPattern: 'dependency-reports/dependency-check-report.html',
                  body: """
                  Hello,
                  The Jenkins pipeline **${env.JOB_NAME}** has completed successfully on **Build #${env.BUILD_NUMBER}**.
                  
                  **Build Details:**
                  - **Job Name:** ${env.JOB_NAME}
                  - **Build Number:** ${env.BUILD_NUMBER}
                  - **Build URL:** ${env.BUILD_URL}
                  
                  You can find the Dependency report attached.
                  
                  Best regards,  
                  Jenkins CI
                  Zero Downtime Crew
                  """,
                      subject: "Jenkins Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                      to: 'aayush.verma@mygurukulam.co'
                      )
          }
          failure {
            slackSend(channel: 'notification', color: 'danger', message: "FAILURE: Dependency Check Failed. Check log and console output. Job Details - Name: ${JOB_NAME}, Build Number: ${BUILD_NUMBER}, URL: ${BUILD_URL}")
                        emailext(
                  attachmentsPattern: 'dependency-reports/dependency-check-report.html',
                  body: """
                  Hello,
                  The Jenkins pipeline **${env.JOB_NAME}** has failed on **Build #${env.BUILD_NUMBER}**.
                  
                  **Job Details:**
                  - **Job Name:** ${env.JOB_NAME}
                  - **Build Number:** ${env.BUILD_NUMBER}
                  - **Build URL:** ${env.BUILD_URL}
                  
                  Please review the attached logs and reports for more details.
                  
                  Regards,  
                  Jenkins CI
                  Zero Downtime Crew
                  """,
                          subject: "Jenkins Pipeline FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                          to: 'aayush.verma@mygurukulam.co'
              )
              
          }
      }
}
