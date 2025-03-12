def call(String jobName, String buildNumber, String buildUrl, String toEmail) {
    slackSend(
        channel: 'notification', 
        color: 'danger', 
        message: "FAILURE: Test case Failed. Check log and console output. Job Details - Name: ${jobName}, Build Number: ${buildNumber}, URL: ${buildUrl}"
    )

    emailext(
        body: """
            Hello,
            
            The Jenkins pipeline **${jobName}** has failed on **Build #${buildNumber}**.
            
            **Job Details:**
            - **Job Name:** ${jobName}
            - **Build Number:** ${buildNumber}
            - **Build URL:** ${buildUrl}
            
            Please review the attached logs and reports for more details.
            
            Regards,  
            Jenkins CI
            Zero Downtime Crew
        """,
        subject: "Jenkins Pipeline FAILED: ${jobName} #${buildNumber}",
        to: toEmail
    )
}
