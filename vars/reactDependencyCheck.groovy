def call () {
 sh '''
    DEP_CHECK_DIR="${WORKSPACE}/dependency-check"
    
      # Run Dependency-Check scan
      $DEP_CHECK_DIR/bin/dependency-check.sh \
        --project "${WORKSPACE}/forntend" \
        --scan . \
        --format HTML \
        --out dependency-reports
      '''
}
