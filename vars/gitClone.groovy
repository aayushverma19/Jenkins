def call(String branch, String repoUrl, String gitPassword ) {
  checkout([$class: 'GitSCM',
              branches: [[name: "*/${branch}"]],
              userRemoteConfigs: [[url: repoUrl , credentialsId: gitPassword ]]
           ])
}
