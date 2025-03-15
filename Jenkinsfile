node {
    def git_url = 'https://github.com/snaatak-Zero-Downtime-Crew/frontend.git'
    def branch_name = 'main'
    def projectKey_name = 'test'
    def channel_name = 'notification'
    def email_recipients = 'aayush.verma@mygurukulam.co'
    
    try {
        stage('clean workspace') {
            cleanWs()
        }
        stage('git clone') {
            checkout([$class: 'GitSCM', branches: [[name: branch_name]], 
            userRemoteConfigs: [[url: git_url, credentialsId: 'git-cred']]
            ])
        }
        stage('Install Dependencies'){
            sh '''
                sudo apt install unzip -y
                wget https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-7.0.2.4839-linux-x64.zip
                unzip sonar-scanner-cli-7.0.2.4839-linux-x64.zip
                sudo rm -rf /opt/sonar-scanner
                sudo mv sonar-scanner-7.0.2.4839-linux-x64 /opt/sonar-scanner
                echo 'export PATH=$PATH:/opt/sonar-scanner/bin' >> ~/.bashrc
                . ~/.bashrc
            '''
        }
        stage('Static Code Analysis'){
            echo "Using SonarQube Project Key: ${projectKey_name}"
            withSonarQubeEnv('sonar') {
                withCredentials([string(credentialsId: 'sonarTocken', variable: 'SONARQUBE_TOKEN')]) {
                    sh """
                        /opt/sonar-scanner/bin/sonar-scanner \
                        -Dsonar.projectKey=${projectKey_name} \
                        -Dsonar.sources=. \
                        -Dsonar.token=${SONARQUBE_TOKEN}
                    """
                }
            }
        }
        stage('Notify Success') {
            slackSend(
                channel: "${channel_name}", 
                color: 'good', 
                message: "Successful: Bug report published on SonarQube Server. Job Details - Name: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}, URL: ${env.BUILD_URL}"
            )
            emailext(
                body: """
                    Hello,
                    
                    The Jenkins pipeline **${env.JOB_NAME}** has completed successfully on **Build #${env.BUILD_NUMBER}**.
                
                    **Build Details:**
                    - **Job Name:** ${env.JOB_NAME}
                    - **Build Number:** ${env.BUILD_NUMBER}
                    - **Build URL:** ${env.BUILD_URL}

                    React Bug report published on SonarQube Server project name:- ${env.projectKey_name}
                
                    Best regards,  
                    Jenkins CI
                    Zero Downtime Crew
                """,
                subject: "Jenkins Build SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                to: email_recipients
            )
        }
    } catch (Exception e) {
        stage('Notify Failure') {
            slackSend(
                channel: "${channel_name}", 
                color: 'danger', 
                username: 'Jenkins CI',
                message: "FAILURE: Bug Check Failed. Job Details - Name: ${env.JOB_NAME}, Build Number: ${env.BUILD_NUMBER}, URL: ${env.BUILD_URL}"
            )
            emailext(
                body: """
                    Hello,
                    
                    The Jenkins pipeline **${env.JOB_NAME}** has failed on **Build #${env.BUILD_NUMBER}**.
                
                    **Job Details:**
                    - **Job Name:** ${env.JOB_NAME}
                    - **Build Number:** ${env.BUILD_NUMBER}
                    - **Build URL:** ${env.BUILD_URL}
                
                    Please review logs and reports for more details.
                
                    Regards,  
                    Jenkins CI
                    Zero Downtime Crew
                """,
                subject: "Jenkins Pipeline FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                to: email_recipients 
            )
        }
        throw e
    }
}
