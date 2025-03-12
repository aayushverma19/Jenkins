pipeline {
    agent any
    
    stages{
        stage('clean workspace'){
            steps{
             script {
                deleteDir()
              }
           }
        }
        stage('test'){
            steps{
                sh 'zap.sh -cmd -quickurl http://52.20.45.55:8080/actuator/health -quickprogress -quickout /var/lib/jenkins/report.html'
            }
        }
        stage('public report'){
            steps{
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, icon: '', keepAll: false, reportDir: '/var/lib/jenkins/', 
                reportFiles: 'report.html', reportName: 'HTML Report', reportTitles: '', useWrapperFileDirectly: true])
            }
        }
    }
    post {
        success {
          slackSend(channel: 'notification', color: 'good', message: "Deployment Successful: DAST check passed and report published. Job Details - Name: ${JOB_NAME}, Build Number: ${BUILD_NUMBER}, URL: ${BUILD_URL}")
         }
         failure {
            slackSend(channel: 'notification', color: 'danger', message: "FAILURE: DAST check Failed. Check log and console output. Job Details - Name: ${JOB_NAME}, Build Number: ${BUILD_NUMBER}, URL: ${BUILD_URL}")
        }
    }
}
