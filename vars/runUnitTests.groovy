def call() {
    sh 'mvn clean test jacoco:report | tee unit-test-report.txt'
}
