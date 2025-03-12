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
        stage('Clone Repository') {
            steps {
                git branch: 'main' , credentialsId: 'git-cred' , url: 'https://github.com/snaatak-Zero-Downtime-Crew/employee-api.git'
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
                  --project "${WORKSPACE}/employee-api" \
                  --scan . \
                  --format HTML \
                  --out security-reports
                '''
            }
        }
    }

    post {
        always {
            publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, icon: '', keepAll: false, reportDir: 'security-reports', 
            reportFiles: 'dependency-check-report.html', reportName: 'HTML Report', reportTitles: '', useWrapperFileDirectly: true])
        }
      
        }
    }
}
