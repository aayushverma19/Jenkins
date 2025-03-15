def call() {
  sh 'mkdir -p reports'
  sh './bin/golangci-lint run ./... --out-format json > reports/Bug-report.json || true'
}
