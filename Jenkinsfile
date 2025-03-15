pipeline {
    agent any
   tools {
        maven 'maven'
    }
    environment {
        git_url  = "https://github.com/snaatak-Zero-Downtime-Crew/salary-api.git"
        branch_name = 'main'
        projectKey_name = 'demo'
        channel_name = 'notification'
        email_recipients = "aayush.verma@mygurukulam.co"
    }
    
    stages{
        stage('cleanworksapce'){
            steps{
                cleanWs()
            }
        }
        stage('gitclone'){
            steps{
                git url: git_url, branch: branch_name, credentialsId: 'git-cred'
            }
        }
        stage('bug'){
            steps{
                withSonarQubeEnv('sonar') {
                withCredentials([string(credentialsId: 'sonarTocken', variable: 'SONARQUBE_TOKEN')]) {
                sh '''
                mvn clean verify sonar:sonar \
                    -Dsonar.projectKey="${projectKey_name}" \
                    -Dsonar.token="${SONARQUBE_TOKEN}"
                '''
                }
                }
            }
        }
    }
    post{
        success {
            slackSend(channel: "${channel_name}", color: 'good', username: 'Jenkins CI',  message: "Successful: Bug report published on SonarQube Server. Job Details - Name: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}, URL: ${env.BUILD_URL}")
            emailext(
                body: """
                    Hello,
    
                    The Jenkins pipeline **${env.JOB_NAME}** has completed successfully on **Build #${env.BUILD_NUMBER}**.
    
                    **Build Details:**
                    - **Job Name:** ${env.JOB_NAME}
                    - **Build Number:** ${env.BUILD_NUMBER}
                    - **Build URL:** ${env.BUILD_URL}
    
                    Bug report published on SonarQube Server
    
                    Best regards,  
                    Jenkins CI
                    Zero Downtime Crew
                """,
                subject: "Jenkins Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                to: email_recipients
            )
        }
        failure {
            slackSend(channel: "${channel_name}", color: 'danger', username: 'Jenkins CI', message: "FAILURE: Bug Check Failed. Check log and console output. Job Details - Name: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}, URL: ${env.BUILD_URL}")

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
                    Jenkins CI
                    Zero Downtime Crew
                """,
                subject: "Jenkins Pipeline FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                to: email_recipients
            )
        }
    }
}

//sqa_198f7222eca06d47db66195bdfc057f909730042
//    -Dsonar.token=sqa_198f7222eca06d47db66195bdfc057f909730042
