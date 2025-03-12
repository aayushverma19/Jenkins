
def call(String jobName, String buildNumber, String buildUrl, String toEmail) {
    slackSend(
        channel: 'notification', 
        color: 'good', 
        message: "Successful: Dependency Check passed and report published. Job Details - Name: ${jobName}, Build Number: ${buildNumber}, URL: ${buildUrl}"
    )
    emailext(
        attachmentsPattern: 'dependency-reports/dependency-check-report.html',
        body: """
            Hello,
            
            The Jenkins pipeline **${jobName}** has completed successfully on **Build #${buildNumber}**.
            
            **Build Details:**
            - **Job Name:** ${jobName}
            - **Build Number:** ${buildNumber}
            - **Build URL:** ${buildUrl}
            
            You can find the Dependency report attached.
            
            Best regards,  
            Jenkins CI
            Zero Downtime Crew
        """,
        subject: "Jenkins Build SUCCESS: ${jobName} #${buildNumber}",
        to: toEmail
    )
}
