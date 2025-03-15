def call() {
  sh 'mkdir -p reports'
  sh './bin/golangci-lint run ./... --out-format json > reports/Static_code-report.json || true'
}
