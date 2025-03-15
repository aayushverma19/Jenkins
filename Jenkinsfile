@Library ('goBug@SCRUM-192') _

pipeline {
    agent any 
    tools {
        go 'Go'
    }
    stages{
        stage('clean-workspace'){
            steps{
                cleanWorkspace()
            }
        }
        stage('clone-repo'){
            steps{
                gitClone('main', 'https://github.com/OT-MICROSERVICES/employee-api.git' , 'git-cred' )
            }
        }
        stage('install'){
            steps{
                installGolangciLint()
            }
        }
        stage('run'){
            steps{
                runStaticCode()
            }
        }
    }
    post {
        always{
            publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: false, 
            reportDir: 'reports', reportFiles: 'Static_code-report.json', reportName: 'Bug Report'])
          }
          success {
            postBuildSuccess(JOB_NAME, BUILD_NUMBER, BUILD_URL, 'aayush.verma@mygurukulam.co')
          }
        failure {
            postBuildFailure(JOB_NAME, BUILD_NUMBER, BUILD_URL, 'aayush.verma@mygurukulam.co')
          }
    }
}
