# Jenkins

post {
        always {
            publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, icon: '', keepAll: false, reportDir: 'htmlcov', 
            reportFiles: 'index.html', reportName: 'HTML Report', reportTitles: '', useWrapperFileDirectly: true])
        }
        success {
          slackSend(channel: 'test-case', color: 'good', message: "Successful: Test case passed and report published. Job Details - Name: ${JOB_NAME}, Build Number: ${BUILD_NUMBER}, URL: ${BUILD_URL}")
                 emailext(
                //attachmentsPattern: 'htmlcov/index.html',
                body: """
                Hello,
                The Jenkins pipeline **${env.JOB_NAME}** has completed successfully on **Build #${env.BUILD_NUMBER}**.
                
                **Build Details:**
                - **Job Name:** ${env.JOB_NAME}
                - **Build Number:** ${env.BUILD_NUMBER}
                - **Build URL:** ${env.BUILD_URL}
                
                You can find the test coverage report attached.
                
                Best regards,  
                Jenkins CI
                Zero Downtime Crew
                """,
                    subject: "Jenkins Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                    to: 'aayush.verma@mygurukulam.co'
                    )
        }
        failure {
          slackSend(channel: 'test-case', color: 'danger', message: "FAILURE:  Test case Failed. Check log and console output. Job Details - Name: ${JOB_NAME}, Build Number: ${BUILD_NUMBER}, URL: ${BUILD_URL}")
                      emailext(
                //attachmentsPattern: 'htmlcov/index.html',
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
