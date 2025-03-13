node {
    environment {
        PATH = "$HOME/.local/bin:$PATH"
    }
    
    try {
        stage('Clean Workspace') {
            cleanWs() 
        }

        stage('Install Package') {
            sh 'sudo apt update'
            sh 'sudo apt install -y python3-pip python3-pytest'
            sh 'pip install --break-system-packages coverage'
        }

        stage('Clone Repository') {
            git branch: 'main', credentialsId: 'git-cred', url: 'https://github.com/snaatak-Zero-Downtime-Crew/attendance-api.git'
        }

        stage('Run Tests with Coverage') {
            sh 'python3 -m coverage run -m pytest || python3 -m coverage report -m'
            sh 'python3 -m coverage html'
        }

        stage('Publish Report') {
            publishHTML([
                allowMissing: false, alwaysLinkToLastBuild: false, icon: '', keepAll: false, reportDir: 'htmlcov', reportFiles: 'index.html', 
                reportName: 'HTML Report', reportTitles: '', useWrapperFileDirectly: true ])
        }

        stage('Notify Success') {
            slackSend(
                channel: 'notification', 
                color: 'good', 
                message: "Successful: Check Code Coverage report. Job Details - Name: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}, URL: ${env.BUILD_URL}"
            )
            emailext(
                attachmentsPattern: "htmlcov/index.html", 
                body: """
                    Hello,
                    
                    The Jenkins pipeline **${env.JOB_NAME}** has completed successfully on **Build #${env.BUILD_NUMBER}**.
                
                    **Build Details:**
                    - **Job Name:** ${env.JOB_NAME}
                    - **Build Number:** ${env.BUILD_NUMBER}
                    - **Build URL:** ${env.BUILD_URL}
                
                    You can find the Code coverage report attached.
                
                    Best regards,  
                    Aayush Verma
                    Zero Downtime Crew
                """,
                subject: "Jenkins Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                to: "aayush.verma@mygurukulam.co" 
            )
        }
    } catch (Exception e) {
        stage('Notify Failure') {
            slackSend(
                channel: 'notification', 
                color: 'danger', 
                username: 'Aayush Verma',
                message: "FAILURE: Check log and console output. Job Details - Name: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}, URL: ${env.BUILD_URL}"
            )
            emailext(
                attachmentsPattern: "htmlcov/index.html",  
                body: """
                    Hello,
                    
                    The Jenkins pipeline **${env.JOB_NAME}** has failed on **Build #${env.BUILD_NUMBER}**.
                
                    **Job Details:**
                    - **Job Name:** ${env.JOB_NAME}
                    - **Build Number:** ${env.BUILD_NUMBER}
                    - **Build URL:** ${env.BUILD_URL}
                
                    Please review logs and reports for more details.
                
                    Regards,  
                    Aayush Verma
                    Zero Downtime Crew
                """,
                subject: "Jenkins Pipeline FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                to: "aayush.verma@mygurukulam.co" 
            )
        }
        throw e
    }
}
