pipeline {
    agent any
    
    environment {
        email_recipients = "aayush.verma@mygurukulam.co"
    }
    
    tools {
        go 'Go'
    }

    stages {
        stage('clean workspace') {
            steps {
                script {
                    cleanWs()
                }
            }
        }
        stage('clone') {
            steps {
                git branch: 'main', url: 'https://github.com/OT-MICROSERVICES/employee-api.git'
            }
        }
        stage('Installation of goci lint') {
            steps {
                sh '''
                    
                    curl -sSfL https://raw.githubusercontent.com/golangci/golangci-lint/master/install.sh | sh -s latest
                    
                '''
            }
        }
        stage('To run the bug analyis and store in a folder'){
            steps {
                sh 'mkdir -p reports'
                sh './bin/golangci-lint run ./... --out-format json > reports/Bug-report.json || true'
            }
        }
        
    }

 post {
      always {
            echo "Publishing the Report"
            publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: false, 
            reportDir: 'reports', reportFiles: 'Bug-report.json', reportName: 'Bug Report'])
      }
     success {
            slackSend(channel: 'notification', color: 'good', username: 'Jenkins CI',  message: "Deployment Successful: DAST check passed and report published. Job Details - Name: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}, URL: ${env.BUILD_URL}")
            emailext(
                attachmentsPattern: "reports/Bug-report.json",
                body: """
                    Hello,
    
                    The Jenkins pipeline **${env.JOB_NAME}** has completed successfully on **Build #${env.BUILD_NUMBER}**.
    
                    **Build Details:**
                    - **Job Name:** ${env.JOB_NAME}
                    - **Build Number:** ${env.BUILD_NUMBER}
                    - **Build URL:** ${env.BUILD_URL}
    
                    You can find the Bug report attached.
    
                    Best regards,  
                    Jenkins CI
                    Zero Downtime Crew
                """,
                subject: "Jenkins Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                to: email_recipients
            )
        }
        failure {
            slackSend(channel: 'notification', color: 'danger', username: 'Jenkins CI', message: "FAILURE: DAST check Failed. Check log and console output. Job Details - Name: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}, URL: ${env.BUILD_URL}")

            emailext(
                attachmentsPattern: "reports/Bug-report.json",
                body: """
                    Hello,
    
                    The Jenkins pipeline **${env.JOB_NAME}** has failed on **Build #${env.BUILD_NUMBER}**.
    
                    **Job Details:**
                    - **Job Name:** ${env.JOB_NAME}
                    - **Build Number:** ${env.BUILD_NUMBER}
                    - **Build URL:** ${env.BUILD_URL}
    
                    Please review logs for more details.
    
                    Regards,  
                    Jenkins CI
                    Zero Downtime Crew
                """,
                subject: "Jenkins Pipeline FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                to: email_recipients
            )
        }
 
    }
}
