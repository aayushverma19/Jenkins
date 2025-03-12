def call(String branch, String repoUrl, String gitPassword ) {
    pipeline {
      agent any
    
      environment {
          DEP_CHECK_VERSION = "8.4.0"
          DEP_CHECK_DIR = "${WORKSPACE}/dependency-check"
      }
    
      stages {
          stage('clean Workspace') {
              steps {
                  script {
                      cleanWs()
                  }
              }
          }
          stage('clone repo'){
            steps{
                checkout([$class: 'GitSCM',
                              branches: [[name: "*/${branch}"]],
                              userRemoteConfigs: [[url: repoUrl , credentialsId: gitPassword ]]])
            }
          }
          stage('Setup Dependency-Check') {
              steps {
                  sh '''
                  # Download and extract Dependency-Check if not already present
                  if [ ! -d "$DEP_CHECK_DIR" ]; then
                      curl -sLO https://github.com/jeremylong/DependencyCheck/releases/download/v$DEP_CHECK_VERSION/dependency-check-$DEP_CHECK_VERSION-release.zip
                      unzip dependency-check-$DEP_CHECK_VERSION-release.zip
                  fi
                  
                  # Verify installation
                  $DEP_CHECK_DIR/bin/dependency-check.sh --version
                  '''
              }
          }
          stage('Run Dependency Check') {
              steps {
                  sh '''
                  # Run Dependency-Check scan
                  $DEP_CHECK_DIR/bin/dependency-check.sh \
                    --project "${WORKSPACE}/employee" \
                    --scan . \
                    --format HTML \
                    --out dependency-reports
                  '''
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
}
