pipeline {
    agent any
    
    environment {
        email_recipients = "aayush.verma@mygurukulam.co"
        ZAP_report = "${WORKSPACE}/ZAP-report.html" 
        Url_ZAP_Attack = 'http://52.20.45.55:8080/actuator/health'
    }

    
    stages{
        stage('clean workspace'){
            steps{
             script {
                cleanWs()
              }
           }
        }
        stage('ZAP-install'){
            steps{
                sh '''
                wget https://github.com/zaproxy/zaproxy/releases/download/v2.16.0/ZAP_2_16_0_unix.sh
                sudo chmod +x ZAP_2_16_0_unix.sh
                sudo ./ZAP_2_16_0_unix.sh -q
                '''
            }
        }
        stage('DAST'){
            steps{
                sh 'zap.sh -cmd -quickurl ${Url_ZAP_Attack} -quickprogress -quickout ${ZAP_report}'
            }
        }
        stage('Publish Report'){
            steps{
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, icon: '', keepAll: false, reportDir: '${ZAP_report}', 
                reportFiles: 'report.html', reportName: 'HTML Report', reportTitles: '', useWrapperFileDirectly: true])
            }
        }
    }
    post {
        success {
            slackSend(channel: 'notification', color: 'good', username: 'Aayush Verma',  message: "Deployment Successful: DAST check passed and report published. Job Details - Name: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}, URL: ${env.BUILD_URL}")
            emailext(
                attachmentsPattern: ZAP_report,
                body: """
                    Hello,
    
                    The Jenkins pipeline **${env.JOB_NAME}** has completed successfully on **Build #${env.BUILD_NUMBER}**.
    
                    **Build Details:**
                    - **Job Name:** ${env.JOB_NAME}
                    - **Build Number:** ${env.BUILD_NUMBER}
                    - **Build URL:** ${env.BUILD_URL}
    
                    You can find the ZAP report attached.
    
                    Best regards,  
                    Aayush Verma
                    Zero Downtime Crew
                """,
                subject: "Jenkins Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                to: email_recipients
            )
        }
        failure {
            slackSend(channel: 'notification', color: 'danger', username: 'Aayush Verma', message: "FAILURE: DAST check Failed. Check log and console output. Job Details - Name: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}, URL: ${env.BUILD_URL}")

            emailext(
                body: """
                    Hello,
    
                    The Jenkins pipeline **${env.JOB_NAME}** has failed on **Build #${env.BUILD_NUMBER}**.
    
                    **Job Details:**
                    - **Job Name:** ${env.JOB_NAME}
                    - **Build Number:** ${env.BUILD_NUMBER}
                    - **Build URL:** ${env.BUILD_URL}
    
                    Please review logs for more details.
    
                    Regards,  
                    Aayush Verma
                    Zero Downtime Crew
                """,
                subject: "Jenkins Pipeline FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                to: email_recipients
            )
        }
    }
}
